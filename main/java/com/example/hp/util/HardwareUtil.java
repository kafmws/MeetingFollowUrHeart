package com.example.hp.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.hp.activity.BaseActivity;

import cn.wch.ch34xuartdriver.CH34xUARTDriver;

public class HardwareUtil {

    private static final String ACTION_USB_PERMISSION = "com.hp.driver.USB_PERMISSION";
    private String TAG = "HardwareUtil";
    private Activity activity;
    private UsbManager mUsbManager;

    private boolean isOpen;
    private boolean opening = false;
    private Handler handler;
    private int re;

    private byte[] writeBuffer;
    private byte[] readBuffer;

    private final int baudRate = 4800;
    private final byte stopBit = 1;
    private final byte dataBit = 8;
    private byte parity = 0;
    private byte flowControl = 0;

    public HardwareUtil(Activity activity) {
        this.activity = activity;
        tryGetUsbPermission();
    }

    public boolean openTheDoor(){
        if(!isOpen){
            init();
            isOpen = true;
        }
        byte[] to_send = toByteArray("11");
//				byte[] to_send = toByteArray2(writeText.getText().toString());
        int re = BaseActivity.driver.WriteData(to_send, to_send.length);//写数据，第一个参数为需要发送的字节数组，第二个参数为需要发送的字节长度，返回实际发送的字节长度
        if (re < 0){
            Log.e(TAG, "写入失败");
        } else {
            opening = !opening;
            Log.e(TAG, "写入成功");
        }
        return opening;
    }


    private final BroadcastReceiver mUsbPermissionActionReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice usbDevice = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        //user choose YES for your previously popup window asking for grant perssion for this usb device
                        if(null != usbDevice){
//                            afterGetUsbPermission(usbDevice);
                        }
                    }
                    else {
                        //user choose NO for your previously popup window asking for grant perssion for this usb device
                        Toast.makeText(context, String.valueOf("Permission denied for device" + usbDevice), Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    };

    private void init() {
        BaseActivity.driver = new CH34xUARTDriver(
                (UsbManager) activity.getSystemService(Context.USB_SERVICE), activity,
                ACTION_USB_PERMISSION);

        if (!BaseActivity.driver.UsbFeatureSupported()){// 判断系统是否支持USB HOST
            Dialog dialog = new AlertDialog.Builder(activity)
                    .setTitle("提示")
                    .setMessage("您的设备不支持USB HOST，请更换其他设备再试！")
                    .setPositiveButton("确认",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0,
                                                    int arg1) {
                                    System.exit(0);
                                }
                            }).create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
        // 保持常亮的屏幕常亮
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        writeBuffer = new byte[512];
        readBuffer = new byte[512];
        isOpen = false;

        handler = new Handler() {
            public void handleMessage(Message msg){
                Toast.makeText(activity,(String)msg.obj,Toast.LENGTH_SHORT).show();
            }
        };

        tryGetUsbPermission();

        if(openDevice()){
            baudRateSet();
        }
    }

    private void tryGetUsbPermission(){
        mUsbManager = (UsbManager) activity.getSystemService(Context.USB_SERVICE);

        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        activity.registerReceiver(mUsbPermissionActionReceiver, filter);

        PendingIntent mPermissionIntent = PendingIntent.getBroadcast(activity, 0, new Intent(ACTION_USB_PERMISSION), 0);

        //here do emulation to ask all connected usb device for permission
        for (final UsbDevice usbDevice : mUsbManager.getDeviceList().values()) {
            //add some conditional check if necessary
            //if(isWeCaredUsbDevice(usbDevice)){
            if(mUsbManager.hasPermission(usbDevice)){
                //if has already got permission, just goto connect it
                //that means: user has choose yes for your previously popup window asking for grant perssion for this usb device
                //and also choose option: not ask again


//                afterGetUsbPermission(usbDevice);
            }else{
                //this line will let android popup window, ask user whether to allow this app to have permission to operate this usb device
                mUsbManager.requestPermission(usbDevice, mPermissionIntent);
            }
            //}
        }
    }

    public void unregisterReceiver(){
        activity.unregisterReceiver(mUsbPermissionActionReceiver);
    }

    private boolean openDevice(){
        if (!isOpen) {
            re = BaseActivity.driver.ResumeUsbList();
            if (re == -1)// ResumeUsbList方法用于枚举CH34X设备以及打开相关设备
            {
                Toast.makeText(activity, "打开设备失败!",
                        Toast.LENGTH_SHORT).show();
                BaseActivity.driver.CloseDevice();
            } else if (re == 0) {
                if (!BaseActivity.driver.UartInit()) {//对串口设备进行初始化操作
                    Toast.makeText(activity, "设备初始化失败!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(activity, "打开设备失败!", Toast.LENGTH_SHORT).show();
                    return false;
                }

                Toast.makeText(activity, "打开设备成功!",
                        Toast.LENGTH_SHORT).show();
                isOpen = true;
                new readThread().start();//开启读线程读取串口接收的数据
            } else {
//                    mUsbManager.requestPermission(usbDevice, mPermissionIntent);
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("未授权限");
                builder.setMessage("确认退出吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
//								MainFragmentActivity.this.finish();
                        System.exit(0);
                    }
                });
                builder.setNegativeButton("返回", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                });
                builder.show();

            }
        } else {
            BaseActivity.driver.CloseDevice();
            isOpen = false;
        }
        return isOpen;
    }

    //配置串口波特率，函数说明可参照编程手册
    private void baudRateSet(){
        if (BaseActivity.driver.SetConfig(baudRate, dataBit, stopBit, parity, flowControl)) {
            Toast.makeText(activity, "串口设置成功!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, "串口设置失败!", Toast.LENGTH_SHORT).show();
        }
    }

    private class readThread extends Thread {

        public void run() {

            byte[] buffer = new byte[4096];

            while (true) {

                Message msg = Message.obtain();
                if (!isOpen) {
                    break;
                }
                int length = BaseActivity.driver.ReadData(buffer, 4096);
                if (length > 0) {
                    String recv = toHexString(buffer, length);
//					String recv = new String(buffer, 0, length);
                    msg.obj = recv;
                    handler.sendMessage(msg);
                }
            }
        }
    }

    /**
     * 将byte[]数组转化为String类型
     * @param arg
     *            需要转换的byte[]数组
     * @param length
     *            需要转换的数组长度
     * @return 转换后的String队形
     */
    private String toHexString(byte[] arg, int length) {
        String result = new String();
        if (arg != null) {
            for (int i = 0; i < length; i++) {
                result = result
                        + (Integer.toHexString(
                        arg[i] < 0 ? arg[i] + 256 : arg[i]).length() == 1 ? "0"
                        + Integer.toHexString(arg[i] < 0 ? arg[i] + 256
                        : arg[i])
                        : Integer.toHexString(arg[i] < 0 ? arg[i] + 256
                        : arg[i])) + " ";
            }
            return result;
        }
        return "";
    }

    /**
     * 将String转化为byte[]数组
     * @param arg
     *            需要转换的String对象
     * @return 转换后的byte[]数组
     */
    private byte[] toByteArray(String arg) {
        if (arg != null) {
            /* 1.先去除String中的' '，然后将String转换为char数组 */
            char[] NewArray = new char[1000];
            char[] array = arg.toCharArray();
            int length = 0;
            for (int i = 0; i < array.length; i++) {
                if (array[i] != ' ') {
                    NewArray[length] = array[i];
                    length++;
                }
            }
            /* 将char数组中的值转成一个实际的十进制数组 */
            int EvenLength = (length % 2 == 0) ? length : length + 1;
            if (EvenLength != 0) {
                int[] data = new int[EvenLength];
                data[EvenLength - 1] = 0;
                for (int i = 0; i < length; i++) {
                    if (NewArray[i] >= '0' && NewArray[i] <= '9') {
                        data[i] = NewArray[i] - '0';
                    } else if (NewArray[i] >= 'a' && NewArray[i] <= 'f') {
                        data[i] = NewArray[i] - 'a' + 10;
                    } else if (NewArray[i] >= 'A' && NewArray[i] <= 'F') {
                        data[i] = NewArray[i] - 'A' + 10;
                    }
                }
                /* 将 每个char的值每两个组成一个16进制数据 */
                byte[] byteArray = new byte[EvenLength / 2];
                for (int i = 0; i < EvenLength / 2; i++) {
                    byteArray[i] = (byte) (data[i * 2] * 16 + data[i * 2 + 1]);
                }
                return byteArray;
            }
        }
        return new byte[] {};
    }

    /**
     * 将String转化为byte[]数组
     * @param arg
     *            需要转换的String对象
     * @return 转换后的byte[]数组
     */
    private byte[] toByteArray2(String arg) {
        if (arg != null) {
            /* 1.先去除String中的' '，然后将String转换为char数组 */
            char[] NewArray = new char[1000];
            char[] array = arg.toCharArray();
            int length = 0;
            for (int i = 0; i < array.length; i++) {
                if (array[i] != ' ') {
                    NewArray[length] = array[i];
                    length++;
                }
            }
            NewArray[length] = 0x0D;
            NewArray[length + 1] = 0x0A;
            length += 2;

            byte[] byteArray = new byte[length];
            for (int i = 0; i < length; i++) {
                byteArray[i] = (byte)NewArray[i];
            }
            return byteArray;

        }
        return new byte[] {};
    }
}

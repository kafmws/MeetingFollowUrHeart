package com.example.hp.activity;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.FaceEngine;

import cn.wch.ch34xuartdriver.CH34xUARTDriver;

import static com.arcsoft.face.ErrorInfo.MERR_ASF_ALREADY_ACTIVATED;
import static com.example.hp.constant.AppData.getArcAppId;
import static com.example.hp.constant.AppData.getArcSdkKey;
import static com.example.hp.constant.AppData.setDeviceID;
import static com.example.hp.util.QRCodeProducer.getUniqueId;

public abstract class BaseActivity extends AppCompatActivity {

    public static CH34xUARTDriver driver;
    public static FaceEngine engine = new FaceEngine();

    private String TAG = "BaseActivity";
    private static final String ACTION_USB_PERMISSION = "com.hp.driver.USB_PERMISSION";
    private static final int PERMISSIONS = 110;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setDeviceID(getUniqueId(this));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getWindow().getDecorView().
                    setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
        super.onCreate(savedInstanceState);
//        tryGetUsbPermission();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setStatusBarColor(int statusColor) {
        Window window = getWindow();
        //取消状态栏透明
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //添加Flag把状态栏设为可绘制模式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        window.setStatusBarColor(statusColor);
        //设置系统状态栏处于可见状态
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        //让view不根据系统窗口来调整自己的布局
        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, false);
            ViewCompat.requestApplyInsets(mChildView);
        }
    }

    public void activeExam(){
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        boolean ACTIVE_FLAG = sp.getBoolean("isActive",false);
        if(!ACTIVE_FLAG){//未激活，进行激活操作
//            FaceEngine engine = new FaceEngine();
            int errorCode = engine.active(this,getArcAppId(),getArcSdkKey());
            if(errorCode!=ErrorInfo.MOK&&errorCode!=MERR_ASF_ALREADY_ACTIVATED) {//激活失败
                Log.e("com.arcsoft", "AFR_FSDK_FailToActive : " + errorCode);
                Toast.makeText(this, "激活失败", Toast.LENGTH_SHORT).show();
            }else{//更新激活状态
                Log.e("com.arcsoft", "AFR_FSDK_ActiveSuccess : " + errorCode);
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("isActive",true);
                editor.apply();
            }
        }
    }

    public void PermissionCheck(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                ||ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.READ_PHONE_STATE,
                    android.Manifest.permission.CAMERA,
                    },PERMISSIONS);
        }else{
            Log.e(TAG, "权限正常");
        }
        //tryGetUsbPermission();
    }

//    private void tryGetUsbPermission(){
//        UsbManager mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
//
//        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
//        registerReceiver(mUsbPermissionActionReceiver, filter);
//
//        PendingIntent mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
//
//        //here do emulation to ask all connected usb device for permission
//        for (final UsbDevice usbDevice : mUsbManager.getDeviceList().values()) {
//            //add some conditional check if necessary
//            //if(isWeCaredUsbDevice(usbDevice)){
//            if(mUsbManager.hasPermission(usbDevice)){
//                //if has already got permission, just goto connect it
//                //that means: user has choose yes for your previously popup window asking for grant perssion for this usb device
//                //and also choose option: not ask again
//
//
////                afterGetUsbPermission(usbDevice);
//            }else{
//                //this line will let android popup window, ask user whether to allow this app to have permission to operate this usb device
//                mUsbManager.requestPermission(usbDevice, mPermissionIntent);
//                Log.e(TAG,"USB权限申请ing");
//            }
//            //}
//        }
//    }
//
//    private final BroadcastReceiver mUsbPermissionActionReceiver = new BroadcastReceiver() {
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (ACTION_USB_PERMISSION.equals(action)) {
//                synchronized (this) {
//                    UsbDevice usbDevice = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
//                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
//                        //user choose YES for your previously popup window asking for grant perssion for this usb device
//                        if(null != usbDevice){
////                            afterGetUsbPermission(usbDevice);
//                        }
//                    }
//                    else {
//                        //user choose NO for your previously popup window asking for grant perssion for this usb device
//                        Toast.makeText(context, String.valueOf("Permission denied for device" + usbDevice), Toast.LENGTH_LONG).show();
//                        tryGetUsbPermission();
//                    }
//                }
//            }
//        }
//    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSIONS:
                boolean activePermission = true;
                for(int i : grantResults){
                    if(i!=PermissionChecker.PERMISSION_GRANTED){
                        activePermission = false;
                        Log.e(TAG, permissions[i]+" failed");
                        finish();
                    }
                }
                if(activePermission){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            activeExam();
                        }
                    }).start();
                };
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

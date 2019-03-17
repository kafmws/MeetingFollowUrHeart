package com.example.hp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.constant.AppData;
import com.example.hp.model.R;

import java.text.SimpleDateFormat;

public class DetailsActivity extends BaseActivity {

    private String TAG = "DetailsActivity";
    private final int msgTime = 1;
    private StringBuilder time;
    private SimpleDateFormat timeFormatter = new SimpleDateFormat("MM/dd HH:mm:ss");

    private TextView tv_doorplate;
    private TextView tv_status;
    private TextView tv_date;
    private TextView tv_time;
    private Button btn_absence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
        PermissionCheck();

        setContentView(R.layout.layout_activity_details);
        initialize();
    }

    public void initialize() {
        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        AppData.setHasDeviceId(sp.getBoolean("hasDeviceId", false));
        if (AppData.hasDeviceId()) {//已初始化
            AppData.setDeviceID(sp.getString("deviceId", null));
            findViewById(R.id.fragment_init).setVisibility(View.GONE);
            tv_doorplate = findViewById(R.id.tv_doorplate);
            tv_status = findViewById(R.id.tv_status);
            tv_date = findViewById(R.id.tv_date);
            tv_time = findViewById(R.id.tv_time);
            btn_absence = findViewById(R.id.btn_absence);
            btn_absence.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PermissionCheck();
                    Log.e(TAG,"准备签到");
                    Intent intent = new Intent(DetailsActivity.this, RecognitionActivity.class);
                    startActivity(intent);
                }
            });
            (new TimeThread()).start();
        } else {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.fragment_init, new InitializeFragment());
            transaction.commit();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragment_init);
        if (fragment != null) {
            manager.beginTransaction().remove(fragment).commit();
        }
    }

    class TimeThread extends Thread {
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = msgTime;
                    handler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    private Handler handler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case msgTime:
                    String[] s = getTime().split("-");
                    tv_date.setText(s[0]);
                    tv_time.setText(s[1]);
                    break;
                default:
                    break;
            }
        }
    };

    //获得当前年月日时分秒星期
    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getTime() {
        time = new StringBuilder();
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        time.append(timeFormatter.format(calendar.getTime()));
        switch (calendar.get(Calendar.DAY_OF_WEEK)-1){
            case 0:time.insert(5," Sun.-");break;
            case 1:time.insert(5," Mon.-");break;
            case 2:time.insert(5," Tues.-");break;
            case 3:time.insert(5," Wed.-");break;
            case 4:time.insert(5," Thur.-");break;
            case 5:time.insert(5," Fri.-");break;
            case 6:time.insert(5," Sat.-");break;
            default:
                time.insert(5,"-"+(calendar.get(Calendar.DAY_OF_WEEK)-1));
                break;
        }
        return time.toString();
    }

}

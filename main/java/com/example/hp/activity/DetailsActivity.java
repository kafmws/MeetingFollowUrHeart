package com.example.hp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.adapter.MeetingsAdapter;
import com.example.hp.constant.AppData;
import com.example.hp.model.MeetingDetails;
import com.example.hp.model.MeetingRoom;
import com.example.hp.model.R;
import com.example.hp.util.OkHttpHelper;
import com.example.hp.util.ResponseDataParser;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.hp.constant.AppData.getApi;
import static com.example.hp.constant.AppData.getDoorplate;

public class DetailsActivity extends BaseActivity {

    private String TAG = "DetailsActivity";
    private final int msgTime = 1;
    private StringBuilder time;
    private SimpleDateFormat timeFormatter = new SimpleDateFormat("MM/dd HH:mm:ss");
    private List<MeetingDetails> meetings;
    private MeetingsAdapter adapter;

    private TextView tv_doorplate;
    private TextView tv_status;
    private TextView tv_date;
    private TextView tv_time;
    private Button btn_absence;
    private TextView tv_head;
    private RecyclerView rv_meetings;


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
            rv_meetings = findViewById(R.id.rv_meetings);
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
            getMeetings();
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

    public void showQRCode(View view) {

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

    private void getMeetings(){
        StringBuilder builder = new StringBuilder(getApi());
        builder.append("?roomNumber=");
        builder.append(getDoorplate());
        OkHttpHelper.SendOkHttpRequest(builder.toString(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(DetailsActivity.this, "网络超时", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                MeetingRoom.DataBean dataBean =
                        ResponseDataParser.RoomDataParser(response.body().string());
                if(dataBean.getStatus()==0){
                    meetings = ResponseDataParser.MeetingsDetailsParser(dataBean);
                    if(adapter == null){
                        adapter = new MeetingsAdapter(meetings, DetailsActivity.this);
                        rv_meetings.setLayoutManager(new LinearLayoutManager(DetailsActivity.this));
                        rv_meetings.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }else{
                    Toast.makeText(DetailsActivity.this, "网络超时", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}

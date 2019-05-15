package com.example.hp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.adapter.MeetingsAdapter;
import com.example.hp.adapter.SignInStatusAdapter;
import com.example.hp.constant.AppData;
import com.example.hp.constant.CheckInStatus;
import com.example.hp.model.MeetingRoom;
import com.example.hp.model.R;
import com.example.hp.model.UserFaceInfoPack;
import com.example.hp.util.OkHttpHelper;
import com.example.hp.util.QRCodeProducer;
import com.example.hp.util.ResponseDataParser;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.hp.model.MeetingRoom.DataBean.MeetingListsBean;
import static com.example.hp.constant.AppData.getDoorplate;
import static com.example.hp.util.TimeUtil.date2time;
import static com.example.hp.util.TimeUtil.setSecond;

public class DetailsActivity extends BaseActivity {

    private String TAG = "DetailsActivity";
    private final int RECOGNITION_CODE = 0;
    private final int msgTime = 1;
    private final int msgSetAdapter = 2;
    private StringBuilder time;
    private SimpleDateFormat timeFormatter = new SimpleDateFormat("MM/dd HH:mm:ss");
    private MeetingRoom room;
    private List<MeetingListsBean> meetings = new ArrayList<>();
    private List<UserFaceInfoPack.UserFaceInfo> userFaceInfo = new ArrayList<>();//用户信息
    private MeetingListsBean currentMeeting;
    private MeetingsAdapter meetingsAdapter;
    private SignInStatusAdapter statusAdapter;
    private boolean isPay = true;

    private TextView tv_status;
    private TextView tv_date;
    private TextView tv_time;
    private ImageButton ib_absence;
    private TextView tv_head;
    private RecyclerView rv_meetings;
    private RecyclerView rv_signInStatus;
    private ImageView iv_QRIdCode;
    private PopupWindow qrWindow;
    private TextView tv_today;
    private TextView tv_signInStatus;
    private NestedScrollView ns_meetings;
    private NestedScrollView ns_signInStatus;

    //currentMeetingView
    private TextView tv_meeting_name;
    private TextView tv_meeting_status;//出现就是进行中
    private TextView tv_meeting_time;
    private TextView tv_cIntroduce;
    private TextView tv_signInPercent;//带参数的TextView


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
        PermissionCheck();

        setContentView(R.layout.layout_activity_details);
        initialize();
//        tv_today.setOnClickListener;
        tv_signInStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentMeeting == null) {
                    Toast.makeText(DetailsActivity.this, "当前无会议进行", Toast.LENGTH_SHORT).show();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        tv_signInStatus.setBackground(getDrawable(R.drawable.gradual_change_light));
//                        tv_today.setBackgroundColor(Color.rgb(65,105,225));
                    }
                    ns_signInStatus.setVisibility(View.VISIBLE);
                    rv_signInStatus.setVisibility(View.VISIBLE);
                    rv_meetings.setVisibility(View.GONE);
                    ns_meetings.setVisibility(View.GONE);
                    getUserInfo();
                }
            }
        });
    }

    public void tv_todayOnClick(View view) {
        ns_meetings.setVisibility(View.VISIBLE);
        rv_meetings.setVisibility(View.VISIBLE);
        ns_signInStatus.setVisibility(View.GONE);
        rv_signInStatus.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    tv_today.setBackground(getDrawable(R.drawable.gradual_change_light));
            tv_signInStatus.setBackgroundColor(Color.rgb(65, 105, 225));
        }
    }

    public void initialize() {
        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        AppData.setHasDoorplate(sp.getBoolean("hasDoorplate", false));
        AppData.setDoorplate(sp.getString("doorplate", null));
        if (AppData.isHasDoorplate() && getDoorplate() != null) {//已初始化
            AppData.setDeviceID(sp.getString("doorplate", null));
            findViewById(R.id.fragment_init).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.tv_doorplate)).setText(getDoorplate());
            tv_status = findViewById(R.id.tv_status);
            tv_date = findViewById(R.id.tv_date);
            tv_time = findViewById(R.id.tv_time);
            ib_absence = findViewById(R.id.ib_absence);

            tv_meeting_name = findViewById(R.id.tv_meeting_name);
            tv_meeting_time = findViewById(R.id.tv_meeting_time);
            tv_meeting_status = findViewById(R.id.tv_meeting_status);
            tv_meeting_status.setVisibility(View.GONE);
            tv_cIntroduce = findViewById(R.id.tv_cIntroduce);
            tv_signInPercent = findViewById(R.id.tv_signIn);

            tv_today = findViewById(R.id.tv_today);
            tv_signInStatus = findViewById(R.id.tv_signInStatus);
            tv_signInStatus.setBackgroundColor(Color.rgb(65,105,225));

            rv_signInStatus = findViewById(R.id.rv_signInStatus);
            rv_signInStatus.setVisibility(View.GONE);
            statusAdapter = new SignInStatusAdapter(userFaceInfo, this);
            rv_signInStatus.setAdapter(statusAdapter);
            rv_signInStatus.setLayoutManager(new LinearLayoutManager(this));
            ns_signInStatus = findViewById(R.id.ns_signInStatus);
            ns_signInStatus.setVisibility(View.GONE);
            ns_meetings = findViewById(R.id.ns_meetings);
            rv_meetings = findViewById(R.id.rv_meetings);
            meetingsAdapter = new MeetingsAdapter(meetings, this);
            rv_meetings.setAdapter(meetingsAdapter);
            rv_meetings.setLayoutManager(new LinearLayoutManager(this));
            ib_absence.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PermissionCheck();
                    if (currentMeeting == null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(DetailsActivity.this, "暂无会议进行", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }
                    Log.e(TAG, "准备签到");
                    Intent intent = new Intent(DetailsActivity.this, RecognitionActivity.class);
                    intent.putExtra("currentMeetingId", currentMeeting.getMeetingId());
                    intent.putExtra("beginTime", currentMeeting.getStartTime());
                    startActivityForResult(intent, RECOGNITION_CODE);
                }
            });
            (new TimeThread()).start();
            (new examThread()).start();
            getMeetings();
        } else {
            Log.e(TAG, getDoorplate() + " null");
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.fragment_init, new InitializeFragment());
            transaction.commit();
        }
        intiQRWindow();
    }

    private void cntSignInPercent() {
        int absence = 0;
        for (UserFaceInfoPack.UserFaceInfo info : userFaceInfo) {
            if (info.getUserStatus() == CheckInStatus.LATE || info.getUserStatus() == CheckInStatus.NORMAL) {
                absence++;
            }
        }
        if (userFaceInfo.size() != 0) {
            tv_signInPercent.setText(String.format("到会人数: %d  出勤率: %d%%", absence, (int)(absence*100.0 / userFaceInfo.size())));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case RECOGNITION_CODE:
                if (resultCode == RESULT_OK) {
                    Log.e(TAG, "出勤改变");
                    View view = LayoutInflater.from(this).inflate(R.layout.layout_sign_in_success, null, false);
                    PopupWindow window = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT, true);
                    window.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                    window.setOnDismissListener(popWindowListener);
                    setBackgroundAlpha(0.5f);
                }
                cntSignInPercent();
                break;
            default:
                break;
        }
    }

    PopupWindow.OnDismissListener popWindowListener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            setBackgroundAlpha(1);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragment_init);
        if (fragment != null) {
            manager.beginTransaction().remove(fragment).commit();
        }
        isPay = false;
    }

//    public void showQRCode(View view) {
//        iv_QR.setVisibility(View.VISIBLE);
//        iv_QR.setImageBitmap(QRCodeProducer.getStandardQRCode(this));
////        iv_QR.getBackground().setAlpha(100);
//        QRVisible = true;
//    }

    private void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha; //0.0-1.0
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }

    public void showQRCode(View view) {
        iv_QRIdCode.setImageBitmap(QRCodeProducer.getStandardQRCode(getApplicationContext(), String.valueOf(room.getData().getId())));
        qrWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        setBackgroundAlpha(0.5f);
    }

//    @Override
//    public void onBackPressed() {
//        if(QRVisible){
//            iv_QR.setVisibility(View.GONE);
//        }
//        else
//            super.onBackPressed();
//    }

    class examThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getMeetings();
                if(currentMeeting!=null){
                    getUserInfo();
                }
            }
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

    private void setCurrentMeeting() {
        if (currentMeeting == null) {
            tv_status.setText("空闲");
            tv_signInStatus.setTextColor(Color.rgb(204, 204, 204));
            tv_meeting_status.setVisibility(View.GONE);
            tv_meeting_name.setText("暂无会议");
            tv_meeting_time.setText("");
            tv_cIntroduce.setText("");
            tv_signInPercent.setText("");
            tv_todayOnClick(tv_today);
        } else {
            meetings.remove(currentMeeting);
            tv_status.setText("使用中");
            tv_meeting_status.setVisibility(View.VISIBLE);
            tv_signInStatus.setTextColor(Color.WHITE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                super.setStatusBarColor(Color.YELLOW);
            }
            tv_meeting_name.setText(currentMeeting.getMeetingName());
            tv_meeting_time.setText(setSecond(date2time(currentMeeting.getStartTime()))
                    + " ~ " + setSecond(date2time(currentMeeting.getEndTime())));
            tv_cIntroduce.setText(currentMeeting.getMeetingIntro());
        }

    }

    private Handler handler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case msgTime:
                    if (isPay) {
                        String[] s = getTime().split("-");
                        tv_date.setText(s[0]);
                        tv_time.setText(s[1]);
                    }
                    break;
                case msgSetAdapter:
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
        switch (calendar.get(Calendar.DAY_OF_WEEK) - 1) {
            case 0:
                time.insert(5, " Sun.-");
                break;
            case 1:
                time.insert(5, " Mon.-");
                break;
            case 2:
                time.insert(5, " Tues.-");
                break;
            case 3:
                time.insert(5, " Wed.-");
                break;
            case 4:
                time.insert(5, " Thur.-");
                break;
            case 5:
                time.insert(5, " Fri.-");
                break;
            case 6:
                time.insert(5, " Sat.-");
                break;
            default:
                time.insert(5, "-" + (calendar.get(Calendar.DAY_OF_WEEK) - 1));
                break;
        }
        return time.toString();
    }

    private void getMeetings() {
        StringBuilder builder = new StringBuilder("http://www.shidongxuan.top/smartMeeting_Web/access/getInfoByRoomNumber.do");
        builder.append("?roomNumber=");
        builder.append(getDoorplate());
        Log.e(TAG, getDoorplate() + " null");
        OkHttpHelper.SendOkHttpRequest(builder.toString(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "???");
                        Toast.makeText(DetailsActivity.this, "网络超时", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e(TAG, response == null ? "Re==null" : "Re!=null");
                room = ResponseDataParser.RoomMeetingsParser(response.body().string());
                Log.e(TAG, room == null ? "Room==null" : "Room!=null");
                Log.e(TAG, String.valueOf(room.getStatus()));
                if (room != null && room.getStatus() == 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!meetings.isEmpty())
                                meetings.clear();
                            meetings.addAll(room.getData().getMeetingLists());
                            meetingsAdapter.notifyDataSetChanged();
                            currentMeeting = findCurrentMeeting();
                            setCurrentMeeting();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DetailsActivity.this, "网络超时", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void getUserInfo() {
        String url = "http://www.shidongxuan.top/smartMeeting_Web/access/getAllUserStatus.do?meetingId=" + currentMeeting.getMeetingId();
        OkHttpHelper.SendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "网络请求失败");
                        Toast.makeText(DetailsActivity.this, "数据获取失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!userFaceInfo.isEmpty())
                    userFaceInfo.clear();
                userFaceInfo.addAll(ResponseDataParser.userFaceInfoParser(response.body().string()));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        statusAdapter.notifyDataSetChanged();
                        cntSignInPercent();
                    }
                });
            }
        });
    }

    private MeetingListsBean findCurrentMeeting() {
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        for (MeetingListsBean meeting : meetings) {
            if (meeting.getStartTime().compareTo(date) <= 0 && meeting.getEndTime().compareTo(date) > 0) {
                return meeting;
            }
        }
        return null;
    }

    private void intiQRWindow() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.qr_code_pw, null);
        iv_QRIdCode = contentView.findViewById(R.id.iv_QRRoomId);
        qrWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        qrWindow.setOnDismissListener(popWindowListener);
    }

    /*
     *   测试用
     */
//    private void intiCurrentMeeting(){
//        List<MeetingListsBean.MemberStatusBean> list = new ArrayList<MeetingListsBean.MemberStatusBean>();
//        list.add(new MeetingListsBean.MemberStatusBean(1,"LL", 1));
//        currentMeeting = new MeetingListsBean(1,
//                "", "",1,"","", 2, null,
//                101, "FZ101", 11,"LL", list);
//        demo();
//    }

    /*
     *   测试用
     */
//    void demo(){
//        List<MeetingListsBean.MemberStatusBean> list = new ArrayList<MeetingListsBean.MemberStatusBean>();
//        list.add(new MeetingListsBean.MemberStatusBean(1,"LL", 1));
//            meetings.add(new MeetingListsBean(1,
//                    "班助选举", "",60,"15:00:00","16:30:00", 2, null,
//                    101, "FZ101", 11,"范宗兰", list));
//            meetings.add(new MeetingListsBean(1,
//                    "小组例会", "",40,"17:00:00","18:30:00", 2, null,
//                    101, "FZ101", 11,"李玉彬", list));
//        meetings.add(new MeetingListsBean(1,
//                "学院例会", "",20,"19:00:00","20:30:00", 2, null,
//                101, "FZ101", 11,"Tony", list));
//        meetings.add(new MeetingListsBean(1,
//                "比赛事宜讨论", "",25,"21:00:00","23:00:00", 2, null,
//                101, "FZ101", 11,"王等闲", list));
//    }
}

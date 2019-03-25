package com.example.hp.util;

import android.util.Log;

import com.example.hp.constant.AppData;
import com.example.hp.model.MeetingDetails;
import com.example.hp.model.MeetingRoom;
import com.example.hp.model.MeetingRoom.DataBean.MeetingBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

public class ResponseDataParser {

    private static String TAG = "ResponseDataParser";

    private static Gson parser = new Gson();

    private static MeetingRoom RoomMeetingsParser(String data) {
        MeetingRoom room = parser.fromJson(data, new TypeToken<MeetingRoom>() {
        }.getType());
        return room.getStatus() == 0 ? room : null;
    }

    public static MeetingRoom.DataBean RoomDataParser(String data) {
        MeetingRoom room = RoomMeetingsParser(data);
        MeetingRoom.DataBean dataBean = null;
        if (room != null) {
            dataBean = room.getData();
        }
        return dataBean;
    }

    private static MeetingDetails MeetingParser(String data) {
        return parser.fromJson(data, new TypeToken<MeetingDetails>() {}.getType());
    }

    public static List<MeetingDetails> MeetingsDetailsParser(MeetingRoom.DataBean dataBean) {
        List<MeetingBean> recentlyMeetings = dataBean.getRecentlyMeetings();
        final List<MeetingDetails> meetings = new ArrayList<>();
        for (final MeetingBean recentlyMeeting : recentlyMeetings) {
            OkHttpHelper.PostOkHttpRequest(
                    new Request.Builder()
                            .url(AppData.getApi())
                            .post(new FormBody.Builder()
                                    .add("meetingId", String.valueOf(recentlyMeeting.getId())).build())
                            .build()
                    , new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e(TAG, "会议完整数据请求失败");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            meetings.add(MeetingParser(response.body().string()));
                        }
                    });
        }
        return meetings;
    }

//    public String getMeetingMasterName(int masterId){
//        
//    }

}

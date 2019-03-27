package com.example.hp.util;

import android.util.Base64;
import android.util.Log;

import com.arcsoft.face.FaceFeature;
import com.example.hp.model.CheckInStatusResponse;
import com.example.hp.model.FaceFutureInfo;
import com.example.hp.model.MeetingCheckInStatus;
import com.example.hp.model.MeetingRoom;
import com.example.hp.model.UserFaceInfo;
import com.example.hp.model.ValidateDoorplate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResponseDataParser {

    private static String TAG = "ResponseDataParser";

    private static Gson parser = new Gson();

    public static MeetingRoom RoomMeetingsParser(String data) {
        Log.e(TAG, data);
        MeetingRoom room = parser.fromJson(data, new TypeToken<MeetingRoom>() {}.getType());
        return room;
    }

    public static boolean validateDoorplateParser(String data){
        Log.e(TAG, data);
        ValidateDoorplate check = parser.fromJson(data, new TypeToken<ValidateDoorplate>(){}.getType());
        return check.getStatus()==0;
    }

    public static List<UserFaceInfo> faceFeaturesParser(String data){
        Log.e(TAG, data);
        FaceFutureInfo faceFutureInfoList = parser.fromJson(data, new TypeToken<FaceFutureInfo>(){}.getType());
        List<UserFaceInfo> list = new ArrayList<>();
        for (FaceFutureInfo.DataBean info: faceFutureInfoList.getData()) {
            list.add(new UserFaceInfo(info.getUserId(), info.getUserName(),
                    new FaceFeature(Base64.decode(info.getFaceData(), 0)) ));
        }
        return list;
    }

    public static MeetingCheckInStatus meetingCheckInStatusParser(String data){
        return parser.fromJson(data, new TypeToken<MeetingCheckInStatus>(){}.getType());
    }

    public static CheckInStatusResponse checkInStatusResponseParser(String data){
        return parser.fromJson(data, new TypeToken<CheckInStatusResponse>(){}.getType());
    }
}

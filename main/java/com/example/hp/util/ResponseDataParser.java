package com.example.hp.util;

import android.util.Log;

import com.example.hp.model.CheckInStatusResponse;
import com.example.hp.model.FaceFutureInfoPack;
import com.example.hp.model.FaceFutureInfoPack.FaceFutureInfo;
import com.example.hp.model.MeetingCheckInStatus;
import com.example.hp.model.MeetingRoom;
import com.example.hp.model.UserFaceInfoPack;
import com.example.hp.model.ValidateDoorplate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

    public static List<UserFaceInfoPack.UserFaceInfo> userFaceInfoParser(String data){
        Log.e("111",data);
        return ((UserFaceInfoPack)parser.fromJson(data,new TypeToken<UserFaceInfoPack>(){}.getType())).getData();
    }

    public static List<FaceFutureInfo> faceFeaturesParser(String data){
        Log.e("FaceFeaturesData",data);
      return ((FaceFutureInfoPack)parser.fromJson(data, new TypeToken<FaceFutureInfoPack>(){}.getType())).getData();
    }

//    public static MeetingCheckInStatus meetingCheckInStatusParser(String data){
//        return parser.fromJson(data, new TypeToken<MeetingCheckInStatus>(){}.getType());
//    }

    public static CheckInStatusResponse checkInStatusResponseParser(String data){
        Log.e("CheckInStatusData",data);
        return parser.fromJson(data, new TypeToken<CheckInStatusResponse>(){}.getType());
    }
}

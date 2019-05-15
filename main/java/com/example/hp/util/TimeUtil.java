package com.example.hp.util;

import android.graphics.Rect;
import com.arcsoft.face.FaceInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TimeUtil {

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");

    public static int subtract(String str1, String str2) {
//        return Integer.parseInt(str1.substring(11,12))+Integer.parseInt(str1.substring(11,12))-Integer.parseInt(str2.substring(11,12));
        try {
            Date date1 = format.parse(str1);
            Date date2 = format.parse(str2);
            return (int) ((date1.getTime()-date2.getTime())/1000/60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String date2time(String str) {
        return str.substring(11);
    }

    //治标不治本  没办法
    public static String setSecond(String str) {
        return str.substring(0,str.length()-2)+"00";
    }

    public static void keepMaxFace(List<FaceInfo> ftFaceList) {
        if (ftFaceList == null || ftFaceList.size() <= 1) {
            return;
        }
        FaceInfo maxFaceInfo = ftFaceList.get(0);
        for (FaceInfo faceInfo : ftFaceList) {
            if (faceInfo.getRect().width() > maxFaceInfo.getRect().width()) {
                maxFaceInfo = faceInfo;
            }
        }
        ftFaceList.clear();
        ftFaceList.add(maxFaceInfo);
    }

}

package com.example.hp.constant;

public class AppData {

    private static final String ArcAppId = "QCAJicnx5Z1K7f7pPEBRRPYN6DMcYD5v9CDs3isAaSj";
    private static final String ArcSdkKey = "2SWeGxKMUqjGQodm9nD7oW3YfABrSqwbvRRD9Tyja9y6";
    private static boolean hasDoorplate;
    private static String deviceID;
    private static String doorplate;

    public static String getDeviceID() {
        return deviceID;
    }

    public static void setDeviceID(String deviceID) {
        AppData.deviceID = deviceID;
    }

    public static boolean isHasDoorplate() {
        return hasDoorplate;
    }

    public static void setHasDoorplate(boolean hasDeviceId) {
        AppData.hasDoorplate = hasDeviceId;
    }

    public static String getArcAppId() {
        return ArcAppId;
    }

    public static String getArcSdkKey() {
        return ArcSdkKey;
    }

    public static String getDoorplate() {
        return doorplate;
    }

    public static void setDoorplate(String doorplate) {
        AppData.doorplate = doorplate;
    }

}

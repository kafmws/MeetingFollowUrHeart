package com.example.hp.model;

import com.arcsoft.face.FaceFeature;

public class UserFaceInfo {
    /**
     * userId : 1000
     * userName : wcmwe
     * faceData : sadddddddddddddddasdfasdasdqweqweretrrgfwwwwwwwwwwwwwwwwwwwwww
     */

    private int userId;
    private String userName;
    private FaceFeature faceFeature;

    public UserFaceInfo(int userId, String userName, FaceFeature faceFeature) {
        this.userId = userId;
        this.userName = userName;
        this.faceFeature = faceFeature;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public FaceFeature getFaceFeature() {
        return faceFeature;
    }

    public void setFaceFeature(FaceFeature faceFeature) {
        this.faceFeature = faceFeature;
    }
}

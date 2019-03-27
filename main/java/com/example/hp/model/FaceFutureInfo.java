package com.example.hp.model;

import java.util.List;

public class FaceFutureInfo {

    /**
     * status : 0
     * data : [{"userId":1000,"userName":"wcmwe","faceData":"sadddddddddddddddasdfasdasdqweqweretrrgfwwwwwwwwwwwwwwwwwwwwww"},{"userId":1001,"userName":"shidongxuan","faceData":"sadddddddddddddddasdfasdasdqweqweretrrgfwwwwwwwwwwwwwwwwwwwwww"},{"userId":1002,"userName":"wangshun","faceData":"sadddddddddddddddasdfasdasdqweqweretrrgfwwwwwwwwwwwwwwwwwwwwww"}]
     */

    private int status;
    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * userId : 1000
         * userName : wcmwe
         * faceData : sadddddddddddddddasdfasdasdqweqweretrrgfwwwwwwwwwwwwwwwwwwwwww
         */

        private int userId;
        private String userName;
        private String faceData;

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

        public String getFaceData() {
            return faceData;
        }

        public void setFaceData(String faceData) {
            this.faceData = faceData;
        }
    }
}

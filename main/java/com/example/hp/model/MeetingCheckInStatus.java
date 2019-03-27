package com.example.hp.model;

import java.util.List;

public class MeetingCheckInStatus {

    /**
     * status : 0
     * data : [{"userId":1000,"username":"wcmwe","userStatus":1},{"userId":1001,"username":"shidongxuan","userStatus":1},{"userId":1002,"username":"wangshun","userStatus":2}]
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
         * username : wcmwe
         * userStatus : 1
         */

        private int userId;
        private String username;
        private int userStatus;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public int getUserStatus() {
            return userStatus;
        }

        public void setUserStatus(int userStatus) {
            this.userStatus = userStatus;
        }
    }
}

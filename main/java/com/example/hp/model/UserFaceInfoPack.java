package com.example.hp.model;

import java.util.List;

public class UserFaceInfoPack {

    /**
     * status : 0
     * data : [{"userId":1004,"username":"刘沛栋","avatarUrl":"http://www.shidongxuan.top/image/28d5841b-84ef-4a17-8467-f825eedd9307.jpg","userStatus":2},{"userId":1009,"username":"辣鸡","avatarUrl":"http://www.shidongxuan.top/image/c5f2f949-adf5-4a1c-86c1-0474064176c4.jpg","userStatus":2},{"userId":1007,"username":"张银鸽","avatarUrl":"http://www.shidongxuan.top/image/1dbdbe8b-7f8b-4718-9860-060e335e064b.png","userStatus":2}]
     */

    private int status;

    private List<UserFaceInfo> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<UserFaceInfo> getData() {
        return data;
    }

    public void setData(List<UserFaceInfo> data) {
        this.data = data;
    }

    public static class UserFaceInfo {
        /**
         * userId : 1004
         * username : 刘沛栋
         * avatarUrl : http://www.shidongxuan.top/image/28d5841b-84ef-4a17-8467-f825eedd9307.jpg
         * userStatus : 2
         */

        private int userId;
        private String username;
        private String avatarUrl;
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

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public int getUserStatus() {
            return userStatus;
        }

        public void setUserStatus(int userStatus) {
            this.userStatus = userStatus;
        }
    }
}

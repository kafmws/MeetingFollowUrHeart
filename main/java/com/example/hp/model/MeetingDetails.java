package com.example.hp.model;

import java.util.List;

public class MeetingDetails {

    /**
     * status : 0
     * data : {"meetingId":1002,"meetingName":"比赛会议","meetingIntro":"开始比赛","peopleNum":2,"startTime":"2019-03-15 19:28:32","endTime":"2019-03-16 19:28:32","status":3,"userStatus":null,"memberStatus":[{"userId":1000,"username":"wcmwe","userStatus":4},{"userId":1002,"username":"wangshun","userStatus":1}],"roomId":2,"roomName":"FZ102","masterId":1002}
     */

    private int status;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * meetingId : 1002
         * meetingName : 比赛会议
         * meetingIntro : 开始比赛
         * peopleNum : 2
         * startTime : 2019-03-15 19:28:32
         * endTime : 2019-03-16 19:28:32
         * status : 3
         * userStatus : null
         * memberStatus : [{"userId":1000,"username":"wcmwe","userStatus":4},{"userId":1002,"username":"wangshun","userStatus":1}]
         * roomId : 2
         * roomName : FZ102
         * masterId : 1002
         */

        private int meetingId;
        private String meetingName;
        private String meetingIntro;
        private int peopleNum;
        private String startTime;
        private String endTime;
        private int status;
        private Object userStatus;
        private int roomId;
        private String roomName;
        private int masterId;
        private List<MemberStatusBean> memberStatus;

        public int getMeetingId() {
            return meetingId;
        }

        public void setMeetingId(int meetingId) {
            this.meetingId = meetingId;
        }

        public String getMeetingName() {
            return meetingName;
        }

        public void setMeetingName(String meetingName) {
            this.meetingName = meetingName;
        }

        public String getMeetingIntro() {
            return meetingIntro;
        }

        public void setMeetingIntro(String meetingIntro) {
            this.meetingIntro = meetingIntro;
        }

        public int getPeopleNum() {
            return peopleNum;
        }

        public void setPeopleNum(int peopleNum) {
            this.peopleNum = peopleNum;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public Object getUserStatus() {
            return userStatus;
        }

        public void setUserStatus(Object userStatus) {
            this.userStatus = userStatus;
        }

        public int getRoomId() {
            return roomId;
        }

        public void setRoomId(int roomId) {
            this.roomId = roomId;
        }

        public String getRoomName() {
            return roomName;
        }

        public void setRoomName(String roomName) {
            this.roomName = roomName;
        }

        public int getMasterId() {
            return masterId;
        }

        public void setMasterId(int masterId) {
            this.masterId = masterId;
        }

        public List<MemberStatusBean> getMemberStatus() {
            return memberStatus;
        }

        public void setMemberStatus(List<MemberStatusBean> memberStatus) {
            this.memberStatus = memberStatus;
        }

        public static class MemberStatusBean {
            /**
             * userId : 1000
             * username : wcmwe
             * userStatus : 4
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
}

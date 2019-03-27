package com.example.hp.model;

import java.util.List;

public class MeetingRoom {


    /**
     * status : 0
     * data : {"roomNumber":"FZ101","content":30,"machineNumber":"0000000001","status":1,"meetingLists":[{"meetingId":1010,"meetingName":"纳新庆祝会议","meetingIntro":"哈哈哈哈哈","peopleNum":2,"startTime":"2019-03-28 21:32:04","endTime":"2019-03-29 21:32:04","status":3,"userStatus":null,"memberStatus":[{"userId":1000,"username":"wcmwe","userStatus":4},{"userId":1002,"username":"wangshun","userStatus":1}],"roomId":1,"roomName":"FZ101","masterId":1002,"masterName":"wangshun"}],"id":1}
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
         * roomNumber : FZ101
         * content : 30
         * machineNumber : 0000000001
         * status : 1
         * meetingLists : [{"meetingId":1010,"meetingName":"纳新庆祝会议","meetingIntro":"哈哈哈哈哈","peopleNum":2,"startTime":"2019-03-28 21:32:04","endTime":"2019-03-29 21:32:04","status":3,"userStatus":null,"memberStatus":[{"userId":1000,"username":"wcmwe","userStatus":4},{"userId":1002,"username":"wangshun","userStatus":1}],"roomId":1,"roomName":"FZ101","masterId":1002,"masterName":"wangshun"}]
         * id : 1
         */

        private String roomNumber;
        private int content;
        private String machineNumber;
        private int status;
        private int id;
        private List<MeetingListsBean> meetingLists;

        public String getRoomNumber() {
            return roomNumber;
        }

        public void setRoomNumber(String roomNumber) {
            this.roomNumber = roomNumber;
        }

        public int getContent() {
            return content;
        }

        public void setContent(int content) {
            this.content = content;
        }

        public String getMachineNumber() {
            return machineNumber;
        }

        public void setMachineNumber(String machineNumber) {
            this.machineNumber = machineNumber;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<MeetingListsBean> getMeetingLists() {
            return meetingLists;
        }

        public void setMeetingLists(List<MeetingListsBean> meetingLists) {
            this.meetingLists = meetingLists;
        }

        public static class MeetingListsBean {
            /**
             * meetingId : 1010
             * meetingName : 纳新庆祝会议
             * meetingIntro : 哈哈哈哈哈
             * peopleNum : 2
             * startTime : 2019-03-28 21:32:04
             * endTime : 2019-03-29 21:32:04
             * status : 3
             * userStatus : null
             * memberStatus : [{"userId":1000,"username":"wcmwe","userStatus":4},{"userId":1002,"username":"wangshun","userStatus":1}]
             * roomId : 1
             * roomName : FZ101
             * masterId : 1002
             * masterName : wangshun
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
            private String masterName;
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

            public String getMasterName() {
                return masterName;
            }

            public void setMasterName(String masterName) {
                this.masterName = masterName;
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
}

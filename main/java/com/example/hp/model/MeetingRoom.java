package com.example.hp.model;

import java.util.List;

public class MeetingRoom {

    /**
     * status : 0
     * data : {"roomNumber":"FZ102","content":40,"machineNumber":"0000000002","status":2,"recentlyMeetings":[{"id":1002,"meetingName":"比赛会议","meetingIntro":"开始比赛","roomId":1001,"status":3,"masterId":1002,"startTime":1552829397000,"endTime":1552915797000,"createTime":1552570197000,"updateTime":1552570197000}],"id":1001}
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
         * roomNumber : FZ102
         * content : 40
         * machineNumber : 0000000002
         * status : 2
         * recentlyMeetings : [{"id":1002,"meetingName":"比赛会议","meetingIntro":"开始比赛","roomId":1001,"status":3,"masterId":1002,"startTime":1552829397000,"endTime":1552915797000,"createTime":1552570197000,"updateTime":1552570197000}]
         * id : 1001
         */

        private String roomNumber;
        private int content;
        private String machineNumber;
        private int status;
        private int id;
        private List<MeetingBean> recentlyMeetings;

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

        public List<MeetingBean> getRecentlyMeetings() {
            return recentlyMeetings;
        }

        public void setRecentlyMeetings(List<MeetingBean> recentlyMeetings) {
            this.recentlyMeetings = recentlyMeetings;
        }

        public static class MeetingBean {
            /**
             * id : 1002
             * meetingName : 比赛会议
             * meetingIntro : 开始比赛
             * roomId : 1001
             * status : 3
             * masterId : 1002
             * startTime : 1552829397000
             * endTime : 1552915797000
             * createTime : 1552570197000
             * updateTime : 1552570197000
             */

            private int id;
            private String meetingName;
            private String meetingIntro;
            private int roomId;
            private int status;
            private int masterId;
            private long startTime;
            private long endTime;
            private long createTime;
            private long updateTime;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
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

            public int getRoomId() {
                return roomId;
            }

            public void setRoomId(int roomId) {
                this.roomId = roomId;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getMasterId() {
                return masterId;
            }

            public void setMasterId(int masterId) {
                this.masterId = masterId;
            }

            public long getStartTime() {
                return startTime;
            }

            public void setStartTime(long startTime) {
                this.startTime = startTime;
            }

            public long getEndTime() {
                return endTime;
            }

            public void setEndTime(long endTime) {
                this.endTime = endTime;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public long getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(long updateTime) {
                this.updateTime = updateTime;
            }
        }
    }
}

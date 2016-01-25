package com.jhlc.grouppurchase.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/12/26.
 */
public class GroupBean {


    /**
     * apprid :
     * code : 100
     * groupList : [{"createuserid":"000001","groupid":5,"grouptype":1,"icon":"1","membercount":1,"notice":"5173四大才子群","tribeId":0,"tribeName":""}]
     * msg : 成功
     * opCode : getGroupList
     * status : success
     */

    private String apprid;
    private int code;
    private String msg;
    private String opCode;
    private String status;
    /**
     * createuserid : 000001
     * groupid : 5
     * grouptype : 1
     * icon : 1
     * membercount : 1
     * notice : 5173四大才子群
     * tribeId : 0
     * tribeName :
     */

    private List<GroupListEntity> groupList;

    public void setApprid(String apprid) {
        this.apprid = apprid;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setOpCode(String opCode) {
        this.opCode = opCode;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setGroupList(List<GroupListEntity> groupList) {
        this.groupList = groupList;
    }

    public String getApprid() {
        return apprid;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getOpCode() {
        return opCode;
    }

    public String getStatus() {
        return status;
    }

    public List<GroupListEntity> getGroupList() {
        return groupList;
    }

    public static class GroupListEntity {
        private String createuserid;
        private int groupid;
        private int grouptype;
        private String icon;
        private int membercount;
        private String notice;
        private int tribeId;
        private String tribeName;

        public void setCreateuserid(String createuserid) {
            this.createuserid = createuserid;
        }

        public void setGroupid(int groupid) {
            this.groupid = groupid;
        }

        public void setGrouptype(int grouptype) {
            this.grouptype = grouptype;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public void setMembercount(int membercount) {
            this.membercount = membercount;
        }

        public void setNotice(String notice) {
            this.notice = notice;
        }

        public void setTribeId(int tribeId) {
            this.tribeId = tribeId;
        }

        public void setTribeName(String tribeName) {
            this.tribeName = tribeName;
        }

        public String getCreateuserid() {
            return createuserid;
        }

        public int getGroupid() {
            return groupid;
        }

        public int getGrouptype() {
            return grouptype;
        }

        public String getIcon() {
            return icon;
        }

        public int getMembercount() {
            return membercount;
        }

        public String getNotice() {
            return notice;
        }

        public int getTribeId() {
            return tribeId;
        }

        public String getTribeName() {
            return tribeName;
        }

        @Override
        public String toString() {
            return "GroupListEntity{" +
                    "createuserid='" + createuserid + '\'' +
                    ", groupid=" + groupid +
                    ", grouptype=" + grouptype +
                    ", icon='" + icon + '\'' +
                    ", membercount=" + membercount +
                    ", notice='" + notice + '\'' +
                    ", tribeId=" + tribeId +
                    ", tribeName='" + tribeName + '\'' +
                    '}';
        }
    }


}

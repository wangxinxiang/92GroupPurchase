package com.jhlc.grouppurchase.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/1/16.
 */
public class GroupTypeBean{

    /**
     * apprid :
     * code : 100
     * msg : 成功
     * opCode : getWxqGroupTypeList
     * status : success
     * wxqTypeList : [{"createdate":"2016-01-15 03:44:09","icon":"1","orderindex":1,"status":1,"typeid":1,"typename":"二手"}]
     */

    private String apprid;
    private int code;
    private String msg;
    private String opCode;
    private String status;
    /**
     * createdate : 2016-01-15 03:44:09
     * icon : 1
     * orderindex : 1
     * status : 1
     * typeid : 1
     * typename : 二手
     */

    private List<WxqTypeListEntity> wxqTypeList;

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

    public void setWxqTypeList(List<WxqTypeListEntity> wxqTypeList) {
        this.wxqTypeList = wxqTypeList;
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

    public List<WxqTypeListEntity> getWxqTypeList() {
        return wxqTypeList;
    }

    public static class WxqTypeListEntity{
        private String createdate;
        private String icon;
        private int orderindex;
        private int status;
        private int typeid;
        private String typename;

        public void setCreatedate(String createdate) {
            this.createdate = createdate;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public void setOrderindex(int orderindex) {
            this.orderindex = orderindex;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public void setTypeid(int typeid) {
            this.typeid = typeid;
        }

        public void setTypename(String typename) {
            this.typename = typename;
        }

        public String getCreatedate() {
            return createdate;
        }

        public String getIcon() {
            return icon;
        }

        public int getOrderindex() {
            return orderindex;
        }

        public int getStatus() {
            return status;
        }

        public int getTypeid() {
            return typeid;
        }

        public String getTypename() {
            return typename;
        }

        @Override
        public String toString() {
            return "WxqTypeListEntity{" +
                    "createdate='" + createdate + '\'' +
                    ", icon='" + icon + '\'' +
                    ", orderindex=" + orderindex +
                    ", status=" + status +
                    ", typeid=" + typeid +
                    ", typename='" + typename + '\'' +
                    '}';
        }
    }

}

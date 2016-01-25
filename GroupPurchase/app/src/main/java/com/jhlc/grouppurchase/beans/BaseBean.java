package com.jhlc.grouppurchase.beans;

/**
 * Created by Administrator on 2016/1/16.
 */
public class BaseBean {

    /**
     * apprid :
     * code : 100
     * msg : 创建成功
     * opCode : userRegist
     * status : success
     */

    private String apprid;
    private int code;
    private String msg;
    private String opCode;
    private String status;

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

    @Override
    public String toString() {
        return "BaseBean{" +
                "apprid='" + apprid + '\'' +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                ", opCode='" + opCode + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

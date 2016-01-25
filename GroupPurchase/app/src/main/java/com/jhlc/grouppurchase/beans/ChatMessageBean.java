package com.jhlc.grouppurchase.beans;

import java.util.List;

/**
 * Created by LiCola on  2015/12/30  14:33
 * 聊天界面用的商品类
 */
public class ChatMessageBean {

    private String customizeMessageType;//自定义消息类型
    private String userId;
    private String title;//标题
    private List<String> photoList;//图片数组的地址
    private String order;//订单
    private String price;//价格

    public String getOrder() {
        return order;
    }

    public ChatMessageBean setOrder(String order) {
        this.order = order;
        return this;
    }

    public String getPrice() {
        return price;
    }

    public ChatMessageBean setPrice(String price) {
        this.price = price;
        return this;
    }

    public ChatMessageBean(String customizeMessageType, String userId, String title, List<String> photoList) {
        this.customizeMessageType = customizeMessageType;
        this.userId = userId;
        this.title = title;
        this.photoList = photoList;
    }

    public String getCustomizeMessageType() {
        return customizeMessageType;
    }

    public void setCustomizeMessageType(String customizeMessageType) {
        this.customizeMessageType = customizeMessageType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<String> photoList) {
        this.photoList = photoList;
    }
}

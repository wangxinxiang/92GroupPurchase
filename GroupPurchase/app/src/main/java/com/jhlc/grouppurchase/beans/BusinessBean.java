package com.jhlc.grouppurchase.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/1/4.
 */
public class BusinessBean {

    /**
     * apprid :
     * code : 100
     * list : [{"commentList":[{"commentdate":"2016-01-13 04:56:37","commentid":1,"commentuserid":"000001","content":"1","iconurl":"","momentsid":1,"nick":"","sta":1}],"content":"无聊ing","createdate":"2016-01-09 03:33:13","iconurl":"","imagesList":[{"bigimageurl":"images1","imageid":1,"middleimageurl":"","momentsid":1,"orderindex":0,"smallimageurl":""}],"likeList":[{"iconurl":"","likedate":"2016-01-13 04:56:19","likeid":1,"likeuserid":"000001","momentsid":1,"nick":"","sta":1}],"momentsaddress":"浙江金华ing","momentsid":1,"nick":"","sta":1,"userid":"000001"}]
     * msg : 成功
     * opCode : getMyMomentsList
     * status : success
     */

    private String apprid;
    private int code;
    private String msg;
    private String opCode;
    private String status;
    /**
     * commentList : [{"commentdate":"2016-01-13 04:56:37","commentid":1,"commentuserid":"000001","content":"1","iconurl":"","momentsid":1,"nick":"","sta":1}]
     * content : 无聊ing
     * createdate : 2016-01-09 03:33:13
     * iconurl :
     * imagesList : [{"bigimageurl":"images1","imageid":1,"middleimageurl":"","momentsid":1,"orderindex":0,"smallimageurl":""}]
     * likeList : [{"iconurl":"","likedate":"2016-01-13 04:56:19","likeid":1,"likeuserid":"000001","momentsid":1,"nick":"","sta":1}]
     * momentsaddress : 浙江金华ing
     * momentsid : 1
     * nick :
     * sta : 1
     * userid : 000001
     */

    private List<ListEntity> list;

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

    public void setList(List<ListEntity> list) {
        this.list = list;
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

    public List<ListEntity> getList() {
        return list;
    }

    public static class ListEntity implements Serializable{
        private String content;
        private String createdate;
        private String iconurl;
        private String momentsaddress;
        private int momentsid;
        private String nick;
        private int sta;
        private String userid;
        /**
         * commentdate : 2016-01-13 04:56:37
         * commentid : 1
         * commentuserid : 000001
         * content : 1
         * iconurl :
         * momentsid : 1
         * nick :
         * sta : 1
         */

        private List<CommentListEntity> commentList;
        /**
         * bigimageurl : images1
         * imageid : 1
         * middleimageurl :
         * momentsid : 1
         * orderindex : 0
         * smallimageurl :
         */

        private List<ImagesListEntity> imagesList;
        /**
         * iconurl :
         * likedate : 2016-01-13 04:56:19
         * likeid : 1
         * likeuserid : 000001
         * momentsid : 1
         * nick :
         * sta : 1
         */

        private List<LikeListEntity> likeList;

        public void setContent(String content) {
            this.content = content;
        }

        public void setCreatedate(String createdate) {
            this.createdate = createdate;
        }

        public void setIconurl(String iconurl) {
            this.iconurl = iconurl;
        }

        public void setMomentsaddress(String momentsaddress) {
            this.momentsaddress = momentsaddress;
        }

        public void setMomentsid(int momentsid) {
            this.momentsid = momentsid;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public void setSta(int sta) {
            this.sta = sta;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public void setCommentList(List<CommentListEntity> commentList) {
            this.commentList = commentList;
        }

        public void setImagesList(List<ImagesListEntity> imagesList) {
            this.imagesList = imagesList;
        }

        public void setLikeList(List<LikeListEntity> likeList) {
            this.likeList = likeList;
        }

        public String getContent() {
            return content;
        }

        public String getCreatedate() {
            return createdate;
        }

        public String getIconurl() {
            return iconurl;
        }

        public String getMomentsaddress() {
            return momentsaddress;
        }

        public int getMomentsid() {
            return momentsid;
        }

        public String getNick() {
            return nick;
        }

        public int getSta() {
            return sta;
        }

        public String getUserid() {
            return userid;
        }

        public List<CommentListEntity> getCommentList() {
            return commentList;
        }

        public List<ImagesListEntity> getImagesList() {
            return imagesList;
        }

        public List<LikeListEntity> getLikeList() {
            return likeList;
        }

        public static class CommentListEntity implements Serializable {
            private String commentdate;
            private int commentid;
            private String commentuserid;
            private String content;
            private String iconurl;
            private int momentsid;
            private String nick;
            private int sta;

            public void setCommentdate(String commentdate) {
                this.commentdate = commentdate;
            }

            public void setCommentid(int commentid) {
                this.commentid = commentid;
            }

            public void setCommentuserid(String commentuserid) {
                this.commentuserid = commentuserid;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public void setIconurl(String iconurl) {
                this.iconurl = iconurl;
            }

            public void setMomentsid(int momentsid) {
                this.momentsid = momentsid;
            }

            public void setNick(String nick) {
                this.nick = nick;
            }

            public void setSta(int sta) {
                this.sta = sta;
            }

            public String getCommentdate() {
                return commentdate;
            }

            public int getCommentid() {
                return commentid;
            }

            public String getCommentuserid() {
                return commentuserid;
            }

            public String getContent() {
                return content;
            }

            public String getIconurl() {
                return iconurl;
            }

            public int getMomentsid() {
                return momentsid;
            }

            public String getNick() {
                return nick;
            }

            public int getSta() {
                return sta;
            }
        }

        public static class ImagesListEntity implements Serializable {
            private String bigimageurl;
            private int imageid;
            private String middleimageurl;
            private int momentsid;
            private int orderindex;
            private String smallimageurl;

            public void setBigimageurl(String bigimageurl) {
                this.bigimageurl = bigimageurl;
            }

            public void setImageid(int imageid) {
                this.imageid = imageid;
            }

            public void setMiddleimageurl(String middleimageurl) {
                this.middleimageurl = middleimageurl;
            }

            public void setMomentsid(int momentsid) {
                this.momentsid = momentsid;
            }

            public void setOrderindex(int orderindex) {
                this.orderindex = orderindex;
            }

            public void setSmallimageurl(String smallimageurl) {
                this.smallimageurl = smallimageurl;
            }

            public String getBigimageurl() {
                return bigimageurl;
            }

            public int getImageid() {
                return imageid;
            }

            public String getMiddleimageurl() {
                return middleimageurl;
            }

            public int getMomentsid() {
                return momentsid;
            }

            public int getOrderindex() {
                return orderindex;
            }

            public String getSmallimageurl() {
                return smallimageurl;
            }
        }

        public static class LikeListEntity implements Serializable {
            private String iconurl;
            private String likedate;
            private int likeid;
            private String likeuserid;
            private int momentsid;
            private String nick;
            private int sta;

            public void setIconurl(String iconurl) {
                this.iconurl = iconurl;
            }

            public void setLikedate(String likedate) {
                this.likedate = likedate;
            }

            public void setLikeid(int likeid) {
                this.likeid = likeid;
            }

            public void setLikeuserid(String likeuserid) {
                this.likeuserid = likeuserid;
            }

            public void setMomentsid(int momentsid) {
                this.momentsid = momentsid;
            }

            public void setNick(String nick) {
                this.nick = nick;
            }

            public void setSta(int sta) {
                this.sta = sta;
            }

            public String getIconurl() {
                return iconurl;
            }

            public String getLikedate() {
                return likedate;
            }

            public int getLikeid() {
                return likeid;
            }

            public String getLikeuserid() {
                return likeuserid;
            }

            public int getMomentsid() {
                return momentsid;
            }

            public String getNick() {
                return nick;
            }

            public int getSta() {
                return sta;
            }
        }
    }
}

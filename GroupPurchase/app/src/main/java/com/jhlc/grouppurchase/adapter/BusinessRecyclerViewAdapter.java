package com.jhlc.grouppurchase.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.utility.IMPrefsTools;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jhlc.grouppurchase.IM.LoginSampleHelper;
import com.jhlc.grouppurchase.R;
import com.jhlc.grouppurchase.activity.BusinessDetailActivity;
import com.jhlc.grouppurchase.activity.MyBusinessActivity;
import com.jhlc.grouppurchase.beans.BaseBean;
import com.jhlc.grouppurchase.beans.BusinessBean;
import com.jhlc.grouppurchase.httpUtils.FrescoBuilder;
import com.jhlc.grouppurchase.httpUtils.ImageLoadFresco;
import com.jhlc.grouppurchase.httpUtils.ObjectCallBack;
import com.jhlc.grouppurchase.util.ComponentSetValues;
import com.jhlc.grouppurchase.util.Constant;
import com.jhlc.grouppurchase.util.Logger;
import com.jhlc.grouppurchase.util.QGApplication;
import com.jhlc.grouppurchase.util.Utils;
import com.jhlc.grouppurchase.view.FlowLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/3.
 */
public class BusinessRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mInflater;
    private Context context;
    private OnItemClickListener mListener;
    protected List<BusinessBean.ListEntity> lists;

    public BusinessRecyclerViewAdapter(Context context, List<BusinessBean.ListEntity> lists) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.lists = lists;
    }

    /**
     * 添加头部识别
     *
     * @param position item 位置
     * @return 0位普通item  1位头部
     */
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = mInflater.inflate(R.layout.business_recyclerview_item, parent, false);
            return new MyViewHolder(view);
        } else {
            View view = mInflater.inflate(R.layout.business_header, parent, false);
            return new HeaderViewHolder(view);
        }

    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Logger.d("执行bindMyViewHolder");
        if (holder instanceof MyViewHolder) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            bindMyViewHolder(myViewHolder, position);
        } else {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            bindHeadViewHolder(headerViewHolder);
        }

    }

    /**
     * 绑定MyViewHolder
     */
    protected void bindMyViewHolder(MyViewHolder myViewHolder, final int position) {
        final BusinessBean.ListEntity businessBean = getPositionInfo(position);
        //设置界面
        FrescoBuilder.setHeadDrawableMC2V(context, myViewHolder.header, businessBean.getIconurl(), false);
        myViewHolder.name.setText(businessBean.getNick());
        myViewHolder.content.setText(businessBean.getContent());
        myViewHolder.time.setText(Utils.parseTime(businessBean.getCreatedate()));

        //动态添加
        final BusinessItemView businessItem = new BusinessItemView(context, businessBean);

        businessItem.addPhoto(myViewHolder.photo, true);     //添加图片


        boolean isNull = businessItem.addLove(myViewHolder.message, true);      //添加点赞

        businessItem.addComment(myViewHolder.message, isNull, true);       //添加评论

        //删除按钮设置
        businessItem.initDelete(myViewHolder.delete, myViewHolder.contact);

        //点赞点击处理
        myViewHolder.love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                businessItem.addMomentsLikeAndComment(0).build()
                        .execute(new ObjectCallBack<BaseBean>(BaseBean.class) {
                            @Override
                            public void onResponse(BaseBean response) {
                                QGApplication.getmApplication().showTextToast("点赞成功");
                                BusinessBean.ListEntity.LikeListEntity addLike = new BusinessBean.ListEntity.LikeListEntity();
                                addLike.setIconurl("http://www.diandidaxue.com:8080/images/beauty/20160117105854.jpg");
                                businessBean.getLikeList().add(addLike);
                                notifyItemChanged(position);
                            }
                        });
            }
        });

        //添加评论
        myViewHolder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentToDetail(businessBean, position);
            }
        });


        myViewHolder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentToDetail(businessBean, position);
            }
        });

    }

    /**
     * 绑定HeadViewHolder
     */
    protected void bindHeadViewHolder(HeaderViewHolder headerViewHolder) {
        headerViewHolder.bg.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);
        headerViewHolder.header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MyBusinessActivity.class);
                context.startActivity(intent);
            }
        });
    }

    /**
     * 根据position获取item信息
     */
    protected BusinessBean.ListEntity getPositionInfo(int position) {
        return lists.get(position - 1);
    }

    /**
     * 跳转到详情页
     *
     * @param businessBean 数据
     */
    private void intentToDetail(BusinessBean.ListEntity businessBean, int position) {
        Intent intent = new Intent(context, BusinessDetailActivity.class);
        intent.putExtra("businessBean", businessBean);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return lists.size() + 1;
    }

    public void setmListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }

    public interface OnItemClickListener {
        void onItemClickListener(View view, int position);
    }


    /**
     * 专为评论优化
     */
    class CommentViewHolder {
        //TODO 使用ViewHolder优化
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView header;           //头像
        TextView name, content, time, delete;   //名字，发布的内容，时间，删除按钮,联系卖家
        LinearLayout message, contact;    //发布的图片的展示地方，留言的展示地方
        ImageButton comment, love;      //评论按钮，点赞按钮
        FlowLayout photo;
        RelativeLayout item;        //整个item

        public MyViewHolder(View itemView) {
            super(itemView);
            header = (SimpleDraweeView) itemView.findViewById(R.id.iv_business_item_header);
            content = (TextView) itemView.findViewById(R.id.tv_business_item_content);
            time = (TextView) itemView.findViewById(R.id.tv_business_item_time);
            delete = (TextView) itemView.findViewById(R.id.tv_business_item_delete);
            name = (TextView) itemView.findViewById(R.id.tv_business_item_name);
            photo = (FlowLayout) itemView.findViewById(R.id.ll_business_item_photo);
            message = (LinearLayout) itemView.findViewById(R.id.ll_business_item_message);
            comment = (ImageButton) itemView.findViewById(R.id.ib_business_item_comment);
            love = (ImageButton) itemView.findViewById(R.id.ib_business_item_love);
            item = (RelativeLayout) itemView.findViewById(R.id.rl_business_item);
            contact = (LinearLayout) itemView.findViewById(R.id.tv_business_item_contact);
        }
    }

    /**
     * 添加头部所需
     */
    class HeaderViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView header, bg;
        TextView name;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            header = (SimpleDraweeView) itemView.findViewById(R.id.image_business_header_head);
            bg = (SimpleDraweeView) itemView.findViewById(R.id.image_business_header_bg);
            name = (TextView) itemView.findViewById(R.id.tv_business_header_name);
        }
    }

    /**
     * Item动态设置
     */
    public static class BusinessItemView {
        LayoutInflater mInflater;
        Context context;
        BusinessBean.ListEntity businessBean;
        String userId;

        public BusinessItemView(Context context, BusinessBean.ListEntity businessBean) {
            mInflater = LayoutInflater.from(context);
            this.context = context;
            this.businessBean = businessBean;
            userId = IMPrefsTools.getStringPrefs(context, Constant.USER_ID, "");
        }

        /**
         * 添加要展示的图片,不多于9个
         *
         * @param isLimit 是否限制显示数量
         */
        public void addPhoto(FlowLayout photo, boolean isLimit) {
            //先清除复用中的view
            photo.removeAllViews();
            List<BusinessBean.ListEntity.ImagesListEntity> photos = businessBean.getImagesList();

            //如果没有数据或则隐藏并退出
            if (photos == null || photos.size() == 0) {
                photo.setVisibility(View.GONE);
                return;
            }

            for (int i = 0; i < photos.size(); i++) {
                if (i > 6 && isLimit) break;           //不多于9个
                BusinessBean.ListEntity.ImagesListEntity images = photos.get(i);
                String url = images.getBigimageurl() + "@!setpic";
                SimpleDraweeView imageView = (SimpleDraweeView) mInflater.inflate(R.layout.business_item_image, photo, false);
                FrescoBuilder.setHeadDrawableMC2V(context, imageView, url, false);
                new ImageLoadFresco.LoadImageFrescoBuilder(context, imageView, url).setFailureImage(context.getResources().getDrawable(R.drawable.people_gray))
                        .build();
                photo.addView(imageView);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        QGApplication.getmApplication().showTextToast("点击了");
                    }
                });
            }
        }


        /**
         * 添加点赞
         *
         * @param isLimit 是否限制显示数量
         */
        public boolean addLove(LinearLayout message, boolean isLimit) {
            //先清除复用中的view
            message.removeAllViews();
            List<BusinessBean.ListEntity.LikeListEntity> loveBeans = businessBean.getLikeList();
            if (loveBeans == null || loveBeans.size() == 0) return true;          //如果没有点赞的人，就不用添加

            View view = mInflater.inflate(R.layout.business_item_love, message, true);

            ImageView kind = (ImageView) view.findViewById(R.id.iv_business_item_kind);
            kind.setImageDrawable(context.getResources().getDrawable(R.drawable.love));
            View line = view.findViewById(R.id.line);
            line.setVisibility(View.GONE);

            //添加点赞人
            final FlowLayout flowLayout = (FlowLayout) view.findViewById(R.id.fl_business_item_love);
            //先清除复用中的view
            flowLayout.setVisibility(View.VISIBLE);
            for (int i = 0; i < loveBeans.size(); i++) {
                if (i > 12 && isLimit) break;
                BusinessBean.ListEntity.LikeListEntity loveBean = loveBeans.get(i);
                String url = loveBean.getIconurl();
                FlowLayout view1 = (FlowLayout) mInflater.inflate(R.layout.business_item_image_min, flowLayout);
                SimpleDraweeView imageView = (SimpleDraweeView) view1.getChildAt(i);
                FrescoBuilder.setHeadDrawableMC2V(context, imageView, url + "@!setpic", false);
            }
            return false;
        }

//        public void addSingleLove(LinearLayout message, BusinessBean.ListEntity.LikeListEntity listEntity) {
//            FlowLayout flowLayout = (FlowLayout) message.findViewById(R.id.fl_business_item_love);
//            SimpleDraweeView simpleDraweeView = (SimpleDraweeView) mInflater.inflate(R.layout.business_item_image_min, flowLayout, false);
//            FrescoBuilder.setHeadDrawableMC2V(context, simpleDraweeView, listEntity.getIconurl(), false);
//            flowLayout.addView(simpleDraweeView);
//        }

        /**
         * 添加评论 最多5条
         *
         * @param isLimit 是否限制显示数量
         */
        public void addComment(LinearLayout message, boolean isLoveNull, boolean isLimit) {
            List<BusinessBean.ListEntity.CommentListEntity> commentBeans = businessBean.getCommentList();
            if (commentBeans == null || commentBeans.size() == 0) {
                if (isLoveNull) {
                    /**
                     * 如果没有点赞和评论就隐藏
                     */
                    message.setVisibility(View.GONE);
                }
                return;
            }

            for (int i = 0; i < commentBeans.size(); i++) {
                if (i > 5 && isLimit) break;
                BusinessBean.ListEntity.CommentListEntity commentBean = commentBeans.get(i);
                //在整个评论区添加一个评论
                LinearLayout linearLayout = (LinearLayout) mInflater.inflate(R.layout.business_item_love, message, true);
                //获取添加的单个评论所在的总LinearLayout
                LinearLayout ll_comment;
                /** 如果有点赞的人，评论所在父容器的index要+1 */
                if (isLoveNull) {
                    ll_comment = (LinearLayout) linearLayout.getChildAt(i);
                    //如果没有人点赞，但又有评论，第一条评论不用添加分割线
                    View line = ll_comment.findViewById(R.id.line);
                    if (i == 0) {
                        line.setVisibility(View.GONE);
                    } else {
                        line.setVisibility(View.VISIBLE);
                    }
                } else {
                    ll_comment = (LinearLayout) linearLayout.getChildAt(i + 1);
                }
                //获取储存评论的LinearLayout
                LinearLayout ll_comment_content = (LinearLayout) ll_comment.getChildAt(1);
                //将评论放入容器中
                View view = mInflater.inflate(R.layout.business_item_comment, ll_comment_content, true);

                SimpleDraweeView simpleDraweeView = (SimpleDraweeView) view.findViewById(R.id.iv_business_item_comment_header);
                FrescoBuilder.setHeadDrawableMC2V(context, simpleDraweeView, commentBean.getIconurl(), false);

                ComponentSetValues.tvSetText(ll_comment_content, R.id.tv_business_item_comment_name, commentBean.getNick());
                ComponentSetValues.tvSetText(ll_comment_content, R.id.tv_business_item_comment_content, commentBean.getContent());
                ComponentSetValues.tvSetText(ll_comment_content, R.id.tv_business_item_comment_time, Utils.parseTime(commentBean.getCommentdate()));

            }
        }

        /**
         * 添加评论和添加点赞的共同操作
         *
         * @param style 0表示添加点赞  1表示添加评论
         * @return PostFormBuilder构造器
         */
        public PostFormBuilder addMomentsLikeAndComment(int style) {
            String localId = IMPrefsTools.getStringPrefs(context, Constant.USER_ID, "");
            String time = Utils.getSimpleDate();
            String opcode;
            if (style == 0) {
                opcode = context.getString(R.string.url_addMomentsLike);
            } else {
                opcode = context.getString(R.string.url_addMomentsComment);
            }
            String checkDate = Utils.getcheckMD5(opcode, String.valueOf(businessBean.getUserid()), time);
            return OkHttpUtils.post().url(context.getString(R.string.url)).addParams("opcode", opcode)
                    .addParams("likeuserid", localId)
                    .addParams("momentsid", String.valueOf(businessBean.getMomentsid()))
                    .addParams("submitDate", time).addParams("checkDate", checkDate);
        }

        /**
         * 设置删除按钮和联系卖家按钮
         *
         * @param delete
         */
        public void initDelete(View delete, View contact) {
            if (userId.equals(businessBean.getUserid())) {
                contact.setVisibility(View.GONE);
                delete.setVisibility(View.VISIBLE);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            } else {
                delete.setVisibility(View.GONE);
                contact.setVisibility(View.VISIBLE);
                contact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        contactSeller();
                    }
                });
            }
        }

        /**
         * 联系卖家
         */
        public void contactSeller() {
            YWIMKit imKit = LoginSampleHelper.getInstance().getIMKit();
            String target = businessBean.getUserid();
            Intent intent = imKit.getChattingActivityIntent(target);
            context.startActivity(intent);
        }
    }
}

package com.jhlc.grouppurchase.IM.UI;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.IMChattingPageOperateion;
import com.alibaba.mobileim.aop.model.ReplyBarItem;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWConversationType;
import com.alibaba.mobileim.conversation.YWMessage;
import com.alibaba.mobileim.conversation.YWMessageBody;
import com.alibaba.mobileim.conversation.YWMessageChannel;
import com.alibaba.mobileim.utility.IMPrefsTools;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.jhlc.grouppurchase.R;
import com.jhlc.grouppurchase.activity.GoodsPostActivity;
import com.jhlc.grouppurchase.beans.ChatMessageBean;
import com.jhlc.grouppurchase.beans.OrderBean;
import com.jhlc.grouppurchase.activity.GoodsDetailsActivity;
import com.jhlc.grouppurchase.httpUtils.ImageLoadFresco;
import com.jhlc.grouppurchase.interfaces.intentClickListener;
import com.jhlc.grouppurchase.util.Constant;
import com.jhlc.grouppurchase.util.Logger;
import com.jhlc.grouppurchase.util.Notification;
import com.jhlc.grouppurchase.util.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiCola on  2015/12/29  16:43
 * <p/>
 * 聊天界面(单聊和群聊界面)的定制点(根据需要实现相应的接口来达到自定义聊天界面)，不设置则使用openIM默认的实现
 */
public class ChattingOperationCustom extends IMChattingPageOperateion {
    private static final String TAG = "ChattingOperationCustom";
    private static final String USER_ID = "mUserId";
    private static final long MESSAGE_ID = 100;


    // 默认写法
    public ChattingOperationCustom(Pointcut pointcut) {
        super(pointcut);
    }

    /**
     * 单聊ui界面，点击url的事件拦截 返回true;表示自定义处理，返回false，由默认处理
     *
     * @param fragment 可以通过 fragment.getActivity拿到Context
     * @param message  点击的url所属的message
     * @param url      点击的url
     */
    @Override
    public boolean onUrlClick(Fragment fragment, YWMessage message, String url,
                              YWConversation conversation) {
        // 仅处理单聊
        if (!isP2PChat(conversation) && !isShopChat(conversation)) {
            return false;
        }

        Toast.makeText(fragment.getActivity(), "用户点击了url:" + url,
                Toast.LENGTH_LONG).show();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        fragment.startActivity(intent);

        return false;
    }

    private boolean isP2PChat(YWConversation conversation) {
        return conversation.getConversationType() == YWConversationType.P2P;
    }

    private boolean isShopChat(YWConversation conversation) {
        return conversation.getConversationType() == YWConversationType.SHOP;
    }


    //定制下方回复栏的操作区 的添加、点击操作

    private static int ITEM_ID_1 = 0x1;
    private static int ITEM_ID_2 = 0x2;

    /**
     * 用于增加聊天窗口 下方回复栏的操作区的item ReplyBarItem itemId:唯一标识 建议从1开始
     * ItemImageRes：显示的图片 ItemLabel：文字 label YWConversation
     * conversation：当前会话，通过conversation.getConversationType() 区分个人单聊，与群聊天
     */
    @Override
    public List<ReplyBarItem> getReplybarItems(Fragment pointcut,
                                               YWConversation conversation) {
        List<ReplyBarItem> replyBarItems = new ArrayList<ReplyBarItem>();
        if (conversation.getConversationType() == YWConversationType.P2P) {
            //单聊
            ReplyBarItem replyBarSell = new ReplyBarItem();
            replyBarSell.setItemId(ITEM_ID_1);
            replyBarSell.setItemImageRes(R.drawable.square_goods);
            replyBarSell.setItemLabel("发商品");

            ReplyBarItem replyBarIndent = new ReplyBarItem();
            replyBarIndent.setItemId(ITEM_ID_2);
            replyBarIndent.setItemImageRes(R.drawable.square_sell);
            replyBarIndent.setItemLabel("下订单");

            replyBarItems.add(replyBarSell);
            replyBarItems.add(replyBarIndent);

        } else if (conversation.getConversationType() == YWConversationType.Tribe) {
            //群聊
            ReplyBarItem replyBarSell = new ReplyBarItem();
            replyBarSell.setItemId(ITEM_ID_1);
            replyBarSell.setItemImageRes(R.drawable.square_goods);
            replyBarSell.setItemLabel("发商品");

            replyBarItems.add(replyBarSell);

        }
        return replyBarItems;

    }

    private static YWConversation mConversation;

    /**
     * 当自定义的item点击时的回调
     */
    @Override
    public void onReplyBarItemClick(Fragment pointcut, ReplyBarItem item,
                                    YWConversation conversation) {
        Logger.d("onReplyBarItemClick" + item.getItemLabel());
        String userId= PreferencesUtils.getStringPrefs(pointcut.getContext(),Constant.USER_ID);
        mConversation = conversation;
        if (conversation.getConversationType() == YWConversationType.P2P) {
            //单聊

            if (item.getItemId() == ITEM_ID_1) {
                startPosGoodActivity(pointcut, Constant.TYPE_GREETING);//跳转到发送商品
            } else if (item.getItemId() == ITEM_ID_2) {
                startPostOrderActivity(pointcut, Constant.TYPE_ORDER);
                List<String> photoList = new ArrayList<>();
//                //手动添加图片地址
                photoList.add("http://wxq.image.alimmdn.com/TestDir/20160120032907?t=1453274952582");
                photoList.add("http://wxq.image.alimmdn.com/TestDir/20160120032716?t=1453274862983");
                photoList.add("http://wxq.image.alimmdn.com/TestDir/20160118124648?t=1453092432979");
                mConversation
                        .getMessageSender()
                        .sendMessage(createP2POrderMessage(Constant.TYPE_ORDER, userId, userId, photoList, "00000001", "10"), MESSAGE_ID, null);
            }
        } else if (conversation.getConversationType() == YWConversationType.Tribe) {
            //群聊
            if (item.getItemId() == ITEM_ID_1) {
                startPosGoodActivity(pointcut, Constant.TYPE_GREETING);//跳转到发送商品

                //代码调试 发送自定义消息
//                List<String> photoList = new ArrayList<>();
//                //手动添加图片地址
//                photoList.add("http://wxq.image.alimmdn.com/TestDir/20160120032907?t=1453274952582");
//                photoList.add("http://wxq.image.alimmdn.com/TestDir/20160120032716?t=1453274862983");
//                photoList.add("http://wxq.image.alimmdn.com/TestDir/20160118124648?t=1453092432979");
//                sendGoodsMessage(conversation, TYPE_GREETING, mUserId, "自定义的消息title", photoList);//发送商品信息
            }
        }
    }

    private void startPostOrderActivity(Fragment pointcut, String typeOrder) {
        //启动 发送订单的activity

    }

    private void startPosGoodActivity(Fragment pointcut, String type_post) {
        //启动 发送商品的activity
        Intent intent = new Intent(pointcut.getActivity(), GoodsPostActivity.class);
        intent.putExtra(GoodsPostActivity.MSG, type_post);
        pointcut.getActivity().startActivity(intent);
    }

    /**
     * @param userId
     * @param title
     * @param photoList
     * @return 返回一条封装的 群消息
     */
    public static YWMessage createTribeGoodsMessage(String type, String userId, String title, List<String> photoList) {
        YWMessageBody messageBody = new YWMessageBody();//new 出消息对象 开始封装内容
        ChatMessageBean bean = new ChatMessageBean(type, userId, title, photoList);
        Gson gson = new Gson();
        messageBody.setContent(gson.toJson(bean));
        messageBody.setSummary("发送了一条商品消息");
        //YWMessageChannel 云旺的消息创建工厂，可以创建适用于云旺聊天的消息对象
        // createTribeCustomMessage 创建自定义消息协议的群消息
        return YWMessageChannel.createTribeCustomMessage(messageBody);
    }

    /**
     * @param userId
     * @param title
     * @param photoList
     * @return 返回一条封装的 群消息
     */
    public static YWMessage createP2PGoodsMessage(String type, String userId, String title, List<String> photoList) {
        YWMessageBody messageBody = new YWMessageBody();//new 出消息对象 开始封装内容
        ChatMessageBean bean = new ChatMessageBean(type, userId, title, photoList);
        Gson gson = new Gson();
        messageBody.setContent(gson.toJson(bean));
        messageBody.setSummary("发送了一条商品消息");
        //YWMessageChannel 云旺的消息创建工厂，可以创建适用于云旺聊天的消息对象
        // createTribeCustomMessage 创建自定义消息协议的单聊消息
        return YWMessageChannel.createCustomMessage(messageBody);
    }

    /**
     * @param type
     * @param userId
     * @param title
     * @param photoList
     * @param order     订单号
     * @param price     价格
     * @return 返回一条封装的 单聊 订单消息
     */
    public static YWMessage createP2POrderMessage(String type, String userId, String title, List<String> photoList, String order, String price) {
        YWMessageBody messageBody = new YWMessageBody();//new 出消息对象 开始封装内容
        OrderBean bean = new OrderBean(type, userId, title, photoList, order, price);
        Gson gson = new Gson();
        messageBody.setContent(gson.toJson(bean));
        messageBody.setSummary("发送了一条订单消息");
        //YWMessageChannel 云旺的消息创建工厂，可以创建适用于云旺聊天的消息对象
        //createCustomMessage 创建自定义消息协议的单聊消息
        return YWMessageChannel.createCustomMessage(messageBody);
    }


    private String getUserID(Context context) {
        return IMPrefsTools.getStringPrefs(context, USER_ID, "");
    }

    /**
     * 创建自定消息的视图
     *
     * @param fragment
     * @param message  消息体
     * @return 自定消息的显示View
     */
    @Override
    public View getCustomMessageView(Fragment fragment, YWMessage message) {
        Logger.d("YWMessage= " + message.getSubType() + " " + message.getConversationId() + " " + message.getMsgId());
        Context context=fragment.getContext();
        View view = null;
        Gson gson = new Gson();
        ChatMessageBean bean = gson.fromJson(message.getMessageBody().getContent(), ChatMessageBean.class);
        if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_TRIBE_CUS) {
            //消息类型为 群自定义消息
            Logger.d("群消息");
            view = getGoodsMessageView(context, bean);
        }

        if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_P2P_CUS) {
            Logger.d("单聊消息");
            if (Constant.TYPE_GREETING.equals(bean.getCustomizeMessageType())) {
                view = getGoodsMessageView(context, bean);
            } else if (Constant.TYPE_ORDER.equals(bean.getCustomizeMessageType())) {
                view=getOrderMessageView(context,bean);
            }
        }

        return view;
    }

    @NonNull
    private View getGoodsMessageView(Context context, ChatMessageBean bean) {

        View view = View.inflate(context, R.layout.item_custom_goods_message, null);
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) view.findViewById(R.id.img_item_image);
        TextView tv_item_title = (TextView) view.findViewById(R.id.tv_item_title);

        Drawable dProgressImage = DrawableCompat.wrap(ContextCompat.getDrawable(context, R.drawable.ic_loop_white_24dp).mutate());
        DrawableCompat.setTint(dProgressImage, ContextCompat.getColor(context, R.color.tint_grey));

        String exceptionUrl = "http://img.hb.aicdn.com/6e988592b2a5279b09bf7b10416ac8070d6b86e3646a-udu9rg";
        try {
            tv_item_title.setText(bean.getTitle());

            String url_style = bean.getPhotoList().get(0) + "@!setpic";
//            FrescoBuilder.setHeadDrawableMC2V(context,simpleDraweeView,bean.getPhotoList().get(0),false);
            new ImageLoadFresco.LoadImageFrescoBuilder(context, simpleDraweeView, url_style)
                    .setProgressBarImage(dProgressImage)
                    .setIsRadius(true)
                    .build();
        } catch (Exception e) {
            Logger.e(e);
            tv_item_title.setText("手动设值");
//            FrescoBuilder.setHeadDrawableMC2V(context,simpleDraweeView,"http://img.hb.aicdn.com/6e988592b2a5279b09bf7b10416ac8070d6b86e3646a-udu9rg",false);
            new ImageLoadFresco.LoadImageFrescoBuilder(context, simpleDraweeView, exceptionUrl)
                    .setIsRadius(true)
                    .build();
        }
        return view;
    }

    @NonNull
    private View getOrderMessageView(final Context context, ChatMessageBean bean) {

        View view = View.inflate(context, R.layout.item_custom_order_message, null);
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) view.findViewById(R.id.img_item_image);
        TextView tv_item_title = (TextView) view.findViewById(R.id.tv_item_title);
        TextView tv_item_price= (TextView) view.findViewById(R.id.tv_item_price);

        Button btn_item_pay= (Button) view.findViewById(R.id.btn_item_pay);
        btn_item_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.d();
                Notification.showToastMsg(context,"点击了付款");
            }
        });

        Drawable dProgressImage = DrawableCompat.wrap(ContextCompat.getDrawable(context, R.drawable.ic_loop_white_24dp).mutate());
        DrawableCompat.setTint(dProgressImage, ContextCompat.getColor(context, R.color.tint_grey));

        String exceptionUrl = "http://img.hb.aicdn.com/6e988592b2a5279b09bf7b10416ac8070d6b86e3646a-udu9rg";
        try {
            tv_item_title.setText(bean.getTitle());
            tv_item_price.setText(bean.getPrice());
            String url_style = bean.getPhotoList().get(0) + "@!setpic";
//            FrescoBuilder.setHeadDrawableMC2V(context,simpleDraweeView,bean.getPhotoList().get(0),false);
            new ImageLoadFresco.LoadImageFrescoBuilder(context, simpleDraweeView, url_style)
                    .setProgressBarImage(dProgressImage)
                    .setIsRadius(true)
                    .build();
        } catch (Exception e) {
            Logger.e(e);
            tv_item_title.setText("手动设值");
//            FrescoBuilder.setHeadDrawableMC2V(context,simpleDraweeView,"http://img.hb.aicdn.com/6e988592b2a5279b09bf7b10416ac8070d6b86e3646a-udu9rg",false);
            new ImageLoadFresco.LoadImageFrescoBuilder(context, simpleDraweeView, exceptionUrl)
                    .setIsRadius(true)
                    .build();
        }
        return view;
    }

    //定制下方回复栏的操作结束

    //定制消息的点击事件 --->

    @Override
    public void onCustomMessageClick(Fragment fragment, YWMessage message) {
        Intent intent = new Intent(fragment.getActivity(), GoodsDetailsActivity.class);
        intent.putExtra(GoodsDetailsActivity.MESSAGEBODY, message.getMessageBody().getContent());
        fragment.getActivity().startActivity(intent);
        Logger.d();
    }

    @Override
    public void onCustomMessageLongClick(Fragment fragment, YWMessage message) {
        Notification.showToastMsg(fragment.getContext(), "你长按了自定义消息");
        Logger.d();
    }


    //定制消息的点击事件 结束


    //聊天跳转需要的接口类


    public static intentClickListener clickListener = new intentClickListener() {
        @Override
        public void onClickContact(String userId) {
            //点击 联系卖家 回调
            Logger.d("联系卖家 回调 onClickContact userID=" + userId);
        }

        @Override
        public void onClickBusiness(String userId) {
            //点击 卖家商圈 回调
            Logger.d("卖家商圈 回调 onClickBusiness userID=" + userId);
        }

        @Override
        public void onPostGoodComplete(String type, String userId, String userInput, List<String> urlList) {

            if (mConversation.getConversationType() == YWConversationType.P2P) {
                mConversation
                        .getMessageSender()
                        .sendMessage(createP2PGoodsMessage(type, userId, userInput, urlList), MESSAGE_ID, null);
            }

            if (mConversation.getConversationType() == YWConversationType.Tribe) {
                mConversation
                        .getMessageSender()
                        .sendMessage(createTribeGoodsMessage(type, userId, userInput, urlList), MESSAGE_ID, null);
            }
        }

        @Override
        public void onPostOrderComplete(String type, String userId, String userInput, List<String> urlList, String order, String price) {
            mConversation
                    .getMessageSender()
                    .sendMessage(createP2POrderMessage(type, userId, userInput, urlList, order, price), MESSAGE_ID, null);
        }


    };
    //接口类 结束

}

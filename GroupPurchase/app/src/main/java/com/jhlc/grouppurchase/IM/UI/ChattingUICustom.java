package com.jhlc.grouppurchase.IM.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.IMChattingPageUI;
import com.alibaba.mobileim.channel.constant.YWProfileSettingsConstants;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWConversationType;
import com.alibaba.mobileim.conversation.YWMessage;
import com.alibaba.mobileim.conversation.YWP2PConversationBody;
import com.alibaba.mobileim.conversation.YWTribeConversationBody;
import com.alibaba.mobileim.fundamental.widget.WxAlertDialog;
import com.alibaba.mobileim.gingko.model.tribe.YWTribe;
import com.alibaba.mobileim.kit.common.IMUtility;
import com.jhlc.grouppurchase.IM.LoginSampleHelper;
import com.jhlc.grouppurchase.R;
import com.jhlc.grouppurchase.util.Logger;

/**
 * Created by LiCola on  2015/12/29  14:04
 */
public class ChattingUICustom extends IMChattingPageUI {
    private static final String TAG = "ChattingUICustom";

    public ChattingUICustom(Pointcut pointcut) {
        super(pointcut);
    }

    @Override
    public int getChattingBackgroundResId() {
        //聊天窗口背景，默认不显示
        return 0;
//         return R.drawable.aliwx_comment_l_bg;
    }


//    对聊天消息背景的定制--->

    /**
     * 设置左边图片消息汽泡背景图，需要.9图
     *
     * @return 0: 默认背景
     * <br>
     * －1:透明背景（无背景）
     * <br>
     * resId：使用resId对应图片做背景
     */
    @Override
    public int getLeftImageMsgBackgroundResId() {
        return R.drawable.aliwx_comment_l_bg;
//        return 0;
//		return -1;//-1是透明
    }


    @Override
    public int getLeftTextMsgBackgroundResId() {
        return R.drawable.aliwx_comment_l_bg;
//        return 0;
    }

    //左边的自定义位置消息显示背景 Geo=geographic 地理的
//    @Override
//    public int getLeftGeoMsgBackgroundResId(YWConversation conversation) {
//        return R.drawable.aliwx_comment_l_bg;
//    }

    @Override
    public int getLeftCustomMsgBackgroundResId(YWConversation conversation) {
        return R.drawable.aliwx_comment_l_bg;
//        return -1;//-1是透明
    }

    /**
     * 设置右边图片消息汽泡背景图，需要.9图
     *
     * @return 0: 默认背景
     * <br>
     * －1:透明背景（无背景）
     * <br>
     * resId：使用resId对应图片做背景
     */
    @Override
    public int getRightImageMsgBackgroundResId() {
        return R.drawable.aliwx_comment_r_bg;
//        return 0;
//		return -1;
    }

    @Override
    public int getRightTextMsgBackgroundResId() {
        return R.drawable.aliwx_comment_r_bg;
//        return 0;
    }

//    @Override
//    public int getRightGeoMsgBackgroundResId(YWConversation conversation) {
//        return R.drawable.aliwx_comment_r_bg;
//    }

    @Override
    public int getRightCustomMsgBackgroundResId(YWConversation conversation) {
        return R.drawable.aliwx_comment_r_bg;
//        return -1;//-1是透明
    }
    //   对聊天消息背景的定制结束


    //对聊天图片的处理 主要是圆角---->

    /**
     * 建议使用{@link #processBitmapOfLeftImageMsg｝和{@link #processBitmapOfRightImageMsg｝
     * 灵活修改Bitmap，达到对图像进行［圆角处理］,［裁减］等目的,这里建议return false
     * 设置是否需要将聊天界面的图片设置为圆角
     *
     * @return false: 不做圆角处理
     * <br>
     * true：做圆角处理（重要：返回true时不会做{@link #processBitmapOfLeftImageMsg｝
     * 和{@link #processBitmapOfRightImageMsg｝二次图片处理，两者互斥！）
     */

    @Override
    public boolean needRoundChattingImage() {
        return true;
    }

    /**
     * 设置聊天界面图片圆角的边角半径的长度(单位：dp)
     *
     * @return
     */
    @Override
    public float getRoundRadiusDps() {
        return 12.6f;
    }

    /**
     *
     * 用于更灵活地加工［左边图片消息］的Bitmap用于显示，SDK内部会缓存之，后续直接使用缓存的Bitmap显示。例如：对图像进行［裁减］，［圆角处理］等等
     * 重要：使用该方法时：
     * 1.请将 {@link #needRoundChattingImage}设为return false(不裁剪圆角)，两者是互斥关系
     * 2.建议将{@link #getLeftImageMsgBackgroundResId}设为return－1（背景透明）
     * @param input 网络获取的聊天图片
     * @return 供显示的Bitmap
     */
//    public Bitmap processBitmapOfLeftImageMsg(Bitmap input) {
//        Bitmap output = Bitmap.createBitmap(input.getWidth(),
//                input.getHeight(), Bitmap.Config.ARGB_8888);
//        //为提高性能，对取得的resource图片做缓存
//        Bitmap distBitmap = YWIMImageUtils.getFromCacheOrDecodeResource(R.drawable.left_bubble);
//        NinePatch np = new NinePatch(distBitmap, distBitmap.getNinePatchChunk(), null);
//        Canvas canvas = new Canvas(output);
//        final Paint paint = new Paint();
//        final Rect rectSrc = new Rect(0, 0, input.getWidth(), input.getHeight());
//        final RectF rectDist = new RectF(0, 0, input.getWidth(), input.getHeight());
//        np.draw(canvas, rectDist);
//        canvas.drawARGB(0, 0, 0, 0);
//        //设置Xfermode
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
//        canvas.drawBitmap(input, rectSrc, rectSrc, paint);
//        return output;
//    }

    /**
     *  用于更灵活地加工［右边图片消息］的Bitmap用于显示，SDK内部会缓存之，后续直接使用缓存的Bitmap显示。例如：对图像进行［裁减］，［圆角处理］等等
     * 重要：使用该方法时：
     * 1.请将 {@link #needRoundChattingImage}设为return false(不裁剪圆角)，两者是互斥关系
     * 2.建议将{@link #getRightImageMsgBackgroundResId}设为return－1（背景透明）
     * @param input 网络获取的聊天图片
     * @return 供显示的Bitmap
     */
//    public  Bitmap processBitmapOfRightImageMsg(Bitmap input) {
//        Bitmap output = Bitmap.createBitmap(input.getWidth(),
//                input.getHeight(), Bitmap.Config.ARGB_8888);
//        //为提高性能，对取得的resource图片做缓存
//        Bitmap distBitmap = YWIMImageUtils.getFromCacheOrDecodeResource(R.drawable.right_bubble);
//        NinePatch np = new NinePatch(distBitmap, distBitmap.getNinePatchChunk(), null);
//        Canvas canvas = new Canvas(output);
//        final Paint paint = new Paint();
//        final Rect rectSrc = new Rect(0, 0, input.getWidth(), input.getHeight());
//        final RectF rectDist = new RectF(0, 0, input.getWidth(), input.getHeight());
//        np.draw(canvas, rectDist);
//        canvas.drawARGB(0, 0, 0, 0);
//        //设置Xfermode
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
//        canvas.drawBitmap(input, rectSrc, rectSrc, paint);
//        return output;
//    }

    //对聊天图片的处理 结束


    //对标题栏的定制---->

    /**
     * 是否隐藏标题栏
     *
     * @param fragment
     * @param conversation
     * @return
     */
    @Override
    public boolean needHideTitleView(Fragment fragment, YWConversation conversation) {
//        if (conversation.getConversationType() == YWConversationType.Tribe){
//            return true;
//        }
        //@消息功能需要展示标题栏，暂不隐藏
        return false;
    }

    /**
     * 创建 聊天界面的title
     * @param fragment
     * @param context
     * @param inflater
     * @param conversation
     * @return view 作为title
     */
    @Override
    public View getCustomTitleView(final Fragment fragment, Context context, LayoutInflater inflater, YWConversation conversation) {
        // 单聊和群聊都会使用这个方法，所以这里需要做一下区分

        //TODO 重要：必须以该形式初始化view---［inflate(R.layout.**, new RelativeLayout(context),false)］------，以让inflater知道父布局的类型，否则布局**中的高度和宽度无效，均变为wrap_content
        View view = inflater.inflate(R.layout.tribe_title_custom, new RelativeLayout(context), false);
        ImageButton ibtn_chat_info = (ImageButton) view.findViewById(R.id.ibtn_chat_info);
        ImageButton ibtn_chat_collection = (ImageButton) view.findViewById(R.id.ibtn_chat_collection);
        TextView tv_chat_title = (TextView) view.findViewById(R.id.tv_chat_title);
        Drawable drawable_info = null;
        String title = null;
        if (conversation.getConversationType() == YWConversationType.P2P) {
            //对个人的聊天
            drawable_info = context.getResources().getDrawable(R.drawable.ibtn_people_selector);//设置右上角的图片

            //获取title
            YWP2PConversationBody conversationBody = (YWP2PConversationBody) conversation
                    .getConversationBody();//获取会话详情信息
            if (!TextUtils.isEmpty(conversationBody.getContact().getShowName())) {
                title = conversationBody.getContact().getShowName();
            } else {
                IYWContact contact = IMUtility.getContactProfileInfo(conversationBody.getContact().getUserId(), conversationBody.getContact().getAppKey());
                //生成showName，According to id。

                if (contact != null && !TextUtils.isEmpty(contact.getShowName())) {
                    title = contact.getShowName();
                }
            }
            //如果标题为空，那么直接使用Id
            if (TextUtils.isEmpty(title)) {
                title = conversationBody.getContact().getUserId();
            }
        } else {
            //对群的聊天
            drawable_info = context.getResources().getDrawable(R.drawable.ibtn_tribe_selector);
            if (conversation.getConversationBody() instanceof YWTribeConversationBody) {
                title = ((YWTribeConversationBody) conversation.getConversationBody()).getTribe().getTribeName();
                if (TextUtils.isEmpty(title)) {
                    title = "群title为空";
                }
            }

        }

        tv_chat_title.setText(title);
        ibtn_chat_info.setImageDrawable(drawable_info);


        view.findViewById(R.id.ibtn_chat_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回的监听
                Logger.d();
                fragment.getActivity().finish();
            }
        });

        ibtn_chat_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //收藏的监听
                Logger.d();

            }
        });
        ibtn_chat_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //群信息的监听
                Logger.d();
            }
        });


        return view;//返回title的视图view

    }

    //对标题栏的定制 结束


    //定制聊天图片的操作--->

    /**
     * 定制图片预览页面titlebar右侧按钮点击事件
     *
     * @param fragment
     * @param message  当前显示的图片对应的ywmessage对象
     * @return true：使用用户定制的点击事件， false：使用默认的点击事件(默认点击事件为保持该图片到本地)
     */
    @Override
    public boolean onImagePreviewTitleButtonClick(Fragment fragment, YWMessage message) {
        Context context = fragment.getActivity();
        Toast.makeText(context, "你点击了该按钮~", Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * 返回图片保存的目录
     *
     * @param fragment
     * @param message
     * @return 如果为null, 使用SDK默认的目录, 否则使用用户设置的目录
     */
    @Override
    public String getImageSavePath(Fragment fragment, YWMessage message) {
//        return Environment
//                .getExternalStorageDirectory().getAbsolutePath()
//                + "/alibaba/WXOPENI/云旺相册";
        return null;
    }

    //定制聊天图片的操作 结束

    //定制聊天的头像 主要是显示头像 和圆形和圆角矩形

    /**
     * 返回单聊默认头像资源Id
     *
     * @return 0:使用SDK默认提供的
     */
    @Override
    public int getDefaultHeadImageResId() {
        return 0;
    }

    /**
     * 是否需要圆角矩形的头像
     *
     * @return true:需要圆角矩形
     * <br>
     * false:不需要圆角矩形，默认为圆形
     * <br>
     * 注：如果返回true，则需要使用{@link #getRoundRectRadius()}给出圆角的设置半径，否则无圆角效果
     */
    @Override
    public boolean isNeedRoundRectHead() {
        return false;
    }

    /**
     * 返回设置圆角矩形的圆角半径大小
     *
     * @return 0:如果{@link #isNeedRoundRectHead()}返回true，此处返回0则表示头像显示为直角正方形
     */
    @Override
    public int getRoundRectRadius() {
        return 0;
    }

    //定制聊天的头像 结束

    //定制群的操作提示 提示在聊天界面出现

    @Override
    public View getChattingFragmentCustomViewAdvice(Fragment fragment, Intent intent) {

        if (intent != null && intent.hasExtra("extraTribeId") && intent.hasExtra("conversationType")) {
            final long tribeId = intent.getLongExtra("extraTribeId", 0);
            int conversationType = intent.getIntExtra("conversationType", -1);
            if (tribeId > 0 && conversationType == YWConversationType.Tribe.getValue()) {

                final YWTribe tribe = LoginSampleHelper.getInstance().getIMKit().getIMCore().getTribeService().getTribe(tribeId);

                if (tribe != null && tribe.getMsgRecType() == YWProfileSettingsConstants.TRIBE_MSG_REJ) { //群在屏蔽的时候才显示。
                    final Activity context = fragment.getActivity();
                    final TextView view = new TextView(context);
                    ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            (int) context.getResources().getDimension(R.dimen.hint_text_view_height));
                    lp.setMargins(0, (int) context.getResources().getDimension(R.dimen.title_bar_height), 0, 0);
                    view.setLayoutParams(lp);
                    view.setGravity(Gravity.CENTER);
                    view.setBackgroundResource(R.color.third_text_color);
                    view.setText("你屏蔽了本群的消息，点击接收群消息");
                    view.setTextColor(context.getResources().getColor(R.color.aliwx_common_bg_white_color));
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final AlertDialog alertDialog = new WxAlertDialog.Builder(context)
                                    .setTitle("提示")
                                    .setMessage("接收群消息可能会产生较大数据流量，您确定接收吗？")
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(final DialogInterface dialog, int which) {
                                            receiveTribeMsg(tribe, view);
                                            dialog.dismiss();
                                        }
                                    }).create();
                            alertDialog.show();
                        }
                    });

                    return view;
                }
            }
        }
        return null;
    }

    private void receiveTribeMsg(YWTribe tribe, final View view) {
        //unblockTribe 重新接收消息 的接口
        LoginSampleHelper.getInstance().getIMKit().getIMCore().getTribeService().unblockTribe(tribe, new IWxCallback() {
            @Override
            public void onSuccess(Object... result) {
                new Handler(Looper.getMainLooper()).post(new Runnable(){
                    @Override
                    public void run() {
                        view.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onError(int code, String info) {

            }

            @Override
            public void onProgress(int progress) {

            }
        });
    }
    //定制群的操作提示 结束

    //聊天界面的消息item 的View做最后调整---->
    /**
     * getView方法内，返回View之前，对［聊天界面的右边消息item的View］做最后调整,如调整View的Padding。
     *
     * @param msg
     * @param rightItemParentView
     * @param fragment
     * @param conversation
     */
    @Override
    public void modifyRightItemParentViewAfterSetValue(YWMessage msg, RelativeLayout rightItemParentView, Fragment fragment, YWConversation conversation) {
//        if(msg!=null&&rightItemParentView!=null&&msg.getSubType()==YWMessage.SUB_MSG_TYPE.IM_IMAGE||msg.getSubType()==YWMessage.SUB_MSG_TYPE.IM_GIF){
//            rightItemParentView.setPadding(rightItemParentView.getPaddingLeft(), rightItemParentView.getPaddingTop(), 0, rightItemParentView.getPaddingBottom());
//        }
    }

    /**
     * getView方法内，返回View之前，对［聊天界面的左边消息item的View］做最后调整,如调整View的Padding。
     *
     * @param msg
     * @param leftItemParentView
     * @param fragment
     * @param conversation
     */
    @Override
    public void modifyLeftItemParentViewAfterSetValue(YWMessage msg, RelativeLayout leftItemParentView, Fragment fragment, YWConversation conversation) {

//        if(msg!=null&&leftItemParentView!=null&&msg.getSubType()==YWMessage.SUB_MSG_TYPE.IM_IMAGE||msg.getSubType()==YWMessage.SUB_MSG_TYPE.IM_GIF) {
//            leftItemParentView.setPadding(0, leftItemParentView.getPaddingTop(), leftItemParentView.getPaddingRight(), leftItemParentView.getPaddingBottom());
//        }
    }
    //聊天界面的消息item 的View做最后调整 结束
}

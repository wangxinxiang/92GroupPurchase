package com.jhlc.grouppurchase.IM;

import com.alibaba.mobileim.aop.AdviceBinder;
import com.alibaba.mobileim.aop.PointCutEnum;
import com.jhlc.grouppurchase.IM.UI.ChattingOperationCustom;
import com.jhlc.grouppurchase.IM.UI.ChattingUICustom;

/**
 * IM定制化初始化统一入口，这里后续会增加更多的IM定制化功能
 *
 * @author zhaoxu
 */
public class CustomUIAndOperationHelper {

    private static String TAG = CustomUIAndOperationHelper.class.getSimpleName();

    public static void initCustom() {

        //聊天界面的自定义
        AdviceBinder.bindAdvice(PointCutEnum.CHATTING_FRAGMENT_UI_POINTCUT, ChattingUICustom.class);
        AdviceBinder.bindAdvice(PointCutEnum.CHATTING_FRAGMENT_OPERATION_POINTCUT, ChattingOperationCustom.class);

//        //聊天界面相关自定义-------
//        //聊天界面的自定义风格1：［图片、文字小猪气泡］风格
//        AdviceBinder.bindAdvice(PointCutEnum.CHATTING_FRAGMENT_UI_POINTCUT, ChattingUICustomSample.class);
//        //聊天界面的自定义风格2：［图片切割气泡、文字小猪气泡］风格
////        AdviceBinder.bindAdvice(PointCutEnum.CHATTING_FRAGMENT_UI_POINTCUT, ChattingUICustomSample2.class);
//        //-----------------------
//        //聊天业务相关
//        AdviceBinder.bindAdvice(PointCutEnum.CHATTING_FRAGMENT_OPERATION_POINTCUT, ChattingOperationCustomSample.class);
//        //会话列表UI相关
//        AdviceBinder.bindAdvice(PointCutEnum.CONVERSATION_FRAGMENT_UI_POINTCUT, ConversationListUICustomSample.class);
//        //会话列表业务相关
//        AdviceBinder.bindAdvice(PointCutEnum.CONVERSATION_FRAGMENT_OPERATION_POINTCUT, ConversationListOperationCustomSample.class);
//        //消息通知栏
//        AdviceBinder.bindAdvice(PointCutEnum.NOTIFICATION_POINTCUT, NotificationInitSampleHelper.class);
//        AdviceBinder.bindAdvice(PointCutEnum.FRIENDS_POINTCUT, FriendsCustomAdviceSample.class);
//        //全局配置修改
//        AdviceBinder.bindAdvice(PointCutEnum.YWSDK_GLOBAL_CONFIG_POINTCUT, YWSDKGlobalConfigSample.class);
//
//        //@消息界面
//        AdviceBinder.bindAdvice(PointCutEnum.TRIBE_FRAGMENT_AT_MSG_DETAIL, SendAtMsgDetailUISample.class);
//        AdviceBinder.bindAdvice(PointCutEnum.TRIBE_ACTIVITY_AT_MSG_LIST, AtMsgListUISample.class);
    }
}

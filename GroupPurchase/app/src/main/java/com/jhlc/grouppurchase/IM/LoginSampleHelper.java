package com.jhlc.grouppurchase.IM;

import android.app.Application;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWChannel;
import com.alibaba.mobileim.YWIMCore;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.YWLoginParam;
import com.alibaba.mobileim.channel.LoginParam;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.channel.util.YWLog;
import com.alibaba.mobileim.gingko.model.tribe.YWTribe;
import com.alibaba.mobileim.gingko.model.tribe.YWTribeMember;
import com.alibaba.mobileim.login.IYWConnectionListener;
import com.alibaba.mobileim.login.YWLoginCode;
import com.alibaba.mobileim.login.YWLoginState;
import com.alibaba.mobileim.login.YWPwdType;
import com.alibaba.mobileim.utility.IMAutoLoginInfoStoreUtil;
import com.alibaba.tcms.env.EnvManager;
import com.alibaba.tcms.env.TcmsEnvType;
import com.alibaba.tcms.env.YWEnvManager;
import com.alibaba.tcms.env.YWEnvType;
import com.jhlc.grouppurchase.activity.LoginActivity;
import com.jhlc.grouppurchase.util.Constant;
import com.jhlc.grouppurchase.util.QGApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * SDK 初始化和登录
 *
 * @author jing.huai
 */
public class LoginSampleHelper {

    private static LoginSampleHelper sInstance = new LoginSampleHelper();

    public static LoginSampleHelper getInstance() {
        return sInstance;
    }

    // 应用APPKEY，这个APPKEY是申请应用时获取的
    public static  String APP_KEY = "23289560";

    //以下两个内容是测试环境使用，开发无需关注
    public static final String APP_KEY_TEST = "4272";  //60026702

    public static YWEnvType sEnvType = YWEnvType.ONLINE;

    // openIM UI解决方案提供的相关API，创建成功后，保存为全局变量使用
    private YWIMKit mIMKit;

    private YWConnectionListenerImpl mYWConnectionListenerImpl = new YWConnectionListenerImpl();
    private Application mApp;

    private List<Map<YWTribe, YWTribeMember>> mTribeInviteMessages = new ArrayList<Map<YWTribe, YWTribeMember>>();

    public YWIMKit getIMKit() {
        return mIMKit;
    }

    public void setIMKit(YWIMKit imkit) {
        mIMKit = imkit;
    }

    public void initIMKit(String userId, String appKey) {
        mIMKit = YWAPI.getIMKitInstance(userId.toString(), appKey);
        addConnectionListener();
    }

    private YWLoginState mAutoLoginState = YWLoginState.idle;

    public YWLoginState getAutoLoginState() {
        return mAutoLoginState;
    }

    public void setAutoLoginState(YWLoginState state) {
        this.mAutoLoginState = state;
    }

    /**
     * 初始化SDK
     *
     * @param context
     */
    public void initSDK_Sample(Application context) {
        mApp = context;
        sEnvType = YWEnvManager.getEnv(context);

        //初始化IMKit
		final String userId = IMAutoLoginInfoStoreUtil.getLoginUserId();
		final String appkey = IMAutoLoginInfoStoreUtil.getAppkey();
		if (!TextUtils.isEmpty(userId) && !TextUtils.isEmpty(appkey)){
//		final String mUserId = IMAutoLoginInfoStoreUtil.getLoginUserId();
			LoginSampleHelper.getInstance().initIMKit(userId, appkey);
//		final String appkey = IMAutoLoginInfoStoreUtil.getAppkey();
			NotificationInitSampleHelper.init();
		}
//		if (!TextUtils.isEmpty(mUserId) && !TextUtils.isEmpty(appkey)){
        TcmsEnvType type = EnvManager.getInstance().getCurrentEnvType(mApp);
        if(type== TcmsEnvType.ONLINE || type == TcmsEnvType.PRE){
            YWAPI.init(mApp, APP_KEY);
        }
        else if(type== TcmsEnvType.TEST){
            YWAPI.init(mApp, APP_KEY_TEST);
        }

        //通知栏相关的初始化
        NotificationInitSampleHelper.init();
        initAutoLoginStateCallback();
    }

    //将自动登录的状态广播出去
    private void sendAutoLoginState(YWLoginState loginState) {
        Intent intent = new Intent(Constant.AUTO_LOGIN_STATE_ACTION);
        intent.putExtra("state", loginState.getValue());
        LocalBroadcastManager.getInstance(YWChannel.getApplication()).sendBroadcast(intent);
    }

    /**
     * 登录操作
     *
     * @param userId   用户id
     * @param password 密码
     * @param callback 登录操作结果的回调
     */
    //------------------请特别注意，OpenIMSDK会自动对所有输入的用户ID转成小写处理-------------------
    //所以开发者在注册用户信息时，尽量用小写
    public void login_Sample(String userId, String password, String appKey,
                             IWxCallback callback) {

        if (mIMKit == null) {
            return;
        }


        YWLoginParam loginParam = YWLoginParam.createLoginParam(userId,
                password);
        if (TextUtils.isEmpty(appKey)) {
            loginParam.setServerType(LoginParam.ACCOUNTTYPE_WANGXIN);
            loginParam.setPwdType(YWPwdType.pwd);
        }
        // openIM SDK提供的登录服务
        IYWLoginService mLoginService = mIMKit.getLoginService();

        mLoginService.login(loginParam, callback);
    }

    //设置连接状态的监听
    private void addConnectionListener() {
        if (mIMKit == null) {
            return;
        }

        YWIMCore imCore = mIMKit.getIMCore();
        imCore.removeConnectionListener(mYWConnectionListenerImpl);
        imCore.addConnectionListener(mYWConnectionListenerImpl);
    }

    private class YWConnectionListenerImpl implements IYWConnectionListener {

        @Override
        public void onReConnecting() {

        }

        @Override
        public void onReConnected() {

//				YWLog.i("LoginSampleHelper", "onReConnected");


        }

        @Override
        public void onDisconnect(int arg0, String arg1) {
            if (arg0 == YWLoginCode.LOGON_FAIL_KICKOFF) {
                sendAutoLoginState(YWLoginState.disconnect);
                //在其它终端登录，当前用户被踢下线
                LoginSampleHelper.getInstance().setAutoLoginState(YWLoginState.disconnect);
                Toast.makeText(QGApplication.getInstance(), "被踢下线", Toast.LENGTH_LONG).show();
                YWLog.i("LoginSampleHelper", "被踢下线");
                Intent intent = new Intent(QGApplication.getInstance(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                QGApplication.getInstance().startActivity(intent);
            }
        }
    }

    /**
     * 登出
     */
    public void loginOut_Sample() {
        if (mIMKit == null) {
            return;
        }


        // openIM SDK提供的登录服务
        IYWLoginService mLoginService = mIMKit.getLoginService();
        mLoginService.logout(new IWxCallback() {

            @Override
            public void onSuccess(Object... arg0) {

            }

            @Override
            public void onProgress(int arg0) {

            }

            @Override
            public void onError(int arg0, String arg1) {

            }
        });
    }

    /**
     * 开发者不需要关注此方法，纯粹是DEMO自动登录的需要
     */
    private void initAutoLoginStateCallback() {
        YWChannel.setAutoLoginCallBack(new IWxCallback() {
            @Override
            public void onSuccess(Object... result) {
                mAutoLoginState = YWLoginState.success;
                sendAutoLoginState(mAutoLoginState);
            }

            @Override
            public void onError(int code, String info) {
                mAutoLoginState = YWLoginState.fail;
                sendAutoLoginState(mAutoLoginState);
            }

            @Override
            public void onProgress(int progress) {
                mAutoLoginState = YWLoginState.logining;
                sendAutoLoginState(mAutoLoginState);
            }
        });
    }

    public List<Map<YWTribe, YWTribeMember>> getTribeInviteMessages() {
        return mTribeInviteMessages;
    }
}

package com.jhlc.grouppurchase.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.callback.InitResultCallback;
import com.alibaba.sdk.android.media.MediaService;
import com.alibaba.wxlib.util.SysUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.jhlc.grouppurchase.IM.InitHelper;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Administrator on 2015/12/25.
 */
public class QGApplication extends Application{

    public static final String NAMESPACE = "wxq";
    public static MediaService mediaService;

    private static QGApplication mApplication;
    private List<Activity> activityList = new LinkedList<Activity>();
    private Stack<Activity> activityStack=new Stack<Activity>();

    //云旺OpenIM的DEMO用到的Application上下文实例
    private static Context sContext;


    public static Context getInstance() {
        return sContext;
    }

    public static QGApplication getmApplication(){
        return (QGApplication) sContext;
    }
    @Override
    public void onCreate() {
        super.onCreate();

        //Application.onCreate中，首先执行这部分代码, 因为，如果在":TCMSSevice"进程中，无需进行openIM和app业务的初始化，以节省内存
        //todo 特别注意:这段代码不能封装到其他方法中，必须在onCreate顶层代码中!
        //以下代码固定在此处，不要改动
        SysUtil.setApplication(this);
        if(SysUtil.isTCMSServiceProcess(this)){
            return;  //todo 特别注意：此处return是退出onCreate函数，因此不能封装到其他任何方法中!
        }
        //以上代码固定在这个位置，不要改动

        sContext = getApplicationContext();
        //初始化云旺SDK
        InitHelper.initYWSDK(this);

        initAliMedia();//初始化阿里多媒体服务
        Fresco.initialize(QGApplication.getInstance());//初始化Fresco图片加载框架
    }

    private void initAliMedia() {
        AlibabaSDK.asyncInit(this, new InitResultCallback() {
            @Override
            public void onSuccess() {
                Logger.d("AlibabaSDK   onSuccess");
                mediaService = AlibabaSDK.getService(MediaService.class);
            }

            @Override
            public void onFailure(int code, String msg) {
                Logger.d("AlibabaSDK onFailure  msg:" + msg + " code:" + code);
            }
        });
    }


    /**
     * 显示一个text的toast,且最多只显示一个toast的时间
     *
     */
    public void showTextToast(String msg) {

        Toast toast;

        toast = Toast.makeText(getApplicationContext(), msg,
                Toast.LENGTH_SHORT);

        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    //添加Activity到容器中
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    //遍历所有Activity并finish
    public void exit() {
        for (Activity activity : activityList) {
            activity.finish();
        }
        System.exit(0);
    }

    public void backTop() {
        for (int i = 1; i < activityList.size(); i++) {
            activityList.get(i).finish();
        }
    }

    //压入activity栈，在需要管理的activity的onCreate中添加
    public void pushStack(Activity activity){
        activityStack.push(activity);
    }

    //遍历栈，全部finish
    public void finishStack(){
        if (!activityStack.empty()){
            for (Activity activity:activityStack){
                activity.finish();
            }
        }
    }

    //在activity 中back键按下或者返回时调用 确保栈中的activity唯一
    public void popStack(Activity activity){
        if(!activityStack.empty()){
            if(activityStack.peek()==activity){
                activityStack.pop();
            }
        }
    }
}

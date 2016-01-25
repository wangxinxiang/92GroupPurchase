package com.jhlc.grouppurchase.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.media.upload.UploadListener;
import com.alibaba.sdk.android.media.upload.UploadOptions;
import com.alibaba.sdk.android.media.upload.UploadTask;
import com.alibaba.sdk.android.media.utils.FailReason;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jhlc.grouppurchase.IM.UI.ChattingOperationCustom;
import com.jhlc.grouppurchase.R;
import com.jhlc.grouppurchase.beans.BaseBean;
import com.jhlc.grouppurchase.httpUtils.ImageLoadFresco;
import com.jhlc.grouppurchase.httpUtils.ObjectCallBack;
import com.jhlc.grouppurchase.util.Constant;
import com.jhlc.grouppurchase.util.Logger;
import com.jhlc.grouppurchase.util.Notification;
import com.jhlc.grouppurchase.util.PreferencesUtils;
import com.jhlc.grouppurchase.util.QGApplication;
import com.jhlc.grouppurchase.util.Utils;
import com.jhlc.grouppurchase.view.FlowLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiCola on  2016/01/04  14:12
 */
public class GoodsPostActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "GoodsPostActivity";
    public static final String MSG = "msg";

    public String type;

    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";//图片名称 每次照片都会修改
    public final String FileName = "92QG";
    public String userId;

    private Context mContext;

    private EditText edit_goods_info;
    private LinearLayout ll_goods_location;
    private TextView tv_goods_location;
    private ImageButton ibtn_goods_location;
    private FlowLayout flow_group;

    private String mUserInput;//用户的输入
    private String mUserLocation;//用户的定位
    private List<String> mFileList = new ArrayList<>(9);//文件地址
    private List<String> mUrlList = new ArrayList<>(9);//上传成功的图片网络地址

    public UploadListener mUpLoadListener;
    public String mTaskId;//上传任务id 为暂停和取消使用

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_post);
        mContext = this;
        type = getIntent().getStringExtra(MSG);
        userId = PreferencesUtils.getStringPrefs(mContext, Constant.USER_ID, "");
        Logger.d(userId);
        initTitle();//初始化标题
        initView();//初始化控件

        addFlowItem("", true);//添加默认的 加号图
        initUpLoadListener();//初始化上传回调接口

//        mFileList.add("http://img.hb.aicdn.com/6e988592b2a5279b09bf7b10416ac8070d6b86e3646a-udu9rg");
//        mFileList.add("http://img.hb.aicdn.com/6e988592b2a5279b09bf7b10416ac8070d6b86e3646a-udu9rg");
//        mFileList.add("http://img.hb.aicdn.com/0952014cfa9139783cb99974c71aa411e3e51f3110a8b-DI9Ulh");
//        mFileList.add("http://img.hb.aicdn.com/966226169f87248ec87c7c5ec96aceb17e2de5476dc-Z6WSVE");
//        mFileList.add("file:///storage/emulated/0/Pictures/MyCustomApp/photo.j pg");
//        mFileList.add("");

//        addPhotoView();
//        addFlowItem("content://media/external/images/media/261663", false);
    }

    private void initView() {
        edit_goods_info = (EditText) findViewById(R.id.edit_goods_info);
        ll_goods_location = (LinearLayout) findViewById(R.id.ll_goods_location);
        ibtn_goods_location = (ImageButton) findViewById(R.id.ibtn_goods_location);
        tv_goods_location = (TextView) findViewById(R.id.tv_goods_location);
        flow_group = (FlowLayout) findViewById(R.id.flow_group);
        ll_goods_location.setOnClickListener(this);//绑定点击事件到this 集中实现
    }

    private void initTitle() {
        ((TextView)findViewById(R.id.tv_title_center)).setText("发布商品");
        findViewById(R.id.ibtn_chat_back).setOnClickListener(this);//绑定点击事件到this 集中实现
        Button btn_title_right = (Button) findViewById(R.id.btn_title_right);
        btn_title_right.setVisibility(View.VISIBLE);
        btn_title_right.setOnClickListener(this);//绑定点击事件到this 集中实现
        btn_title_right.setText("发布");
    }

    private void addFlowItem(String url, final boolean isAdd) {
        //实例化布局文件 添加到FlowView
        final ViewGroup view = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.item_goods_photo, flow_group, false);
        if (isAdd) {
            //true 表示需要 添加图片
            flow_group.addView(view);
            view.getChildAt(1).setVisibility(View.INVISIBLE);
        } else {
            flow_group.addView(view, 0);

//            FrescoBuilder.setHeadDrawableMC2V(mContext, (SimpleDraweeView) view.getChildAt(0), url, false);
            new ImageLoadFresco.LoadImageFrescoBuilder(mContext, (SimpleDraweeView) view.getChildAt(0), url)
                    .build();

            view.getChildAt(1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //清除图片
                    Logger.d();
                    flow_group.removeView(view);
                }
            });
        }

        view.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAdd) {
                    //打开相机 添加图片
                    Logger.d("add photo");
                    takePhotos();
                } else {
                    //浏览已经选中的图片
                    Logger.d("scan photo");
                }
            }
        });

    }

    /**
     * 打开相机 拍照
     */
    private void takePhotos() {
//        //打开相册应用 返回provider格式的图片地址
//        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent, 1);

        photoFileName = Utils.getCurrenTime() + ".jpg";
        Logger.d(photoFileName);
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri(photoFileName)); // set the image file name


        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    /**
     * 根据文件名返回Uri
     * 1.跳转拍照时调用 2.读取onActivityResult 返回结果时调用
     *
     * @param photoFileName
     * @return
     */
    private Uri getPhotoFileUri(String photoFileName) {
        // Only continue if the SD Card is mounted
        if (isExternalStorageAvailable()) {
            // Get safe storage directory for photos
            File mediaStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), FileName);

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
                Logger.d("failed to create directory");
            }

            // Return the file target for the photo based on filename
            return Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator + photoFileName));
        }
        return null;

    }

    /**
     * 手机存储是否 可用
     *
     * @return
     */
    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Logger.d("requestCode=" + requestCode + "  resultCode=" + resultCode);
//
//        Uri uri = data.getData();
//        Logger.d(uri.toString());

        Logger.d("requestCode=" + requestCode + " resultCode=" + resultCode);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                //得到返回结果 并添加到 mFileList变量中 和添加一个View
                Uri takenPhotoUri = getPhotoFileUri(photoFileName);
                if (takenPhotoUri != null) {
                    Logger.d(takenPhotoUri.getPath());
                    mFileList.add(takenPhotoUri.getPath());
                    addFlowItem(takenPhotoUri.toString(), false);
                }
            } else { // Result was a failure
                Logger.d("Picture wasn't taken!");
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ibtn_chat_back:
                onBackEdit();//返回事件处理
                break;
            case R.id.btn_title_right:
                onPostMessage();//发布事件
                break;
            case R.id.ll_goods_location:
                onLocation();//定位事件
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onBackEdit();
    }

    /**
     * 开始定位
     */
    private void onLocation() {
        //// TODO: 2016/1/22 0022 需要接入定位第三方 高德功能
        Logger.d("开始定位");
    }

    /**
     * 检查内容服务
     * 1.检查阿里多媒体服务
     * 2.检查用户输入 不能为空
     * 3.检查图片 不能为空
     * 通过检查 上传图片
     */
    private void onPostMessage() {
        //检查服务
        if (QGApplication.mediaService == null) {
            Logger.d("初始化失败，无法获取多媒体服务");
            Notification.showToastMsg(mContext, "无法获取多媒体服务");
            return;
        }

        // TODO: 2016/1/8 0008 检查输入内容
        mUserInput = edit_goods_info.getText().toString();
        if (TextUtils.isEmpty(mUserInput)) {
            Notification.showToastMsg(mContext, "请输入商品信息");
            return;
        }

        mUserLocation = tv_goods_location.getText().toString();

        if (mFileList.size() == 0) {
            Notification.showToastMsg(mContext, "请添加至少一张图片");
            return;
        }

        for (String path : mFileList) {
            UpLoadFile(path);//开始上传
        }
    }


    /**
     * UpLoadFile 上传图片到阿里多媒体云
     * 调用的方法
     */
    private void UpLoadFile(String mFilePath) {
        //上传选项配置
        final UploadOptions options = new UploadOptions.Builder()
                .dir("TestDir")
                .build();

        mTaskId = QGApplication.mediaService.upload(new File(mFilePath), QGApplication.NAMESPACE, options, mUpLoadListener);
    }

    /**
     * 在onCreate 初始化好 上传监听器 方便上传时调用
     */
    private void initUpLoadListener() {
        /**
         * UploadTask的参数解释
         * //自定义tag
         public String getTag();

         //文件大小
         public long getTotal();

         //已上传大小
         public long getCurrent();

         //原文件
         public File getFile();

         //上传完成信息
         public Result getResult();
         */

        mUpLoadListener = new UploadListener() {
            @Override
            public void onUploading(UploadTask uploadTask) {
                //上传中
//                Logger.d("onUploading= " + uploadTask.getCurrent() + "/" + uploadTask.getTotal());
            }

            @Override
            public void onUploadFailed(UploadTask uploadTask, FailReason failReason) {
                //上传失败
                Logger.d("onUploadFailed= " + failReason.toString());
                Notification.showToastMsg(mContext, "图片上传失败，请重试");
            }

            @Override
            public void onUploadComplete(UploadTask uploadTask) {
                //上传成功
                Logger.d("onUploadComplete= " + uploadTask.getResult().url);
                //添加网络图片地址到 mUrlList
                mUrlList.add(uploadTask.getResult().url);
                //如果上传完成 开始下一步
                if (mUrlList.size() == mFileList.size()) {
                    UpPublishMoments(mUrlList, mUserInput, mUserLocation);
                }
            }

            @Override
            public void onUploadCancelled(UploadTask uploadTask) {
                //上传取消
                Logger.d("onUploadCancelled");
            }
        };
    }

    /**
     * 提交数据到服务器
     *
     * @param urlList      上传到阿里多媒体服务器的 图片地址list
     * @param userInput    用户输入内容
     * @param userLocation 用户位置信息
     */
    private void UpPublishMoments(List<String> urlList, String userInput, String userLocation) {
        //网络参数初始化
        String time = Utils.getSimpleDate();

        String opcode = getString(R.string.url_publishMoments);
        String checkData = Utils.getcheckMD5(opcode, userId, time);

        //多张图片地址拼接在 url后 方便服务器接受 多张图片地址
        String url = getString(R.string.url) + "?";
        String image = "images=";
        for (int i = 0, size = urlList.size(); i < size; i++) {
            url += image + urlList.get(i);
            if (i != size - 1) {
                url += "&";
            }
        }
        Logger.d("拼接后 带图片的地址的url = " + url);

        PostFormBuilder builder = OkHttpUtils.post().url(url)
                .addParams("opcode", opcode)
                .addParams("userid", userId)
                .addParams("content", userInput)
                .addParams("momentsaddress", userLocation)
                .addParams("submitDate", time)
                .addParams("checkDate", checkData);

        builder.build().execute(new ObjectCallBack<BaseBean>(BaseBean.class) {
            @Override
            public void onResponse(BaseBean response) {
                //发送成功
                onCompletionTask();
            }
        });

    }

    /**
     * 完成所有操作
     */
    private void onCompletionTask() {
        if (Constant.TYPE_BUSINESS.equals(type)){
            //// TODO: 2016/1/22 0022 来自朋友圈调用的 返回处理
            finish();
        }else {
            //来自聊天的调用 的返回处理
            ChattingOperationCustom.clickListener.onPostGoodComplete(type,userId,mUserInput,mUrlList);
            finish();
        }
    }

    /**
     * title返回 和物理返回键调用
     */
    private void onBackEdit() {

    }
}

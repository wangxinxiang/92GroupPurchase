package com.jhlc.grouppurchase.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.alibaba.mobileim.utility.IMPrefsTools;
import com.alibaba.sdk.android.media.upload.UploadListener;
import com.alibaba.sdk.android.media.upload.UploadOptions;
import com.alibaba.sdk.android.media.upload.UploadTask;
import com.alibaba.sdk.android.media.utils.FailReason;
import com.jhlc.grouppurchase.R;
import com.jhlc.grouppurchase.beans.BaseBean;
import com.jhlc.grouppurchase.beans.GroupTypeBean;
import com.jhlc.grouppurchase.httpUtils.ObjectCallBack;
import com.jhlc.grouppurchase.util.Constant;
import com.jhlc.grouppurchase.util.DataVeri;
import com.jhlc.grouppurchase.util.Logger;
import com.jhlc.grouppurchase.util.QGApplication;
import com.jhlc.grouppurchase.util.Utils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/1/15.
 */
public class CreateGroupActivity extends BaseActivity{

    private ImageView icon;
    private String iconUrl, tribe_name, notice, grouptype;
    private UploadListener mUpLoadListener;
    private String mTaskId;//上传任务id 为暂停和取消使用
    private EditText groupName, groupNotice;
    private Spinner spinner_group_kind;
    private List<GroupTypeBean.WxqTypeListEntity> kinds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_group);
        initUpLoadListener();
        initView();
        initListener();
    }

    @Override
    protected void initView() {
        super.initView();
        initTitle();

        icon = (ImageView) findViewById(R.id.iv_creategroup_ico);
        groupName = (EditText) findViewById(R.id.edit_group_name);
        groupNotice = (EditText) findViewById(R.id.et_group_notice);
        spinner_group_kind = (Spinner) findViewById(R.id.spinner_group_kind);
        getGroupType();
    }

    private void initTitle() {
        setTitle("创建群");
        Button button = showRightButton("确定");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createGroup();
            }
        });
    }

    private void initListener() {
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_PICK);
                intent1.setType("image/*");
                startActivityForResult(intent1, 1);
            }
        });
    }

    private void initGroupType() {
        if (kinds == null) return;
        String[] items = new String[kinds.size()];
        for (int i = 0; i<kinds.size(); i++) {
            items[i] = kinds.get(i).getTypename();
        }
        Logger.d(Arrays.toString(items));
        spinner_group_kind.setAdapter(new ArrayAdapter<>(this,
                R.layout.simple_textview, items));
        spinner_group_kind.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                grouptype = String.valueOf(kinds.get(position).getTypeid());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                grouptype = String.valueOf(kinds.get(0).getTypeid());
            }
        });

        //设定默认值
    }

    /**
     * 创建群组
     */
    private void createGroup() {
        if (!checkData()) return;
        String localId = IMPrefsTools.getStringPrefs(this, Constant.USER_ID, "");
        OkHttpUtils.post().url(getString(R.string.url)).addParams("opcode", getString(R.string.url_createGroup))
                .addParams("userid", localId).addParams("icon", iconUrl).addParams("tribe_name", tribe_name)
                .addParams("notice", notice).addParams("grouptype", grouptype).addParams("submitDate",  Utils.getSimpleDate())
                .addParams("checkDate", "12").build()
                .execute(new ObjectCallBack<BaseBean>(BaseBean.class) {
                    @Override
                    public void onResponse(BaseBean response) {
                        QGApplication.getmApplication().showTextToast("创建成功");
                        finish();
                    }
                });

    }

    /**
     * 创建群组前检查数据
     * @return
     */
    private boolean checkData() {
        tribe_name = groupName.getText().toString().trim();
        notice = groupNotice.getText().toString().trim();

        return !DataVeri.stringIsNull(tribe_name, "群组名") && !DataVeri.stringIsNull(notice, "群描述");
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case 1:
                ContentResolver resolver = getContentResolver();

                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inPreferredConfig = Bitmap.Config.RGB_565;
                opt.inPurgeable = true;
                opt.inInputShareable = true;
                InputStream input = null;
                try {
                    input = resolver.openInputStream(data.getData());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Bitmap srcBitmap = BitmapFactory.decodeStream(input, null, opt);
                icon.setImageBitmap(srcBitmap);
                Uri uri = data.getData();


                String iconPath = Utils.getRealFilePath(this, uri);
                if (iconPath != null) {
                    UpLoadFile(iconPath);
                }

        }
    }

    /**
     * 获取群类型
     */
    private void getGroupType() {
        String opcode = getResources().getString(R.string.url_getWxqGroupTypeList);
        OkHttpUtils.post().url(getResources().getString(R.string.url)).addParams("opcode", opcode)
                .build().execute(new ObjectCallBack<GroupTypeBean>(GroupTypeBean.class) {
            @Override
            public void onResponse(GroupTypeBean response) {
                kinds = response.getWxqTypeList();
                initGroupType();
            }
        });
    }

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

        mUpLoadListener =new UploadListener() {
            @Override
            public void onUploading(UploadTask uploadTask) {
                //上传中
                Logger.d("onUploading= " + uploadTask.getCurrent() + "/" + uploadTask.getTotal());
            }

            @Override
            public void onUploadFailed(UploadTask uploadTask, FailReason failReason) {
                //上传失败
                Logger.d("onUploadFailed= "+failReason.toString());
            }

            @Override
            public void onUploadComplete(UploadTask uploadTask) {
                //上传成功
                Logger.d("onUploadComplete= "+uploadTask.getResult().url);
                iconUrl = uploadTask.getResult().url;
                Logger.d(iconUrl);
            }

            @Override
            public void onUploadCancelled(UploadTask uploadTask) {
                //上传取消
                Logger.d("onUploadCancelled");
            }
        };
    }

    /**
     * 退出编辑 title的返回键/物理返回键
     * 调用的方法
     */private void UpLoadFile(String mFilePath) {
        //上传选项配置
        final UploadOptions options = new UploadOptions.Builder()
                .dir("TestDir")
                .build();

        mTaskId= QGApplication.mediaService.upload(new File(mFilePath),QGApplication.NAMESPACE,options, mUpLoadListener);
    }
}

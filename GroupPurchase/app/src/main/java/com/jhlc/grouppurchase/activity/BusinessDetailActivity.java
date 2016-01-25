package com.jhlc.grouppurchase.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jhlc.grouppurchase.R;
import com.jhlc.grouppurchase.adapter.BusinessRecyclerViewAdapter;
import com.jhlc.grouppurchase.beans.BaseBean;
import com.jhlc.grouppurchase.beans.BusinessBean;
import com.jhlc.grouppurchase.httpUtils.FrescoBuilder;
import com.jhlc.grouppurchase.httpUtils.ObjectCallBack;
import com.jhlc.grouppurchase.util.DataVeri;
import com.jhlc.grouppurchase.util.QGApplication;
import com.jhlc.grouppurchase.util.Utils;
import com.jhlc.grouppurchase.view.FlowLayout;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/1/19.
 */
public class BusinessDetailActivity extends BaseActivity {

    private LinearLayout ll_comment;
    private EditText et_comment;
    private Button btn_comment;
    private BusinessBean.ListEntity businessBean;
    private BusinessRecyclerViewAdapter.BusinessItemView businessItemView;
    private ImageButton like, comment;      //点赞，评论按钮
    private LinearLayout message;
    private FlowLayout photos;
    private Integer position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_detail);
        initView();
        initListener();
    }

    @Override
    protected void initView() {
        super.initView();

        setTitle("详情");

        position = getIntent().getIntExtra("position", 0);
        businessBean = (BusinessBean.ListEntity) getIntent().getSerializableExtra("businessBean");
        businessItemView = new BusinessRecyclerViewAdapter.BusinessItemView(this, businessBean);

        //初始化界面
        SimpleDraweeView head = (SimpleDraweeView) findViewById(R.id.iv_business_item_header);
        FrescoBuilder.setHeadDrawableMC2V(this, head, businessBean.getIconurl(), false);
        TextView name = (TextView) findViewById(R.id.tv_business_item_name);
        name.setText(businessBean.getNick());
        TextView content = (TextView) findViewById(R.id.tv_business_item_content);
        content.setText(businessBean.getContent());
        TextView time = (TextView) findViewById(R.id.tv_business_item_time);
        time.setText(Utils.parseTime(businessBean.getCreatedate()));

        //设置删除按钮和联系卖家按钮
        TextView delete = (TextView) findViewById(R.id.tv_business_item_delete);
        LinearLayout contact = (LinearLayout) findViewById(R.id.tv_business_item_contact);
        businessItemView.initDelete(delete, contact);

        //动态添加
        message = (LinearLayout) findViewById(R.id.ll_business_item_message);
        photos = (FlowLayout) findViewById(R.id.ll_business_item_photo);
        businessItemView.addPhoto(photos, false);
        boolean isLoveNull = businessItemView.addLove(message, false);
        businessItemView.addComment(message, isLoveNull, false);

        //评论
        ll_comment = (LinearLayout) findViewById(R.id.ll_comment);
        et_comment = (EditText) findViewById(R.id.et_comment);
        btn_comment = (Button) findViewById(R.id.btn_comment);
        ll_comment.setVisibility(View.VISIBLE);

        like = (ImageButton) findViewById(R.id.ib_business_item_love);
        comment = (ImageButton) findViewById(R.id.ib_business_item_comment);
    }

    private void initListener() {
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                businessItemView.addMomentsLikeAndComment(0).build()
                        .execute(new ObjectCallBack<BaseBean>(BaseBean.class) {
                            @Override
                            public void onResponse(BaseBean response) {
                                QGApplication.getmApplication().showTextToast("点赞成功");
                                BusinessBean.ListEntity.LikeListEntity addLike = new BusinessBean.ListEntity.LikeListEntity();
                                addLike.setIconurl("http://www.diandidaxue.com:8080/images/beauty/20160117105854.jpg");
                                businessBean.getLikeList().add(addLike);
                                //更新界面
                                boolean isLoveNull = businessItemView.addLove(message, false);
                                businessItemView.addComment(message, isLoveNull, false);
                                EventBus.getDefault().post(position);
                            }
                        });

            }
        });

        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String content = et_comment.getText().toString().trim();
                if (DataVeri.stringIsNull(content, "评论内容")) return;
                businessItemView.addMomentsLikeAndComment(1)
                        .addParams("content", content).build()
                        .execute(new ObjectCallBack<BaseBean>(BaseBean.class) {
                            @Override
                            public void onResponse(BaseBean response) {
                                QGApplication.getmApplication().showTextToast("评论成功");
                                //通知朋友圈更新数据
                                EventBus.getDefault().post(position);
                                finish();
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                            }
                        });
            }
        });

    }

}

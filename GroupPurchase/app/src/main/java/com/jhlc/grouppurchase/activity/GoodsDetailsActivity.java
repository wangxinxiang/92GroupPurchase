package com.jhlc.grouppurchase.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.mobileim.YWIMKit;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.jhlc.grouppurchase.IM.LoginSampleHelper;
import com.jhlc.grouppurchase.IM.UI.ChattingOperationCustom;
import com.jhlc.grouppurchase.R;
import com.jhlc.grouppurchase.beans.ChatMessageBean;
import com.jhlc.grouppurchase.httpUtils.ImageLoadFresco;
import com.jhlc.grouppurchase.util.Constant;
import com.jhlc.grouppurchase.util.Logger;
import com.jhlc.grouppurchase.util.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiCola on  2015/12/30  18:40
 * 自定义消息的详情展示类
 */
public class GoodsDetailsActivity extends AppCompatActivity {
    private static final String TAG = "GoodsDetailsActivity";
    public static final String MESSAGEBODY = "MessageBody";

    private Context mContext;
    public String mUserId;
    private ChatMessageBean bean;

    private ViewPager mViewPager;
    private ImagePagerAdapter mAdapter;
    private RadioGroup radioGroup_point;

    private List<SimpleDraweeView> mListView = new ArrayList<>(9);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_goods_detail);
        mContext = this;
        String message = getIntent().getStringExtra(MESSAGEBODY);
        mUserId = PreferencesUtils.getStringPrefs(mContext,Constant.USER_ID);
        Logger.d("getMessage=" + message);
        initBean(message);//解析bean对象
        initViewData();
        setPoint(bean.getPhotoList().size());
        initListener();
    }

    private void initListener() {

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((RadioButton) radioGroup_point.getChildAt(position)).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        findViewById(R.id.ibtn_details_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });

        findViewById(R.id.ibtn_details_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.d("跳转到卖家商圈");
                ChattingOperationCustom.clickListener.onClickBusiness(bean.getUserId());
            }
        });

        Button btn_details_contact= (Button) findViewById(R.id.btn_details_contact);

        if (mUserId.equals(bean.getUserId())){
            //自己发出的商品 不需要 联系卖家
            Logger.d();
            btn_details_contact.setVisibility(View.GONE);
        }else {
            Logger.d();
            btn_details_contact.setVisibility(View.VISIBLE);
        }

        btn_details_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.d("跳转到联系卖家 id=" + bean.getUserId());
                //TODO 联系卖家之后 防止返回再次回到群聊 应该关闭群聊界面
                ChattingOperationCustom.clickListener.onClickContact(bean.getUserId());
                final YWIMKit imKit = LoginSampleHelper.getInstance().getIMKit();
                Intent intent=imKit.getChattingActivityIntent(bean.getUserId());
                startActivity(intent);
                finish();
            }
        });

    }

    private void setPoint(int size) {
        if (size == 0) {
            return;//数据为空 异常情况
        }
        radioGroup_point = (RadioGroup) findViewById(R.id.radiogroup_point);
        for (int i = 0, s = bean.getPhotoList().size(); i < s; i++) {
            getLayoutInflater().inflate(R.layout.image_point, radioGroup_point);
        }

        ((RadioButton) radioGroup_point.getChildAt(0)).setChecked(true);//默认选中第一项

    }

    private void initViewData() {
        Drawable dProgressImage = DrawableCompat.wrap(ContextCompat.getDrawable(mContext, R.drawable.ic_loop_white_48dp).mutate());
        DrawableCompat.setTint(dProgressImage, ContextCompat.getColor(mContext, R.color.tint_grey));

        for (int i = 0, size = bean.getPhotoList().size(); i < size; i++) {
            SimpleDraweeView sView = (SimpleDraweeView) getLayoutInflater().inflate(R.layout.item_image_viewpager, null);
            new ImageLoadFresco.LoadImageFrescoBuilder(mContext, sView, bean.getPhotoList().get(i))
                    .setProgressBarImage(dProgressImage)
                    .setImageScaleType(ScalingUtils.ScaleType.FIT_XY)
                    .build();
            mListView.add(sView);
        }

        mViewPager = (ViewPager) findViewById(R.id.viewpager_details);
        mAdapter = new ImagePagerAdapter(mListView);
        mViewPager.setAdapter(mAdapter);

        ((TextView) findViewById(R.id.tv_details_title)).setText(bean.getTitle());

    }

    private void initBean(String message) {
        Gson gson = new Gson();
        bean = gson.fromJson(message, ChatMessageBean.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private class ImagePagerAdapter extends PagerAdapter {
        private List<SimpleDraweeView> mList;

        public ImagePagerAdapter(List<SimpleDraweeView> mList) {
            this.mList = mList;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //实例化单个界面
            container.addView(mList.get(position));
            return mList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mList.get(position));//viewGroup中删除view
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}

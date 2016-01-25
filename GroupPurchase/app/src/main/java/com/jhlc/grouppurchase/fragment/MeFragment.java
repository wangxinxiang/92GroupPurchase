package com.jhlc.grouppurchase.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhlc.grouppurchase.R;
import com.jhlc.grouppurchase.activity.WalletActivity;
import com.jhlc.grouppurchase.util.QGApplication;

/**
 * Created by Administrator on 2015/12/28.
 */
public class MeFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private TextView tv_collcet, tv_attention_group, tv_attention_person, tv_group;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        initTitle(view);
        initView(view);
        initListener(view);
        return view;
    }


    /**
     * 初始化title
     */
    private void initTitle(View view) {
        ImageView back = (ImageView) view.findViewById(R.id.ibtn_chat_back);
        back.setVisibility(View.VISIBLE);

        TextView textView = (TextView) view.findViewById(R.id.tv_title_center);
        textView.setText(R.string.fragment_me_title);
    }

    private void initView(View view) {
        tv_collcet = (TextView) view.findViewById(R.id.tv_me_collect);
        tv_attention_group = (TextView) view.findViewById(R.id.tv_me_attention_group);
        tv_attention_person = (TextView) view.findViewById(R.id.tv_me_attention_person);
        tv_group = (TextView) view.findViewById(R.id.tv_me_group);
    }

    /**
     * 点击事件监听
     * @param view
     */
    private void initListener(View view) {
        if (mListener != null) {

            //收藏的商品点击
            (view.findViewById(R.id.tv_me_collect)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onCollectOnClick();
                }
            });

            //关注的人和群点击
            (view.findViewById(R.id.ll_me_attention_group)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onAttentionOnClick();
                }
            });

            //关注的人和群点击
            (view.findViewById(R.id.ll_me_attention_person)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            //我创建的群点击
            (view.findViewById(R.id.ll_me_group)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onMyCreatGroup();
                }
            });
        }

        //钱包点击
        (view.findViewById(R.id.me_wallet)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WalletActivity.class);
                startActivity(intent);
            }
        });

         //我是卖家点击
        (view.findViewById(R.id.me_seller)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //我是买家钱包点击
        (view.findViewById(R.id.me_buyer)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

/***************************************回调******************************************/
    /**
     * 设置Activity与Fragment的回调响应
     * @param listener
     */
    public void setOnFragmentListener(OnFragmentInteractionListener listener) {
        mListener = listener;
    }

    /**
     * 与activity交互
     */
    public interface OnFragmentInteractionListener {
        /**
         * 收藏商品点击
         */
        void onCollectOnClick();

        /**
         * 关注的人和群点击
         */
        void onAttentionOnClick();

        /**
         * 我创建的群点击
         */
        void onMyCreatGroup();
    }
}

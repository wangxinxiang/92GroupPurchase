package com.jhlc.grouppurchase.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhlc.grouppurchase.R;
import com.jhlc.grouppurchase.adapter.BusinessRecyclerViewAdapter;
import com.jhlc.grouppurchase.adapter.MyBusinessAdapter;
import com.jhlc.grouppurchase.view.DividerItemDecoration;
import com.jhlc.grouppurchase.view.DividerItemTopDecoration;

/**
 * Created by Administrator on 2016/1/21.
 */
public class MyBusinessFragment extends BusinessFragment{



    @Override
    protected void initTitle(View view) {
        TextView title = (TextView) view.findViewById(R.id.tv_title_center);
        title.setText(R.string.myBusiness);

        ImageButton back = (ImageButton) view.findViewById(R.id.ibtn_chat_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        ImageView message = (ImageView) view.findViewById(R.id.iv_title_right);
        message.setImageResource(R.drawable.my_business_message);
        message.setVisibility(View.VISIBLE);
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void initAdapter() {
        mAdapter = new MyBusinessAdapter(getContext(), businessBeans);
    }

    @Override
    protected String getOpcode() {
        return getString(R.string.url_getMyMomentsList);
    }

    @Override
    protected void initDividerItemDecoration() {
        DividerItemTopDecoration dividerItemDecoration = new DividerItemTopDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
    }
}

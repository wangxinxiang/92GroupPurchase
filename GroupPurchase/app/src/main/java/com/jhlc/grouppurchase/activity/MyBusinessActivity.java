package com.jhlc.grouppurchase.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.jhlc.grouppurchase.R;
import com.jhlc.grouppurchase.adapter.MyBusinessAdapter;
import com.jhlc.grouppurchase.fragment.MyBusinessFragment;

/**
 * Created by Administrator on 2016/1/21.
 */
public class MyBusinessActivity extends FragmentActivity{

    private FragmentManager manager;
    private FragmentTransaction transaction;
    private MyBusinessFragment myBusinessFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_business);
        initView();
    }

    protected void initView() {
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        myBusinessFragment = new MyBusinessFragment();
        transaction.replace(R.id.fl_my_business, myBusinessFragment);
        transaction.commit();
    }



}

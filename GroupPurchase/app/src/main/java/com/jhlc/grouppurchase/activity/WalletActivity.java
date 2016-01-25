package com.jhlc.grouppurchase.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import com.jhlc.grouppurchase.R;

/**
 * Created by Administrator on 2016/1/13.
 */
public class WalletActivity extends BaseActivity{


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet);
        initView();
        initListener();
    }

    @Override
    protected void initView() {
        super.initView();
        setTitle("钱包");
    }

    private void initListener() {
        findViewById(R.id.ll_wallet_recharge).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WalletActivity.this, RechargeActivity.class);
                startActivity(intent);
            }
        });
    }
}

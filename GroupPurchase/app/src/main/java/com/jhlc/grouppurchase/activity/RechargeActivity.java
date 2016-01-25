package com.jhlc.grouppurchase.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.jhlc.grouppurchase.R;
import com.jhlc.grouppurchase.util.DataVeri;

/**
 * Created by Administrator on 2016/1/13.
 */
public class RechargeActivity extends BaseActivity{

    private EditText money;
    private String num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recharge);
        initView();
        setTitle("充值");
        initListener();
    }

    @Override
    protected void initView() {
        super.initView();
        money = (EditText) findViewById(R.id.et_recharge_money);
    }

    private void initListener() {
        //贝付支付
        findViewById(R.id.ll_recharge_beifu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkData()) {

                }
            }
        });
    }

    private boolean checkData() {
        num = money.getText().toString().trim();
        return DataVeri.isNaN(num, "金额");
    }
}

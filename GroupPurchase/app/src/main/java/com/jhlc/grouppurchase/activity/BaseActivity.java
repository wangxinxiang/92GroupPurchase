package com.jhlc.grouppurchase.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jhlc.grouppurchase.R;

/**
 * Created by Administrator on 2016/1/13.
 */
public class BaseActivity extends Activity{
    private TextView title;
    private ImageButton back;
    private Button right;

    protected void initView() {
        title = (TextView) findViewById(R.id.tv_title_center);
        back = (ImageButton) findViewById(R.id.ibtn_chat_back);
        right = (Button) findViewById(R.id.btn_title_right);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected void setTitle(String title) {
        this.title.setText(title);
    }

    protected void showBack(boolean isShow) {
        if (isShow) {
            back.setVisibility(View.VISIBLE);
        } else {
            back.setVisibility(View.GONE);
        }
    }

    /**
     * 显示标题右侧的按钮
     * @param str 按钮显示的字
     */
    protected Button showRightButton(String str) {
        right.setVisibility(View.VISIBLE);
        right.setText(str);
        return right;
    }
}

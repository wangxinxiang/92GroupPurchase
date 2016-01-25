package com.jhlc.grouppurchase.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.alibaba.mobileim.YWIMKit;
import com.jhlc.grouppurchase.IM.LoginSampleHelper;
import com.jhlc.grouppurchase.R;
import com.jhlc.grouppurchase.fragment.BusinessFragment;
import com.jhlc.grouppurchase.fragment.FindGroupFragment;
import com.jhlc.grouppurchase.fragment.MeFragment;
import com.jhlc.grouppurchase.fragment.MessageConFragment;
import com.jhlc.grouppurchase.fragment.MessageFragment;
import com.jhlc.grouppurchase.util.Logger;
import com.jhlc.grouppurchase.util.QGApplication;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2015/12/25.
 */
public class MainActivity extends AppCompatActivity {

    public static final String LOGIN_SUCCESS = "loginSuccess"; //为openIM准备的常量

    private RadioButton rbtn_firstpage, rbtn_exchange, rbtn_me, rbtn_more;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private RadioGroup radioGroup;
    private static Boolean isQuit = false;
    private final Timer timer = new Timer();
    private Fragment findGroupFragment, businessFragment,messageFragment;
    private MeFragment meFragment;

    public YWIMKit mIMKit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIMKit = LoginSampleHelper.getInstance().getIMKit();
        if (mIMKit == null) {
            return;
        }
        setContentView(R.layout.maintab);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setOnClick();
    }

    protected void initView() {
        rbtn_firstpage = (RadioButton) findViewById(R.id.rbtn_firstpage);
        rbtn_exchange = (RadioButton) findViewById(R.id.rbtn_exchange);
        rbtn_me = (RadioButton) findViewById(R.id.rbtn_me);
        rbtn_more = (RadioButton) findViewById(R.id.rbtn_business);
        radioGroup = (RadioGroup) this.findViewById(R.id.radiogroup);
        manager = getSupportFragmentManager();
        setSelected(0);
    }

    protected void setOnClick() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbtn_firstpage:
                        setSelected(0);
                        break;
                    case R.id.rbtn_exchange:
                        setSelected(1);
                        break;
                    case R.id.rbtn_business:
                        setSelected(2);
                        break;
                    case R.id.rbtn_me:
                        setSelected(3);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isQuit == false) {
                isQuit = true;
                QGApplication.getmApplication().showTextToast("再按一次退出秒赚不误");
                TimerTask task = null;
                task = new TimerTask() {
                    @Override
                    public void run() {
                        isQuit = false;
                    }
                };
                timer.schedule(task, 2000);
            } else {
                QGApplication.getmApplication().exit();
            }
        }
        return true;
    }

    private void setSelected(int position) {
        transaction = manager.beginTransaction();
        hideFragment(transaction);
        switch (position) {
            case 0:
                if (findGroupFragment == null) {
                    findGroupFragment = new FindGroupFragment();
                    transaction.add(R.id.content, findGroupFragment);
                } else {
                    transaction.show(findGroupFragment);
                }
                rbtn_firstpage.setChecked(true);
                break;
            case 1:
//                transaction.replace(R.id.content, new ExchangeFragment());
                if (messageFragment == null) {
                    messageFragment = MessageConFragment.newInstance("none");
                    transaction.add(R.id.content, messageFragment);
                } else {
                    transaction.show(messageFragment);
                }
                rbtn_exchange.setChecked(true);
                break;
            case 2:
                if (businessFragment == null) {
                    businessFragment = new BusinessFragment();
                    transaction.add(R.id.content, businessFragment);
                } else {
                    transaction.show(businessFragment);
                }
                rbtn_more.setChecked(true);

                break;
            case 3:
                if (meFragment == null) {
                    createMeFragment(transaction);
                } else {
                    transaction.show(meFragment);
                }
                rbtn_me.setChecked(true);
                break;
            default:
                break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (findGroupFragment != null) {
            transaction.hide(findGroupFragment);
        }
        if (messageFragment != null) {
            transaction.hide(messageFragment);
        }
        if (meFragment != null) {
            transaction.hide(meFragment);
        }
        if (businessFragment != null) {
            transaction.hide(businessFragment);
        }
    }

    /**
     * 创建添加MeFragment
     * @param transaction
     */
    private void createMeFragment(FragmentTransaction transaction) {
        meFragment = new MeFragment();
        transaction.add(R.id.content, meFragment);
        //回调
        meFragment.setOnFragmentListener(new MeFragment.OnFragmentInteractionListener() {
            @Override
            public void onCollectOnClick() {

            }

            @Override
            public void onAttentionOnClick() {

            }

            @Override
            public void onMyCreatGroup() {

            }
        });
    }
}

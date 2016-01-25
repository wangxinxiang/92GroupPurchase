package com.jhlc.grouppurchase.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jhlc.grouppurchase.R;
import com.jhlc.grouppurchase.activity.SearchGroupActivity;
import com.jhlc.grouppurchase.beans.GroupTypeBean;
import com.jhlc.grouppurchase.httpUtils.FrescoBuilder;
import com.jhlc.grouppurchase.util.Logger;
import com.jhlc.grouppurchase.util.QGApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/26.
 */
public class FindGroupViewPagerContent extends LinearLayout{
    private FlowLayout flowLayout;          //流式布局
    private List<GroupTypeBean.WxqTypeListEntity> kinds;       //图标
    private int itemWidth;
    private LayoutInflater inflater;

    public FindGroupViewPagerContent(Context context, List<GroupTypeBean.WxqTypeListEntity> kinds) {
        super(context);
        this.kinds = kinds;
        inflater = LayoutInflater.from(context);
        init();
    }


    private void init() {
        itemWidth = getScreenWidth(getContext()) / 4;
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.findgroup_viewpager_view,this);
        flowLayout = (FlowLayout) findViewById(R.id.fl_findgroup_viewpager);

        //添加item
        for (int i = 0; i < kinds.size(); i++) {
            GroupTypeBean.WxqTypeListEntity kind = kinds.get(i);

            addKind(kind);
        }
    }

    /**
     * 动态添加每个item
     */
    private void addKind(final GroupTypeBean.WxqTypeListEntity kind) {
        LinearLayout type = (LinearLayout) inflater.inflate(R.layout.group_type, flowLayout, false);

        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) type.getChildAt(0);
        FrescoBuilder.setHeadDrawableMC2V(getContext(), simpleDraweeView, kind.getIcon(), true);

        TextView textView = (TextView) type.getChildAt(1);
        textView.setText(kind.getTypename());

        type.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchGroupActivity.class);
                intent.putExtra(SearchGroupActivity.GroupType, kind.getTypeid());
                intent.putExtra(SearchGroupActivity.GroupName, kind.getTypename());
                getContext().startActivity(intent);
            }
        });
        flowLayout.addView(type);

    }


    //得到屏幕宽度
    private int getScreenWidth(Context context) {

        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

}

package com.jhlc.grouppurchase.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by wang on 2015/3/16.
 */
public class FindGroupViewPageAdapter extends PagerAdapter {

    private ArrayList<View> views = new ArrayList<View>();
    private Context context;

    public FindGroupViewPageAdapter(Context context, ArrayList<View> views) {
        this.views = views;
        this.context = context;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position));
        return views.get(position);
    }
}

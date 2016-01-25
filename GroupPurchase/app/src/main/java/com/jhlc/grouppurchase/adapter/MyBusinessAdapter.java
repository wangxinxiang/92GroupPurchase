package com.jhlc.grouppurchase.adapter;

import android.content.Context;

import com.jhlc.grouppurchase.beans.BusinessBean;

import java.util.List;

/**
 * Created by Administrator on 2016/1/21.
 * 去除头部操作
 */
public class MyBusinessAdapter extends BusinessRecyclerViewAdapter{

    public MyBusinessAdapter(Context context, List<BusinessBean.ListEntity> lists) {
        super(context, lists);
    }

    /**
     * 重写，去除头部  下同
     * @param position item 位置
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() - 1;
    }

    @Override
    protected BusinessBean.ListEntity getPositionInfo(int position) {
        return lists.get(position);
    }
}

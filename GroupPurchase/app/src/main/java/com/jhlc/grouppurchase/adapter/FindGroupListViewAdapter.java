package com.jhlc.grouppurchase.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jhlc.grouppurchase.R;
import com.jhlc.grouppurchase.beans.GroupBean;
import com.jhlc.grouppurchase.httpUtils.FrescoBuilder;
import com.jhlc.grouppurchase.httpUtils.ImageLoadFresco;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/26.
 */
public class FindGroupListViewAdapter extends BaseAdapter{

    private Context mContext;
    private List<GroupBean.GroupListEntity> groupBeans;
    private LayoutInflater mInflater;

    public FindGroupListViewAdapter(Context mContext, List<GroupBean.GroupListEntity> groupBeans) {
        this.mContext = mContext;
        this.groupBeans = groupBeans;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return groupBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GroupBean.GroupListEntity groupBean = groupBeans.get(position);

        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.group_item, parent, false);
            viewHolder.head = (SimpleDraweeView) convertView.findViewById(R.id.iv_group_img);
            viewHolder.title = (TextView) convertView.findViewById(R.id.tv_group_title);
            viewHolder.describe = (TextView) convertView.findViewById(R.id.tv_group_signature);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        new ImageLoadFresco.LoadImageFrescoBuilder(mContext, viewHolder.head, groupBean.getIcon())
                .setFailureImage(mContext.getResources().getDrawable(R.drawable.people_gray)).build();

        viewHolder.title.setText(groupBean.getTribeName() + "(" + groupBean.getMembercount() + ")");
        viewHolder.describe.setText(groupBean.getNotice());

        return convertView;
    }

    static class ViewHolder {
        SimpleDraweeView head;
        TextView title;
        TextView time;
        TextView describe;
    }
}

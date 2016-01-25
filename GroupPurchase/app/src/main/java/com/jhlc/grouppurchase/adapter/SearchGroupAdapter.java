package com.jhlc.grouppurchase.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jhlc.grouppurchase.R;
import com.jhlc.grouppurchase.beans.GroupBean;
import com.jhlc.grouppurchase.httpUtils.FrescoBuilder;

import java.util.List;

/**
 * Created by Administrator on 2016/1/21.
 */
public class SearchGroupAdapter extends RecyclerView.Adapter<SearchGroupAdapter.MyViewHolder>{

    private Context mContext;
    private List<GroupBean.GroupListEntity> groupBeans;
    private LayoutInflater mInflater;

    public SearchGroupAdapter(Context mContext, List<GroupBean.GroupListEntity> groupBeans) {
        this.mContext = mContext;
        this.groupBeans = groupBeans;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.business_header, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        GroupBean.GroupListEntity groupBean = groupBeans.get(position);
        FrescoBuilder.setHeadDrawableMC2V(mContext, holder.head, groupBean.getIcon(), false);
        holder.title.setText(groupBean.getTribeName());
        holder.describe.setText(groupBean.getNotice());
    }

    @Override
    public int getItemCount() {
        return groupBeans.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        SimpleDraweeView head;
        TextView title;
        TextView describe;

        public MyViewHolder(View itemView) {
            super(itemView);
            head = (SimpleDraweeView) itemView.findViewById(R.id.iv_group_img);
            title = (TextView) itemView.findViewById(R.id.tv_group_title);
            describe = (TextView) itemView.findViewById(R.id.tv_group_signature);
        }
    }
}

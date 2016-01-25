package com.jhlc.grouppurchase.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.mobileim.utility.IMPrefsTools;
import com.jhlc.grouppurchase.R;
import com.jhlc.grouppurchase.activity.GoodsPostActivity;
import com.jhlc.grouppurchase.adapter.BusinessRecyclerViewAdapter;
import com.jhlc.grouppurchase.beans.BusinessBean;
import com.jhlc.grouppurchase.httpUtils.ObjectCallBack;
import com.jhlc.grouppurchase.util.Constant;
import com.jhlc.grouppurchase.view.DividerItemDecoration;
import com.jhlc.grouppurchase.util.QGApplication;
import com.jhlc.grouppurchase.util.Utils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * Created by Administrator on 2016/1/3.
 */
public class BusinessFragment extends Fragment {

    protected RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;
    private ImageView take_photo;

    protected BusinessRecyclerViewAdapter mAdapter;
    private int lastVisibleItem;
    private LinearLayoutManager mLayoutManager;

    protected List<BusinessBean.ListEntity> businessBeans = new ArrayList<>();
    private int currentPageNum;
    private boolean isAddData;   //是否可以上拉加载数据

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_business, container, false);
        initView(view);
        initListener();
        EventBus.getDefault().register(this);
        getData(false, 0);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView(View view) {
        initTitle(view);

        //下拉刷新
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_widget);
        mRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_business);
        initRecyclerView();

    }

    protected void initTitle(View view) {
        view.findViewById(R.id.ibtn_chat_back).setVisibility(View.GONE);
        take_photo = (ImageView) view.findViewById(R.id.iv_title_right);
        TextView title = (TextView) view.findViewById(R.id.tv_title_center);
        title.setText(R.string.fg_business_title);
        take_photo.setVisibility(View.VISIBLE);

        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GoodsPostActivity.class);
                intent.putExtra(GoodsPostActivity.MSG, "business");
                startActivity(intent);
            }
        });
    }

    private void initListener() {
        //设置上拉刷新时要执行的动作
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(false, 0);
            }
        });

        /**
         * listView设置上拉刷新响应,当距离底部还有5个item时执行刷新
         */
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 2 == mAdapter.getItemCount() && isAddData) {
                    getData(true, 0);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();      //获取显示的最后一个item位置
            }
        });

    }

    private void initRecyclerView() {
        mRefreshLayout.setColorSchemeResources(R.color.blue, R.color.black);

        //RecyclerView
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //设置分割线
        initDividerItemDecoration();

        initAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    protected void initAdapter() {
        mAdapter = new BusinessRecyclerViewAdapter(getContext(), businessBeans);
        mAdapter.setmListener(new BusinessRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {

            }

        });
    }
    protected  void initDividerItemDecoration() {
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        dividerItemDecoration.setHaveHead(true);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
    }
    /**
     * 获取朋友圈信息
     * @param isAdd 是否是增加数据
     * @param position  更新的item位置，0为更新所有
     */
    private void getData(boolean isAdd, final int position) {
        isAddData = false;
        if (isAdd) {
            currentPageNum++;
        } else {
            currentPageNum = 1;
            businessBeans.clear();
        }
        String time = Utils.getSimpleDate();
        String localId = IMPrefsTools.getStringPrefs(getContext(), Constant.USER_ID, "");
        String opcode = getOpcode();
        String checkData = Utils.getcheckMD5(opcode, localId, time);

        OkHttpUtils.post().url(getString(R.string.url)).addParams("opcode", opcode)
                .addParams("userid", "000001").addParams("pageNum", String.valueOf(currentPageNum))
                .addParams("numPerPage", "5").addParams("submitDate", time).addParams("checkDate", checkData).build()
                .execute(new ObjectCallBack<BusinessBean>(BusinessBean.class) {
                    @Override
                    public void onResponse(BusinessBean response) {
                        isAddData = response.getList().size() != 0;     //如果可以上拉加载已经没有数据了，就不在上拉加载操作
                        businessBeans.addAll(response.getList());
                        mRefreshLayout.setRefreshing(false);
                        if (position == 0) {
                            mAdapter.notifyDataSetChanged();
                        } else {
                            mAdapter.notifyItemChanged(position);
                        }

                    }
                });
    }

    protected String getOpcode() {
        return getString(R.string.url_getMomentsList);
    }

    @Subscribe
    public void onEvent(Integer position) {
        getData(false, position);
    }
}

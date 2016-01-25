package com.jhlc.grouppurchase.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jhlc.grouppurchase.R;
import com.jhlc.grouppurchase.util.Logger;


/**
 * Created by Administrator on 2015/12/30.
 * 上拉加载的listView
 */
public class RefreshListView extends ListView implements AbsListView.OnScrollListener {


    private LinearLayout footView;
    private TextView footText;      //尾部刷新要显示的字

    private boolean loadEnable;
    private OnLoadListener onLoadListener;
    private boolean isLoading;      //是否正在加载
    private boolean isLoadFull;     //是否还有数据可以加载，如果false则显示已经到底部

    public final static int STATUES_REFRESHING = 0;
    public final static int STATUES_OVER = 1;

    public RefreshListView(Context context) {
        this(context, null);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initFootView(context);
        this.setOnScrollListener(this);
    }

    /**
     * 初始化尾部加载
     */
    private void initFootView(Context context) {
        footView = (LinearLayout) View.inflate(context, R.layout.refresh_foot1, null);
        footText = (TextView) footView.findViewById(R.id.tv_refresh_foot);
        addFooterView(footView);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        ifNeedLoad(view, scrollState);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }

    /**
     * 根据listview滑动的状态判断是否需要加载更多
     */
    private void ifNeedLoad(AbsListView view, int scrollState) {
        if (!loadEnable) {
            return;
        }
        try {
            if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
                    && !isLoading               //是否在加载
                    && view.getLastVisiblePosition() == view.getPositionForView(footView)
                    && !isLoadFull) {
                onLoad();
                isLoading = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行刷新接口方法
     */
    private void onLoad() {
        if (onLoadListener != null) {
            onLoadListener.onLoad();
        }
    }

    // 加载更多监听
    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.loadEnable = true;
        this.onLoadListener = onLoadListener;
    }

    /**
     *  定义加载更多接口
     */
    public interface OnLoadListener {
        void onLoad();
    }


    /**
     *设置刷新的状态
     * 当没有数据可以刷新时，也就是到了底部时。设置状态 STATUES_OVER
     */
    public void setStatus(int statues) {
        switch (statues) {
            case STATUES_REFRESHING:
                footText.setText("上拉加载更多...");
                isLoadFull = false;
                break;
            case STATUES_OVER:
                footText.setText("已经到底部了");
                isLoadFull = true;
        }
    }

    // 用于加载更多结束后的回调
    public void onLoadComplete() {
        isLoading = false;
    }
}

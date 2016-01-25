package com.jhlc.grouppurchase.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;

import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.tribe.IYWTribeService;
import com.alibaba.mobileim.utility.IMPrefsTools;
import com.jhlc.grouppurchase.IM.LoginSampleHelper;
import com.jhlc.grouppurchase.R;
import com.jhlc.grouppurchase.activity.CreateGroupActivity;
import com.jhlc.grouppurchase.activity.SearchGroupActivity;
import com.jhlc.grouppurchase.adapter.FindGroupListViewAdapter;
import com.jhlc.grouppurchase.adapter.FindGroupViewPageAdapter;
import com.jhlc.grouppurchase.beans.GroupBean;
import com.jhlc.grouppurchase.beans.GroupTypeBean;
import com.jhlc.grouppurchase.httpUtils.ObjectCallBack;
import com.jhlc.grouppurchase.util.Constant;
import com.jhlc.grouppurchase.util.DataVeri;
import com.jhlc.grouppurchase.util.Logger;
import com.jhlc.grouppurchase.util.Utils;
import com.jhlc.grouppurchase.view.FindGroupViewPagerContent;
import com.jhlc.grouppurchase.view.RefreshListView;
import com.jhlc.grouppurchase.view.ViewPageForScrollView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/25.
 */
public class FindGroupFragment extends Fragment {

    private ViewPageForScrollView viewPager;
    private RefreshListView listView;
    private FindGroupListViewAdapter groupAdapter;
    private List<GroupBean.GroupListEntity> groupBeanList = new ArrayList<>();      //adapter要用的数据
    private int currentPager = 1, currentGroup = 0;         //请求数据的页数    显示的group的index

    private RadioGroup rg_group;
    private ImageView groupTab1,groupTab2,groupTab3;            //group指示器
    private LinearLayout viewPager_indicated;               //viewPager页面指示
    private List<GroupTypeBean.WxqTypeListEntity> kinds;

    private ImageView search;
    private EditText searchInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.fg_find_group, container, false);
        getGroupType();
        initHeaderView(view);
        initListView();
        initTitle(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 创建群组
     */
    private void initTitle(View view) {
        ImageButton createGroup = (ImageButton) view.findViewById(R.id.ib_findgroup_add);

        View root = LayoutInflater.from(getContext()).inflate(R.layout.pw_create_group, null);
        final PopupWindow popupWindow = new PopupWindow(root, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));

        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.showAsDropDown(v);
            }
        });

        Button btn_creategroup = (Button) root.findViewById(R.id.btn_creategroup);
        btn_creategroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateGroupActivity.class);
                startActivity(intent);
                popupWindow.dismiss();
            }
        });

        searchInfo = (EditText) view.findViewById(R.id.et_findgroup_search);
        search = (ImageView) view.findViewById(R.id.iv_findgroup_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = searchInfo.getText().toString().trim();
                if (!DataVeri.stringIsNull(info, "搜索信息")) {
                    Intent intent = new Intent(getActivity(), SearchGroupActivity.class);
                    intent.putExtra(SearchGroupActivity.GroupName, info);
                    startActivity(intent);
                }
            }
        });
    }

    /************************************* 初始化中部的ViewPage****************/
    private void initHeaderView(View view) {
        listView = (RefreshListView) view.findViewById(R.id.findgroup_bottom_listView);

        View header = View.inflate(getContext(), R.layout.fg_find_group_header, null);
        listView.addHeaderView(header);
        viewPager = (ViewPageForScrollView)header.findViewById(R.id.tab1_center_guide);
        viewPager_indicated = (LinearLayout) header.findViewById(R.id.ll_tab1_indicate);

        rg_group = (RadioGroup) header.findViewById(R.id.rg_group);
        groupTab1 = (ImageView) header.findViewById(R.id.iv_group_tab1);
        groupTab2 = (ImageView) header.findViewById(R.id.iv_group_tab2);
        groupTab3 = (ImageView) header.findViewById(R.id.iv_group_tab3);
    }

    private void initViewPage(){
        ArrayList<View> views = new ArrayList<View>();

        if (kinds == null) return;
        int alreadyShow = 0;
        while (alreadyShow < kinds.size()) {
            FindGroupViewPagerContent view;
            if (alreadyShow + 8 < kinds.size()) {
                view = new FindGroupViewPagerContent(getContext(), kinds.subList(alreadyShow, alreadyShow+8));
            } else {
                view = new FindGroupViewPagerContent(getContext(), kinds.subList(alreadyShow, kinds.size()));
            }
            views.add(view);
            alreadyShow += 8;
        }

        final List<View> indicates = addIndicate(2);

        final PagerAdapter adapter = new FindGroupViewPageAdapter(getActivity(),views);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                for (int j = 0; j < 2; j++) {
                    ImageView imageView = (ImageView) indicates.get(j);
                    if (j == i) {
                        imageView.setImageResource(R.mipmap.viewpager_guider_focused);
                    } else {
                        imageView.setImageResource(R.mipmap.viewpager_guider_normal);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    private List<View> addIndicate(int size) {
        ArrayList<View> views = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ImageView view = (ImageView) LayoutInflater.from(getContext()).inflate(R.layout.indicate_imageview, viewPager_indicated, false);
            viewPager_indicated.addView(view);
            views.add(view);
            if ( i == 0) {
                view.setImageResource(R.mipmap.viewpager_guider_focused);
            } else {
                view.setImageResource(R.mipmap.viewpager_guider_normal);
            }
        }
        return views;
    }

    /************************************* 初始化中部的ListView****************************************/

    private void initListView(){
        groupAdapter = new FindGroupListViewAdapter(getContext(), groupBeanList);
        listView.setAdapter(groupAdapter);

        rg_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbtn_hot_group:
                        setSelect(0);
                        break;
                    case R.id.rbtn_attentipn_group:
                        setSelect(1);
                        break;
                    case R.id.rbtn_create_group:
                        setSelect(2);
                        break;
                }
            }
        });
        setSelect(currentGroup);

        /**
         * 上拉加载更多
         */
        listView.setOnLoadListener(new RefreshListView.OnLoadListener() {
            @Override
            public void onLoad() {
                getData(true, currentGroup);
                Logger.d("上拉加载");
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (position == groupBeanList.size()) return;
                final YWIMKit imKit = LoginSampleHelper.getInstance().getIMKit();
                IYWTribeService tribeService = imKit.getTribeService();
                tribeService.joinTribe(new IWxCallback() {
                    @Override
                    public void onSuccess(Object... objects) {
                        //参数为群ID号
                        Intent intent = imKit.getTribeChattingActivityIntent(groupBeanList.get(position - 1).getTribeId());
                        startActivity(intent);
                    }

                    @Override
                    public void onError(int i, String s) {
                        //参数为群ID号
                        Intent intent = imKit.getTribeChattingActivityIntent(groupBeanList.get(position - 1).getTribeId());
                        startActivity(intent);
                    }

                    @Override
                    public void onProgress(int i) {

                    }
                }, groupBeanList.get(position - 1).getTribeId());

            }
        });
    }

    /**
     * tab被选中时的处理
     * @param i 被选中的tab项
     */
    private void setSelect(int i) {
        groupTab1.setVisibility(View.INVISIBLE);
        groupTab2.setVisibility(View.INVISIBLE);
        groupTab3.setVisibility(View.INVISIBLE);
        switch (i) {
            case 0:
                groupTab1.setVisibility(View.VISIBLE);
                currentGroup = 0;
                break;
            case 1:
                groupTab2.setVisibility(View.VISIBLE);
                currentGroup = 1;
                break;
            case 2:
                groupTab3.setVisibility(View.VISIBLE);
                currentGroup = 2;
        }
        getData(false, currentGroup);
    }


    /************************************* 获取数据 ******************************************/


    /**
     * 刷新界面
     * @param newList   新增的数据
     * @param isAdd     是否是上拉加载数据
     */
    private void refreshData(List<GroupBean.GroupListEntity> newList, boolean isAdd) {
        if (!isAdd) {
            groupBeanList.clear();
        }
        if (newList != null){
            groupBeanList.addAll(newList);

        }

        //更新上拉加载信息
        if (newList == null|| newList.size() == 0) {
            listView.setStatus(RefreshListView.STATUES_OVER);
        } else {
            listView.setStatus(RefreshListView.STATUES_REFRESHING);
        }
        listView.onLoadComplete();          //加载完成

        groupAdapter.notifyDataSetChanged();

    }

    /**
     * 设置将要获取的pageNum
     * @param isAdd
     */
    private void setCurrentPager(boolean isAdd) {
        if (isAdd) {
            currentPager++;
        } else {
            currentPager = 1;
        }
    }


    /***
     * 获取热门推荐，关注的群，创建的群 数据
     * @param isAdd  是否是上拉加载数据 true为添加数据
     * @param style  0 为获取热门推荐   1 为获取关注的群    2 为获取创建的群
     */
    private void getData(final boolean isAdd, int style) {
        String url = getResources().getString(R.string.url);
        String time = Utils.getSimpleDate();
        setCurrentPager(isAdd);
        String opcode;
        String checkData;

        /*****************添加参数********************/
        PostFormBuilder builder = OkHttpUtils.post().url(url)
                .addParams("pageNum", String.valueOf(currentPager))         //添加请求页码
                .addParams("numPerPage", "5")                               //一页的数量
                .addParams("submitDate", time)   ;                           //时间戳


        if (style == 0) {
            /**添加热门推荐的opcode*/
            opcode = getResources().getString(R.string.url_getHotGroupList);
            checkData = Utils.getcheckMD5(opcode, String.valueOf(currentPager), time);
            builder = builder.addParams("opcode", opcode)
                    .addParams("checkDate", checkData);                              //验证字段
        } else {
            if (style == 1) {
                /** 获取关注的群的opcode*/
                opcode = getResources().getString(R.string.url_getAttentionGroupList);
            } else {
                /** 获取创建的群的opcode*/
                opcode = getResources().getString(R.string.url_getCreateGroupList);
            }

            String localId = IMPrefsTools.getStringPrefs(getContext(), Constant.USER_ID, "");
            checkData = Utils.getcheckMD5(opcode, localId, time);
            builder = builder.addParams("opcode", opcode).addParams("userid", localId)
                    .addParams("checkDate", checkData);
        }

        /**********************发送请求并获得数据**************************/
        builder.build().execute(new ObjectCallBack<GroupBean>(GroupBean.class) {

            @Override
            public void onResponse(GroupBean response) {
                Logger.d(response.toString());
                refreshData(response.getGroupList(), isAdd);
            }
        });
    }


    /**
     * 获取群类型
     */
    private void getGroupType() {
        String time = Utils.getSimpleDate();
        String opcode = getResources().getString(R.string.url_getWxqGroupTypeListForIndex);
        String checkData = Utils.getcheckMD5(opcode, String.valueOf(currentPager), time);
        OkHttpUtils.post().url(getResources().getString(R.string.url)).addParams("opcode", opcode)
                .addParams("pageNum", "1").addParams("numPerPage", "100")
                .addParams("checkDate", checkData).addParams("submitDate", time)
                .build().execute(new ObjectCallBack<GroupTypeBean>(GroupTypeBean.class) {
            @Override
            public void onResponse(GroupTypeBean response) {
                kinds = response.getWxqTypeList();
                initViewPage();
            }
        });
    }
}

package com.jhlc.grouppurchase.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.tribe.IYWTribeService;
import com.jhlc.grouppurchase.IM.LoginSampleHelper;
import com.jhlc.grouppurchase.R;
import com.jhlc.grouppurchase.adapter.FindGroupListViewAdapter;
import com.jhlc.grouppurchase.beans.GroupBean;
import com.jhlc.grouppurchase.httpUtils.ObjectCallBack;
import com.jhlc.grouppurchase.util.DataVeri;
import com.jhlc.grouppurchase.util.Logger;
import com.jhlc.grouppurchase.util.QGApplication;
import com.jhlc.grouppurchase.util.Utils;
import com.jhlc.grouppurchase.view.RefreshListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/21.
 */
public class SearchGroupActivity extends Activity{

    public static String GroupName = "GroupName";
    public static String GroupType = "GroupType";
    private Integer grouptype;                      //储存群类型
    private String groupname;                       //储存群关键字

    private FindGroupListViewAdapter groupAdapter;
    private TextView groupName;
    private int currentPager;
    private List<GroupBean.GroupListEntity> groupList = new ArrayList<>();
    private RefreshListView listView;

    private ImageView search;
    private EditText searchInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_group);
        initView();
        initListener();
    }

    private void initView() {
        createGroup();
        initTitle();
        initListView();
        initSpinner();
    }

    private void initListener() {
        findViewById(R.id.ibtn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    /**
     * 初始化创建群组
     */
    private void createGroup() {
        ImageButton createGroup = (ImageButton) findViewById(R.id.ib_findgroup_add);

        View root = LayoutInflater.from(this).inflate(R.layout.pw_create_group, null);
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
                Intent intent = new Intent(SearchGroupActivity.this, CreateGroupActivity.class);
                startActivity(intent);
                popupWindow.dismiss();
            }
        });
    }

    private void initTitle() {
        searchInfo = (EditText) findViewById(R.id.et_findgroup_search);
        search = (ImageView) findViewById(R.id.iv_findgroup_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchInfo == null) return;
                String info = searchInfo.getText().toString().trim();
                if (!DataVeri.stringIsNull(info, "搜索信息")) {
                    groupname = info;
                    searchData(false);
                }
            }
        });

        groupName = (TextView) findViewById(R.id.tv_group_type);

        groupname = getIntent().getStringExtra(GroupName);
        grouptype = getIntent().getIntExtra(GroupType, -1);
        Logger.d(groupname + grouptype);

        if (grouptype < 0){
            groupName.setVisibility(View.GONE);
            searchInfo.setText(groupname);
        } else {
            groupName.setText(groupname);
        }
        searchData(false);

    }

    private void initListView() {
        listView = (RefreshListView) this.findViewById(R.id.search_bottom_listView);
        groupAdapter = new FindGroupListViewAdapter(this, groupList);
        listView.setAdapter(groupAdapter);

        /**
         * 上拉加载更多
         */
        listView.setOnLoadListener(new RefreshListView.OnLoadListener() {
            @Override
            public void onLoad() {
                searchData(true);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (position == groupList.size()) return;
                final YWIMKit imKit = LoginSampleHelper.getInstance().getIMKit();
                IYWTribeService tribeService = imKit.getTribeService();
                tribeService.joinTribe(new IWxCallback() {
                    @Override
                    public void onSuccess(Object... objects) {
                        //参数为群ID号
                        Intent intent = imKit.getTribeChattingActivityIntent(groupList.get(position).getTribeId());
                        startActivity(intent);
                    }

                    @Override
                    public void onError(int i, String s) {
                        //参数为群ID号
                        Intent intent = imKit.getTribeChattingActivityIntent(groupList.get(position).getTribeId());
                        startActivity(intent);
                    }

                    @Override
                    public void onProgress(int i) {

                    }
                }, groupList.get(position).getTribeId());

            }
        });
    }

    private void initSpinner() {
        String[] items = new String[1];
        items[0] = "全部";

        String[] items1 = new String[1];
        items1[0] = "排序";

        Spinner kind = (Spinner) findViewById(R.id.spinner_search_kind);
        kind.setAdapter(new ArrayAdapter<>(this,
                R.layout.simple_textview, items));

        Spinner sort = (Spinner) findViewById(R.id.spinner_search_sort);
        sort.setAdapter(new ArrayAdapter<>(this,
                R.layout.simple_textview, items1));
    }

    /**
     *  按条件搜寻结果
     */
    private void searchData(boolean isAddInfo) {
        if (isAddInfo) {
            currentPager++;
        } else {
            currentPager = 1;
            groupList.clear();
        }
        String time = Utils.getSimpleDate();
        String opcode = getResources().getString(R.string.url_getWxqGroupTypeListForIndex);
        String checkData = Utils.getcheckMD5(opcode, String.valueOf(grouptype), time);
        PostFormBuilder postFormBuilder = OkHttpUtils.post().url(getString(R.string.url)).addParams("opcode", opcode)
               .addParams("pageNum", String.valueOf(currentPager)).addParams("groupname", groupname)
                .addParams("numPerPage", "10").addParams("submitDate", time).addParams("checkDate", checkData);

        //如果是群类型搜索就添加群类型参数，否则添加群关键字参数
        if (grouptype > 0) {
            postFormBuilder = postFormBuilder.addParams("grouptype", String.valueOf(grouptype));
        }

        postFormBuilder.build()
                .execute(new ObjectCallBack<GroupBean>(GroupBean.class) {
                    @Override
                    public void onResponse(GroupBean response) {
                        QGApplication.getmApplication().showTextToast("搜索完成");
                        if (response.getGroupList() == null || response.getGroupList().size() == 0) {
                            listView.setStatus(RefreshListView.STATUES_OVER);
                        } else {
                            listView.setStatus(RefreshListView.STATUES_REFRESHING);
                            groupList.addAll(response.getGroupList());
                            groupAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}

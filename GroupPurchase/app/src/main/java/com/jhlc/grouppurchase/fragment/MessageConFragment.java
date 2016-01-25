package com.jhlc.grouppurchase.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jhlc.grouppurchase.R;
import com.jhlc.grouppurchase.activity.MainActivity;

/**
 * Created by LiCola on  2016/01/21  13:15
 */
public class MessageConFragment extends Fragment {
    private static final String TAG = "MessageConFragment";
    private static final String ARG_PARAM1 = "param1";

    private MainActivity mMainActivity;

    private String mParam1;

    public MessageConFragment() {

    }

    public static MessageConFragment newInstance(String param1) {
        MessageConFragment fragment = new MessageConFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity= (MainActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_message_connection,container,false);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getChildFragmentManager().beginTransaction().add(R.id.content,mMainActivity.mIMKit.getConversationFragment()).commit();

    }
}

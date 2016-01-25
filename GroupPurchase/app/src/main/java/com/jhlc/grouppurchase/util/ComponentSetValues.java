package com.jhlc.grouppurchase.util;

import android.view.View;
import android.widget.TextView;

import com.jhlc.grouppurchase.R;

/**
 * Created by Administrator on 2016/1/5.
 */
public class ComponentSetValues {

    public static void tvSetText(View view, int resource, String values) {
        TextView textView = (TextView) view.findViewById(resource);
        textView.setText(values);
    }
}

package com.jhlc.grouppurchase.httpUtils;

import com.google.gson.Gson;

import com.jhlc.grouppurchase.util.Logger;
import com.jhlc.grouppurchase.util.QGApplication;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/1/7.
 */
public abstract class ObjectCallBack<T> extends Callback<T> {

    private Class<T> clazz;                 //获取结果后将json转成bean

    public ObjectCallBack(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T parseNetworkResponse(Response response) throws IOException {
        Gson gson = new Gson();
        try {
            return gson.fromJson(response.body().string(), clazz);
        } catch (Exception e){
            /**
             * 当gson解析错误时，不再返回正确数据，抛出异常
             */
            QGApplication.getmApplication().showTextToast("数据解析错误");
            e.printStackTrace();
            throw new IOException("gson解析异常");
        }
    }

    @Override
    public void onError(Request request, Exception e) {
        QGApplication.getmApplication().showTextToast("请求数据失败");
    }
}

package com.jhlc.grouppurchase.httpUtils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jhlc.grouppurchase.R;

/**
 * Created by LiCola on  2015/12/30  15:27
 *
 *fresco的构造类 方便图片的统一管理
 */
public class FrescoBuilder {

    /**
     * 配置SimpleDraweeView 并开始加载图片
     * 弃用 use ImageLoadFresco
     * @param context
     * @param mSimpleDraweeView
     * @param url
     * @param isCircle
     */
    public static void setHeadDrawableMC2V(Context context, final SimpleDraweeView mSimpleDraweeView, String url, boolean isCircle){
       new ImageLoadFresco.LoadImageFrescoBuilder(context, mSimpleDraweeView, url)
               .setFailureImage(context.getResources().getDrawable(R.drawable.people_gray)).build();
    }


    /**
     * 根据布尔值设置 图片为圆角或者圆圈
     * @param isCircle true就是圆圈 否则就是固定值得圆角
     * @return RoundingParams对象
     */
    private static RoundingParams getRoundCircleParams(boolean isCircle) {
        RoundingParams roundingParams=null;
        if (isCircle){
            roundingParams= RoundingParams.asCircle();
        }else {
            roundingParams= RoundingParams.fromCornersRadius(10);
        }
        return roundingParams;
    }
}

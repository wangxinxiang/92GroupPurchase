package com.jhlc.grouppurchase.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.WindowManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by LiCola on  2016/01/06  12:23
 */
public class Utils {
    private static final String TAG = "Utils";

    public static int getScreenWidth(Context context){
        return getPoint(context).x;
    }

    public static int getScreenHeight(Context context){
        return getPoint(context).y;
    }

    @NonNull
    private static Point getPoint(Context context) {
        WindowManager wm= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display=wm.getDefaultDisplay();
        Point point=new Point();
        display.getSize(point);
        return point;
    }

    public static int dp2px(Context context,float dpValue){
        final float scale=context.getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
    }

    public static int px2dp(Context context,float pxValue){
        final float scale=context.getResources().getDisplayMetrics().density;
        return (int)(pxValue/scale+0.5f);
    }


    public static String getCurrenTime(){
        SimpleDateFormat format=new SimpleDateFormat("yyyyMMddhhmmss");
        Date date=new Date(System.currentTimeMillis());
        return format.format(date);
    }

    public static String getSimpleDate(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss", Locale.SIMPLIFIED_CHINESE);
        return format.format(new Date());
    }

    /**
     * @return 解析当前时间为中文时间
     */
    public static String parseTime(String time) {
        if (time == null) return "";
        Logger.d(time);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日 HH:mm:ss", Locale.CHINA);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFormat.format(date);
    }

    /**
     * Try to return the absolute file path from the given Uri
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePath( final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * MD5加密，获取checkDate
     * @param opcode 要加密的opcode
     * @param firstParam 请求的第一个参数
     * @param date  时间戳
     * @return  checkDate
     */
    public static String getcheckMD5(String opcode, String firstParam, String date) {
        //用户ID+充值账号-时间*125
        StringBuffer buffer = new StringBuffer();
        buffer.append(opcode);
        buffer.append(firstParam);
        buffer.append(date);

        return MD5.getMD5Lower(buffer.toString());
    }
}

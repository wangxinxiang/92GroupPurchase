package com.jhlc.grouppurchase.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.alibaba.wxlib.util.SharedPreferencesCompat;

import java.util.Set;

/**
 * Created by Administrator on 2016/1/11.
 */
public class PreferencesUtils {
    private static SharedPreferences defalultSprefs = null;

    private static SharedPreferences getDefaultPreferences(Context context) {
        if(defalultSprefs != null) {
            return defalultSprefs;
        } else {
            defalultSprefs = context.getSharedPreferences("92qg", 0);
            return defalultSprefs;
        }
    }

    public static void setLongPrefs(Context context, String key, long value) {
        SharedPreferences.Editor editor = getDefaultPreferences(context).edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static long getLongPrefs(Context context, String key) {
        return getLongPrefs(context, key, 0L);
    }

    public static long getLongPrefs(Context context, String key, long defaultValue) {
        return getDefaultPreferences(context).getLong(key, defaultValue);
    }

    public static void setBooleanPrefs(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = getDefaultPreferences(context).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBooleanPrefs(Context context, String key) {
        return getBooleanPrefs(context, key, false);
    }

    public static boolean getBooleanPrefs(Context context, String key, boolean defaultValue) {
        return getDefaultPreferences(context).getBoolean(key, defaultValue);
    }

    public static void setStringPrefs(Context context, String key, String value) {
        if(!TextUtils.isEmpty(key) && value != null) {
            SharedPreferences.Editor editor = getDefaultPreferences(context).edit();
            editor.putString(key, value);
            editor.apply();
        }
    }

    public static String getStringPrefs(Context context, String key) {
        return getStringPrefs(context, key, "");
    }

    public static String getStringPrefs(Context context, String key, String defaultValue) {
        return getDefaultPreferences(context).getString(key, defaultValue);
    }

    public static void removePrefs(Context context, String key) {
        SharedPreferences.Editor editor = getDefaultPreferences(context).edit();
        editor.remove(key);
        editor.apply();
    }

    public static void setIntPrefs(Context context, String key, int value) {
        SharedPreferences.Editor editor = getDefaultPreferences(context).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getIntPrefs(Context context, String key) {
        return getIntPrefs(context, key, 0);
    }

    public static int getIntPrefs(Context context, String key, int defaultValue) {
        return getDefaultPreferences(context).getInt(key, defaultValue);
    }

    public static void setStringSetValue(Context context, String key, Set<String> value) {
        SharedPreferences.Editor editor = getDefaultPreferences(context).edit();
        editor.putStringSet(key, value);
        editor.apply();
    }

    public static Set<String> getStringSetValue(Context context, String key) {
        return getDefaultPreferences(context).getStringSet(key, (Set) null);
    }
}

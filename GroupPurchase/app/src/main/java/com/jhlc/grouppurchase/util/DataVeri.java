package com.jhlc.grouppurchase.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by licheng on 17/7/15.
 */
public class DataVeri {

    public static Boolean isNaN(String array, String name) {
        if ("".equals(array)) {
            QGApplication.getmApplication().showTextToast(name + "数字为空");
            return false;
        }
        if (array.matches("^[0-9]*$") || array.matches("\\d+\\.\\d+"))
            return true;
        else {
            QGApplication.getmApplication().showTextToast(name + "只能输入纯数字");
            return false;
        }

    }

    public static Boolean isAge(String array) {
        if ("".equals(array)) {
            QGApplication.getmApplication().showTextToast("年龄不能为空");
            return false;
        } else {
            if (Integer.valueOf(array) >= 16 && Integer.valueOf(array) <= 110) {
                return true;
            } else {
                QGApplication.getmApplication().showTextToast("年龄输入有误");
                return false;
            }

        }
    }


    public static Boolean isMobileNum(String array) {
        if ("".equals(array)) {
            QGApplication.getmApplication().showTextToast("手机号不能为空");
            return false;
        } else {
            if (array.matches("^((13[0-9])|(17[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$")) {
                return true;
            } else {
                QGApplication.getmApplication().showTextToast("手机号格式错误");
                return false;
            }

        }
    }

    public static Boolean isIDNum(String array) {
        if ("".equals(array)) {
            QGApplication.getmApplication().showTextToast("身份证号码不能为空");
            return false;
        } else {
            if (array.matches("/^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$/")) {
                return true;
            } else {
                QGApplication.getmApplication().showTextToast("身份证号码格式错误");
                return false;
            }
        }
    }

    public static Boolean stringIsNull(String str, String name) {
        if (str==null || "".equals(str)) {
            QGApplication.getmApplication().showTextToast(name + "不能为空");
            return true;
        } else {
            return false;
        }
    }

    public static boolean isBlank(String str){
        return str == null || "".equals(str);
    }

    public static String getLoan_type(String i) {
        switch (i) {
            case "1":
                return "车贷";
            case "2":
                return "房贷";
            case "3":
                return "信用贷";
            default:
                return "其他";
        }
    }

    public static String getMoneyFromDouble(String account) {
        double money = Double.valueOf(account);
        int money1 = (int) money;
        return money1 + "";
    }

    /**
     17.     * 电话号码验证
     18.     *
     19.     * @param  str
     20.     * @return 验证通过返回true
     21.     */

    public static boolean isPhone(String str) {
        Pattern p1 = null, p2 = null;
        Matcher m = null;
        boolean b = false;
        p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$");  // 验证带区号的
        p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");         // 验证没有区号的
        if (str.length() > 9) {
            m = p1.matcher(str);
            b = m.matches();
        } else {
            m = p2.matcher(str);
            b = m.matches();
        }
        return b;
    }

    public static boolean compare_date(String DATE1, String DATE2) {
        if (DATE1 == null || "".equals(DATE1) || DATE2 == null || "".equals(DATE2)) {
            QGApplication.getmApplication().showTextToast("时间为空");
            return true;
        }
        boolean flag = false;

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                QGApplication.getmApplication().showTextToast("欠款日期应在还款日期前");
                flag = true;
            } else if (dt1.getTime() < dt2.getTime()) {
                flag = false;
            } else {
                flag = false;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return flag;
    }

}

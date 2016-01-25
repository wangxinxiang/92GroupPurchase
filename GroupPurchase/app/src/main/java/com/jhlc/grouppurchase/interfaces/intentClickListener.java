package com.jhlc.grouppurchase.interfaces;

import java.util.List;

/**
 * Created by LiCola on  2015/12/30  19:47
 */
public interface intentClickListener {
    void onClickContact(String userId);

    void onClickBusiness(String userId);

    void onPostGoodComplete(String type, String userId, String userInput, List<String> urlList);

    void onPostOrderComplete(String type, String userId, String userInput, List<String> urlList, String order, String price);
}

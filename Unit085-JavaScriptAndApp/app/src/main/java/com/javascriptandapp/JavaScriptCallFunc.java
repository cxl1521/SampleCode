package com.javascriptandapp;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class JavaScriptCallFunc {

    Context mContext;

    JavaScriptCallFunc(Context c) {
        mContext = c;
    }

    @JavascriptInterface
    public void showToastMsg(String s) {
        Toast.makeText(mContext, s, Toast.LENGTH_LONG)
                .show();
    }

}

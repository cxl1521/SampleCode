package com.webbrowser;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class MyWebViewClient extends WebViewClient {

    private Activity mActivity;
    private Button mBtnGoBack,
            mBtnGoForward,
            mBtnStop,
            mBtnReload;
    private WebView mWebView;

    public MyWebViewClient setupViewComponent(Activity act,
                                              WebView webView,
                                              Button btnGoBack,
                                              Button btnGoForward,
                                              Button btnReload,
                                              Button btnStop) {
        mActivity = act;
        mWebView = webView;
        mBtnGoBack = btnGoBack;
        mBtnGoForward = btnGoForward;
        mBtnReload = btnReload;
        mBtnStop = btnStop;

        return this;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        if (Uri.parse(request.getUrl().toString()).getHost().indexOf("google") >= 0) {
            return false;
        }

        Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(request.getUrl().toString()));
        mActivity.startActivity(it);
        return true;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        // 在標題列顯示橫式進度列，或是環狀等待迴圈。
        mActivity.setTitle("正在下載網頁...");
        mBtnReload.setEnabled(false);
        mBtnStop.setEnabled(true);
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        mActivity.setTitle(R.string.app_name);
        mBtnReload.setEnabled(true);
        mBtnStop.setEnabled(false);

        if (mWebView.canGoBack())
            mBtnGoBack.setEnabled(true);
        else
            mBtnGoBack.setEnabled(false);

        if (mWebView.canGoForward())
            mBtnGoForward.setEnabled(true);
        else
            mBtnGoForward.setEnabled(false);

        super.onPageFinished(view, url);
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        mBtnReload.setEnabled(true);
        mBtnStop.setEnabled(false);

        Toast.makeText(mActivity, "開啟網頁錯誤：" + error.toString(),
                Toast.LENGTH_LONG)
                .show();

        super.onReceivedError(view, request, error);
    }
}

package com.javascriptandapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import java.net.URLDecoder;

public class MainActivity extends AppCompatActivity
                            implements View.OnClickListener {

    private Button mBtnLoadHtml,
            mBtnShowImage,
            mBtnBuildHtml;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnLoadHtml = (Button)findViewById(R.id.btnLoadHtml);
        mBtnShowImage = (Button)findViewById(R.id.btnShowImage);
        mBtnBuildHtml = (Button)findViewById(R.id.btnBuildHtml);
        mWebView = (WebView)findViewById(R.id.webView);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mWebView.addJavascriptInterface(new JavaScriptCallFunc(this), "Android");

        mBtnLoadHtml.setOnClickListener(this);
        mBtnShowImage.setOnClickListener(this);
        mBtnBuildHtml.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLoadHtml:
                mWebView.loadUrl("file:///android_asset/my_web_page.html");
                break;
            case R.id.btnShowImage:
                mWebView.loadUrl("javascript:showImage()");
                break;
            case R.id.btnBuildHtml:
                String sHtml = null;
                try {
                    sHtml = URLDecoder.decode("<html>" +
                        "<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">" +
                        "<body>這是由程式碼建立的網頁。</body>" +
                        "</html>", "utf-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 在有些版本的Android平台，loadData()無法正常顯示中文，
                // 如果遇到這種情況，可以換成使用loadDataWithBaseURL()。
//                mWebView.loadData(sHtml, "text/html", "utf-8");
    			mWebView.loadDataWithBaseURL(null, sHtml, "text/html", "utf-8", null);

                break;
        }
    }
}

package com.webbrowser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
                            implements View.OnClickListener {

    private Button mBtnOpenUrl,
                    mBtnGoBack,
                    mBtnGoForward,
                    mBtnStop,
                    mBtnReload;
    private EditText mEdtUrl;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnOpenUrl = (Button)findViewById(R.id.btnOpenUrl);
        mBtnGoBack = (Button)findViewById(R.id.btnGoBack);
        mBtnGoForward = (Button)findViewById(R.id.btnGoForward);
        mBtnStop = (Button)findViewById(R.id.btnStop);
        mBtnReload = (Button)findViewById(R.id.btnReload);
        mEdtUrl = (EditText)findViewById(R.id.edtUrl);
        mWebView = (WebView)findViewById(R.id.webView);

        // 使用自訂的 MyWebViewClient 可以篩選在程式中
        // 瀏覽的網頁，或是啟動外部的瀏覽器。
        mWebView.setWebViewClient(new MyWebViewClient()
                .setupViewComponent(this,
                        mWebView,
                        mBtnGoBack,
                        mBtnGoForward,
                        mBtnReload,
                        mBtnStop));
        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress >= 100)
                    Toast.makeText(MainActivity.this, "網頁下載完成",
                            Toast.LENGTH_SHORT)
                            .show();
            }
        });

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);

        mBtnOpenUrl.setOnClickListener(this);
        mBtnGoBack.setOnClickListener(this);
        mBtnGoForward.setOnClickListener(this);
        mBtnStop.setOnClickListener(this);
        mBtnReload.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnOpenUrl:
                mWebView.loadUrl(mEdtUrl.getText().toString());
                break;
            case R.id.btnGoBack:
                mWebView.goBack();
                mEdtUrl.setText(mWebView.getUrl());
                break;
            case R.id.btnGoForward:
                mWebView.goForward();
                mEdtUrl.setText(mWebView.getUrl());
                break;
            case R.id.btnReload:
                mWebView.reload();
                break;
            case R.id.btnStop:
                mWebView.stopLoading();

                setTitle(R.string.app_name);
                mBtnReload.setEnabled(true);
                mBtnStop.setEnabled(false);
                break;
        }
    }
}

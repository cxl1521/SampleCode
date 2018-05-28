package com.webview;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button mBtnOpenUrl;
    private EditText mEdtUrl;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnOpenUrl = (Button)findViewById(R.id.btnOpenUrl);
        mEdtUrl = (EditText)findViewById(R.id.edtUrl);
        mWebView = (WebView)findViewById(R.id.webView);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);   // 開啟Java Script 解譯功能

        // 設定轉址的網頁還是由WebView開啟，不要用外部的瀏覽器。
        mWebView.setWebViewClient(new WebViewClient());

        mBtnOpenUrl.setOnClickListener(btnOpenUrlOnClick);
    }

    private View.OnClickListener btnOpenUrlOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mWebView.loadUrl(mEdtUrl.getText().toString());
        }
    };
}

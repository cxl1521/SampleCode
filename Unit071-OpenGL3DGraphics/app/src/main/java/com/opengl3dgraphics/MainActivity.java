package com.opengl3dgraphics;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    MyGLSurfaceView mMyGLSurfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 檢查手機是否支援OpenGL ES 2.0
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

        if (supportsEs2) {
            // 用MyGLSurfaceView物件當成App畫面
            mMyGLSurfView = new MyGLSurfaceView(this);
            mMyGLSurfView.setEGLContextClientVersion(2);
            mMyGLSurfView.setRenderer(mMyGLSurfView);
            setContentView(mMyGLSurfView);
        }
        else
            setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMyGLSurfView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMyGLSurfView.onPause();
    }
}

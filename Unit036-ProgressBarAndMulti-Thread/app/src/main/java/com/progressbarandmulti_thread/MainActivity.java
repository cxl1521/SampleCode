package com.progressbarandmulti_thread;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    // 自己建立一個靜態的Handler類別。
    private static class StaticHandler extends Handler {
        private final WeakReference<MainActivity> mActivity;

        public StaticHandler(MainActivity activity) {
            mActivity = new WeakReference<MainActivity>(activity);
        }
    }

    // 使用自己建立的靜態Handler以避免出現記憶體漏洞。
    public final StaticHandler mHandler = new StaticHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

        DoLengthyWork work = new DoLengthyWork();
        work.setHandler(mHandler);
        work.setProgressBar(progressBar);
        work.start();
    }
}

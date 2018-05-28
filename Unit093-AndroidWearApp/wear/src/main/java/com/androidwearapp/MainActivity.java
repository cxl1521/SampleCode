package com.androidwearapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView mTextView;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
                mButton = (Button) stub.findViewById(R.id.button);
                mButton.setOnClickListener(buttonOnClick);
            }
        });
    }

    private View.OnClickListener buttonOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mTextView.setText("你按下按鈕！");
        }
    };
}

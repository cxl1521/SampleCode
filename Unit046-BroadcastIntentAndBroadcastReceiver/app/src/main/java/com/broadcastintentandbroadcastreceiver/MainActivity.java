package com.broadcastintentandbroadcastreceiver;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button mBtnRegReceiver,
                    mBtnUnregReceiver,
                    mBtnSendBroadcast1,
                    mBtnSendBroadcast2;

    private MyBroadcastReceiver2 mMyReceiver2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnRegReceiver = (Button)findViewById(R.id.btnRegReceiver);
        mBtnUnregReceiver = (Button)findViewById(R.id.btnUnregReceiver);
        mBtnSendBroadcast1 = (Button)findViewById(R.id.btnSendBroadcast1);
        mBtnSendBroadcast2 = (Button)findViewById(R.id.btnSendBroadcast2);

        mBtnRegReceiver.setOnClickListener(btnRegReceiverOnClick);
        mBtnUnregReceiver.setOnClickListener(btnUnregReceiverOnClick);
        mBtnSendBroadcast1.setOnClickListener(btnSendBroadcast1OnClick);
        mBtnSendBroadcast2.setOnClickListener(btnSendBroadcast2OnClick);
    }

    private View.OnClickListener btnRegReceiverOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            IntentFilter itFilter = new IntentFilter("com.android.MY_BROADCAST2");
            mMyReceiver2 = new MyBroadcastReceiver2();
            registerReceiver(mMyReceiver2, itFilter);
        }
    };

    private View.OnClickListener btnUnregReceiverOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            unregisterReceiver(mMyReceiver2);
        }
    };

    private View.OnClickListener btnSendBroadcast1OnClick = new View.OnClickListener() {
        public void onClick(View v) {
            Intent it = new Intent("com.android.MY_BROADCAST1");
            it.putExtra("sender_name", "主程式");
            sendBroadcast(it);
        }
    };

    private View.OnClickListener btnSendBroadcast2OnClick = new View.OnClickListener() {
        public void onClick(View v) {
            Intent it = new Intent("com.android.MY_BROADCAST2");
            it.putExtra("sender_name", "主程式");
            sendBroadcast(it);
        }
    };
}

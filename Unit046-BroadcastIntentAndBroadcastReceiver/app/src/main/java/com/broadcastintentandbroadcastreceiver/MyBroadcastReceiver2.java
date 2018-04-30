package com.broadcastintentandbroadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyBroadcastReceiver2 extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String sender = intent.getStringExtra("sender_name");
        Toast.makeText(context, "BroadcastReceiver2收到" + sender + "發送的Broadcast訊息",
                Toast.LENGTH_LONG).show();
    }
}

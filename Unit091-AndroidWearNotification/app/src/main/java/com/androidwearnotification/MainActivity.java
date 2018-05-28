package com.androidwearnotification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.BigTextStyle;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private static final int NOTI_ID = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Android Wear的Action按鈕使用的Intent和action。
        Intent itPhoneCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "123456789"));
        PendingIntent penItPhoneCall = PendingIntent.getActivity(getApplicationContext(),
                0, itPhoneCall, PendingIntent.FLAG_CANCEL_CURRENT);

        // Android Wear的Action按鈕使用的action。
        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(
                        android.R.drawable.ic_menu_call, "打電話", penItPhoneCall)
                        .build();

        // 建立WearableExtender物件，設定好屬性
        NotificationCompat.WearableExtender wearableExtender =
                new NotificationCompat.WearableExtender();
        wearableExtender.addAction(action);
        wearableExtender.setBackground(
                BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));

        Notification secondNotiMsg = new NotificationCompat.Builder(this)
                .setContentTitle("第二頁")
                .setContentText("第二頁的說明。")
                .build();
        wearableExtender.addPage(secondNotiMsg);

        Notification noti = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.btn_star_big_on)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        android.R.drawable.ic_menu_camera))
                .setTicker("訊息提示文字")
                .setContentTitle("訊息標題")
                .setContentText("訊息內容")
                .extend(wearableExtender)	// 設定Android Wear裝置專用的訊息內容。
                .build();

        NotificationManagerCompat notiMgr =
                NotificationManagerCompat.from(getApplicationContext());

        notiMgr.notify(NOTI_ID, noti);	// NOTI_ID 是程式中定義的常數。
    }
}

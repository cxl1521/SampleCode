package com.advancedappwidget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RemoteViews;

import java.util.Calendar;

public class AppWidgetConfigActivity extends AppCompatActivity {

    int mAppWidgetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_widget_config);

        Intent itIn = getIntent();
        Bundle extras = itIn.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        Intent itOut = new Intent("com.android.MY_OWN_WIDGET_UPDATE");
        PendingIntent penIt = PendingIntent.getBroadcast(this, 0, itOut, 0);
        AlarmManager alarmMan = (AlarmManager)getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 5);
        alarmMan.setRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), 20*1000, penIt);

        MyAppWidget.SaveAlarmManager(alarmMan, penIt);

        // 以下是新加入的程式碼。
//        RemoteViews viewAppWidget = new RemoteViews(getPackageName(),
//                R.layout.app_widget);
//        viewAppWidget.setImageViewResource(R.id.imgViewAppWidget,
//                R.drawable.app_widget_icon);
//        AppWidgetManager appWidgetMan = AppWidgetManager.getInstance(this);
//        appWidgetMan.updateAppWidget(mAppWidgetId, viewAppWidget);

        Intent itAppWidgetConfigResult = new Intent();
        itAppWidgetConfigResult.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                mAppWidgetId);
        setResult(RESULT_OK, itAppWidgetConfigResult);

        finish();
    }
}

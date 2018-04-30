package com.appwidgetwithtimer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

public class MyAppWidget extends AppWidgetProvider {

    private static final String LOG_TAG = "MyAppWidget";

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.d(LOG_TAG, "onDeleted()");
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.d(LOG_TAG, "onDisabled()");
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d(LOG_TAG, "onEnabled()");

        Calendar alarmTime = Calendar.getInstance();
        alarmTime.setTimeInMillis(System.currentTimeMillis());
        alarmTime.add(Calendar.SECOND, 30);
        setAlarm(context, alarmTime);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if(!intent.getAction().equals("com.android.MY_OWN_WIDGET_UPDATE"))
            return;

        Log.d(LOG_TAG, "onReceive()");

        AppWidgetManager appWidgetMan = AppWidgetManager.getInstance(context);
        ComponentName thisAppWidget = new ComponentName(context.getPackageName(),
                MyAppWidget.class.getName());
        int[] appWidgetIds = appWidgetMan.getAppWidgetIds(thisAppWidget);

        onUpdate(context, appWidgetMan, appWidgetIds);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.d(LOG_TAG, "onUpdate()");
    }

    private void setAlarm(Context context, Calendar alarmTime) {
        Intent it = new Intent("com.android.MY_OWN_WIDGET_UPDATE");
        PendingIntent penIt = PendingIntent.getBroadcast(context, 0, it, 0);
        AlarmManager alarmMan =
                (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
        alarmMan.set(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), penIt);
    }
}

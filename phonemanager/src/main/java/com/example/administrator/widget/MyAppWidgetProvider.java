package com.example.administrator.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.format.Formatter;
import android.widget.RemoteViews;

import com.example.administrator.phonemanager.R;
import com.example.administrator.utils.ProcessUtils;

/**
 * Created by Administrator on 2016/4/3.
 */
public class MyAppWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //跨进程的更新
        //告诉系统要去更新那个widget
        ComponentName name = new ComponentName(context, MyAppWidgetProvider.class);
        RemoteViews remoteview = new RemoteViews("com.example.administrator.phonemanager", R.layout.processmanager_appwidget);//远程视图

        long availableRam = ProcessUtils.getAvailableRam(context);
        remoteview.setTextViewText(R.id.tv_processwidget_memory, "可用内存" + Formatter.formatFileSize(context,availableRam) );
        int runningProcessCount = ProcessUtils.getRunningProcessCount(context);
        remoteview.setTextViewText(R.id.tv_processwidget_count, "总进程数" + runningProcessCount);
        Intent intent = new Intent("com.example.administrator.widgetupdate");
        PendingIntent pdintent = PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_CANCEL_CURRENT);//PendingIntent为阻塞式intent 100为响应吗
        remoteview.setOnClickPendingIntent(R.id.btn_widget_clear,pdintent);
        appWidgetManager.updateAppWidget(name,remoteview);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

}

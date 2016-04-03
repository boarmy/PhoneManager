package com.example.administrator.application;

import android.app.ActivityManager;
import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.text.format.Formatter;
import android.widget.RemoteViews;

import com.example.administrator.bean.ProcessInfo;
import com.example.administrator.phonemanager.R;
import com.example.administrator.service.MyNumberLocationService;
import com.example.administrator.utils.ProcessUtils;
import com.example.administrator.widget.MyAppWidgetProvider;

import java.util.List;

/**
 * Created by Administrator on 2016/3/25.
 */
//本机的IP全局变量
public class MyApplication extends Application {
    public static String SERVER_PATH;//定义了从服务器上获取更新文件的路径 里面包含URL
    public static SharedPreferences configsp;//sharedpreferences 为一个轻量级的数据库奥 亲
    public static SharedPreferences.Editor editor;
    private MyReveiver myReveiver;
    @Override
    public void onCreate() {
        super.onCreate();
        SERVER_PATH = "http://192.168.3.31/Aday10Network/";
        configsp = getSharedPreferences("config", MODE_PRIVATE);
        editor = configsp.edit();
        if (configsp.getBoolean("showloaction", false)) {
            startService(new Intent(this, MyNumberLocationService.class));
        }

        //在此注册桌面小工具的广播接收者
        IntentFilter filter = new IntentFilter("com.example.administrator.widgetupdate");//IntentFilter 过滤器
        myReveiver = new MyReveiver();//new一个MyReveiver之后就会执行MyReveiver中的代码了  从而达到更新的目的
        getApplicationContext().registerReceiver(myReveiver, filter);//动态注册这个广播接收者
    }

    //设置数据并销毁这个界面
    public static void setConfigValue(String key, String value) {

        editor.putString(key, value);
        editor.commit();
    }

    public static void setConfigValue(String key, int value) {

        editor.putInt(key, value);
        editor.commit();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        stopService(new Intent(this, MyNumberLocationService.class));

    }

    //为桌面小工具注册的广播接收者用于接收桌面小工具发来的广播（MyAppWidgetProvider） 在这里进行数据的更新
    class MyReveiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //第一步，先kill后台进程
            ActivityManager ams = (ActivityManager) getSystemService(ACTIVITY_SERVICE);//得到一个活动管理者
            List<ProcessInfo> userprocessInfoList = ProcessUtils.getAllProcInfo(getApplicationContext());//得到所有程序
            //把除自己以外的进程全部干掉
            for (ProcessInfo pp : userprocessInfoList) {
                if (pp.getPackagename().equals(getPackageName())) {
                    continue;
                }
                ams.killBackgroundProcesses(pp.getPackagename());//就杀掉这个应用
            }
            //第二部，更新widget
            final AppWidgetManager instance = AppWidgetManager.getInstance(context);//得到一个桌面小工具的管理者
            ComponentName name = new ComponentName(context,MyAppWidgetProvider.class);//得到那个小组件的名称
            RemoteViews remoteview =  new RemoteViews("com.example.administrator.phonemanager", R.layout.processmanager_appwidget);//得道那个小部件的远程视图

            final long availableRam = ProcessUtils.getAvailableRam(context);
            remoteview.setTextViewText(R.id.tv_processwidget_memory, "可用内存" + Formatter.formatFileSize(context, availableRam));

            final int runningProcessCount = ProcessUtils.getRunningProcessCount(context);
            remoteview.setTextViewText(R.id.tv_processwidget_count, "总进程数" + runningProcessCount);

            instance.updateAppWidget(name, remoteview);//将小组件的名称和更新后的远程视图通过小工具的管理者发过去
        }
    }
}

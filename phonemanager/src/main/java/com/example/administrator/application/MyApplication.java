package com.example.administrator.application;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.administrator.service.MyNumberLocationService;

/**
 * Created by Administrator on 2016/3/25.
 */
//本机的IP全局变量
public class MyApplication extends Application {
    public static String SERVER_PATH;//定义了从服务器上获取更新文件的路径 里面包含URL
    public static SharedPreferences configsp;//sharedpreferences 为一个轻量级的数据库奥 亲
    public static SharedPreferences.Editor editor;
    @Override
    public void onCreate() {
        super.onCreate();
        SERVER_PATH = "http://192.168.3.31/Aday10Network/";
        configsp = getSharedPreferences("config", MODE_PRIVATE);
        editor=configsp.edit();
        startService(new Intent(this, MyNumberLocationService.class));
    }
    //设置数据并销毁这个界面
    public static void setConfigValue(String key, String value){

        editor.putString(key,value);
        editor.commit();
    }
    public static void setConfigValue(String key, int value){

        editor.putInt(key,value);
        editor.commit();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        stopService(new Intent(this, MyNumberLocationService.class));

    }
}

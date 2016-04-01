package com.example.administrator.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2016/3/31.
 */
//用来存放所有安装应用的信息 应用名 应用的图片 应用的安装位置等
public class AppInfo {
    String appname;
    Drawable icon;
    boolean isSdcard;//true 表示装在sdcard false 表示装在ROM中
    boolean isSystem;//true 表示系统应用， false 表示用户自己安装的应用
    String packagename;

    public String getPackagename() {
        return packagename;
    }

    public AppInfo(String appname, Drawable icon, boolean isSdcard, boolean isSystem, String packagename) {
        this.appname = appname;
        this.icon = icon;
        this.isSdcard = isSdcard;
        this.isSystem = isSystem;
        this.packagename = packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public boolean isSdcard() {
        return isSdcard;
    }

    public void setIsSdcard(boolean isSdcard) {
        this.isSdcard = isSdcard;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setIsSystem(boolean isSystem) {
        this.isSystem = isSystem;
    }

    public AppInfo(String appname, Drawable icon, boolean isSdcard, boolean isSystem) {
        this.appname = appname;
        this.icon = icon;
        this.isSdcard = isSdcard;
        this.isSystem = isSystem;
    }

    public AppInfo() {
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "appname='" + appname + '\'' +
                ", icon=" + icon +
                ", isSdcard=" + isSdcard +
                ", isSystem=" + isSystem +
                '}';
    }
}

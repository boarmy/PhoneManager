package com.example.administrator.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2016/4/2.
 */
public class ProcessInfo {
    Drawable appicon;
    String appname;
    long appram;
    String packagename;
    boolean isSystem;
    boolean isCheck;

    public boolean isSystem() {
        return isSystem;
    }

    public void setIsSystem(boolean isSystem) {
        this.isSystem = isSystem;
    }

    @Override
    public String toString() {
        return "ProcessInfo{" +
                "appname='" + appname + '\'' +
                ", appicon=" + appicon +
                ", appram=" + appram +
                ", packagename='" + packagename + '\'' +
                ", isCheck=" + isCheck +
                '}';
    }

    public ProcessInfo(Drawable appicon, String appname, long appram, String packagename, boolean isSystem) {
        this.appicon = appicon;
        this.appname = appname;
        this.appram = appram;
        this.packagename = packagename;
        this.isSystem = isSystem;
        isCheck=false;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public Drawable getAppicon() {
        return appicon;
    }

    public void setAppicon(Drawable appicon) {
        this.appicon = appicon;
    }

    public long getAppram() {
        return appram;
    }

    public void setAppram(long appram) {
        this.appram = appram;
    }

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }
}

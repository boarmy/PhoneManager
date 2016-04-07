package com.example.administrator.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import com.example.administrator.bean.AppInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/31.
 */
//应用管理的工具类
public class PackageUtils {
    //获取sdkcard的可用空间
    public static long getAvailableSDcardSize() {
        File SDcard = Environment.getExternalStorageDirectory();
        StatFs statFs = new StatFs(SDcard.getAbsolutePath());
        long availableBlocks;
        long blockSize;
        if (Build.VERSION.SDK_INT >= 18) {//如果sdk版本在18以上则用下面的 因为之下的没有这函数
            availableBlocks = statFs.getAvailableBlocksLong();
            blockSize = statFs.getBlockSizeLong();
        } else {
            availableBlocks = statFs.getAvailableBlocks();
            blockSize = statFs.getBlockSize();
        }
        return availableBlocks * blockSize;
    }

    //获取手机内存的剩余空间
    public static long getAvailableROMSize() {
        File SDcard = Environment.getDataDirectory();
        StatFs statFs = new StatFs(SDcard.getAbsolutePath());
        long availableBlocks;
        long blockSize;
        if (Build.VERSION.SDK_INT >= 18) {//如果sdk版本在18以上则用下面的 因为之下的没有这函数
            availableBlocks = statFs.getAvailableBlocksLong();
            blockSize = statFs.getBlockSizeLong();
        } else {
            availableBlocks = statFs.getAvailableBlocks();
            blockSize = statFs.getBlockSize();
        }
        return availableBlocks * blockSize;
    }

    //用于获取应用的所有信息
    public static List<AppInfo> getAllAppInfo(Context ctx) {
        List<AppInfo> appinfolist = new ArrayList<AppInfo>();//存放信息
        PackageManager packageManager = ctx.getPackageManager();// 得到系统的应用管理器（包管理器）
        final List<ApplicationInfo> installedApplications = packageManager.getInstalledApplications(0);//用这个包管理器就可以得到所有安装应用的信息（应用名称，图片，以及安装在内存卡还是sd卡上等信息）
        for (ApplicationInfo appinfo : installedApplications) {
            //应用名字，应用图片，应用是否安装在SDcard上
            // appinfo.packageName;
            String packagename = appinfo.packageName;
            final CharSequence lable = appinfo.loadLabel(packageManager); //应用名
            final Drawable icon = appinfo.loadIcon(packageManager); //应用图片
            boolean isSDCARD;
            boolean isSystem;

            if ((appinfo.flags & appinfo.FLAG_SYSTEM) != 0) //=   1 | 4;
            {
                isSystem = true;//系统应用
            } else {
                isSystem = false;//用户自己安装的应用
            }

            if ((appinfo.flags & appinfo.FLAG_EXTERNAL_STORAGE) != 0) //=   1 | 4;
            {
                isSDCARD = true;//SDCARD
            } else {
                isSDCARD = false;//非SDcard安装
            }
            AppInfo appInfo = new AppInfo(lable.toString(), icon, isSDCARD, isSystem,packagename);
            appinfolist.add(appInfo);
        }
        return appinfolist;

    }
}

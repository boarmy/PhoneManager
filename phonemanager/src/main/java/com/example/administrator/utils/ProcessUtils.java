package com.example.administrator.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Debug;

import com.example.administrator.bean.ProcessInfo;
import com.example.administrator.phonemanager.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/2.
 */
public class ProcessUtils {

    //当前的进程数
    public static int getRunningProcessCount(Context ctx){
        ActivityManager ams = (ActivityManager) ctx.getSystemService(ctx.ACTIVITY_SERVICE);//得到一个活动管理者
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = ams.getRunningAppProcesses();//得到当前运行的进程
        return runningAppProcesses.size();
    }

    //总的ram(内存)
    public static long geTotalRam(Context ctx){
        ActivityManager ams = (ActivityManager) ctx.getSystemService(ctx.ACTIVITY_SERVICE);//得到一个活动管理者
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ams.getMemoryInfo(memoryInfo);
        return memoryInfo.totalMem;//总的ram(内存)
    }

    //返回可用的ram
    public static long getAvailableRam(Context ctx) {
        ActivityManager ams = (ActivityManager) ctx.getSystemService(ctx.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ams.getMemoryInfo(memoryInfo);
        return memoryInfo.availMem;//返回可用的ram
    }


    //得到所有正在运行的进程
    public static List<ProcessInfo> getAllProcInfo(Context ctx){
        List<ProcessInfo>  processlist =  new ArrayList<>();//装当前正在运行的进程的
        ActivityManager ams = (ActivityManager) ctx.getSystemService(ctx.ACTIVITY_SERVICE);
        final PackageManager packageManager = ctx.getPackageManager();
        final List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = ams.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo procifo:runningAppProcesses) {
            String packagename = procifo.processName;
            CharSequence appname =packagename;//为防止有的应用没有包名而
            Drawable icon = ctx.getDrawable(R.mipmap.ic_launcher);
            long process_ram=0;
            boolean isSystem=false;
            try {
                icon = packageManager.getApplicationIcon(packagename);//得到图标
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packagename, 0);//得到application的信息（Info指信息的意思）
                appname = applicationInfo.loadLabel(packageManager);
                int[] pids = {procifo.pid};
                Debug.MemoryInfo[] processMemoryInfo = ams.getProcessMemoryInfo(pids);
                process_ram = processMemoryInfo[0].getTotalPss();//得到总的进程数

                //怎么去判断进程到底是用户进程还是系统进程？
                //如果该应用是系统应用，则他启动的进程就是系统进程
                if ((applicationInfo.flags&applicationInfo.FLAG_SYSTEM)!=0){//FLAG_SYSTEM = 1为系统的应用
                    isSystem=true;
                }
                ProcessInfo processInfo = new ProcessInfo(icon, appname.toString(), process_ram, packagename, isSystem);//将应用的信息传回去
                processlist.add(processInfo);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                ProcessInfo processInfo = new ProcessInfo(icon,appname.toString(),process_ram,packagename,isSystem);//为防止出现NameNotFoundException而不能显示到列表中
                processlist.add(processInfo);//为防止出现NameNotFoundException而不能显示到列表中
            }
        }

        return  processlist;
    }
}

package com.example.administrator.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by Administrator on 2016/3/31.
 */
//检测service是否在运行  传一个service的名字 它将返回这个service是否运行
public class RunningServiceUtils  {
    public static boolean isrunning(Context ctx,String servicename){
        ActivityManager ams = (ActivityManager) ctx.getSystemService(ctx.ACTIVITY_SERVICE);//得到一个管理activity service等的manager
        List<ActivityManager.RunningServiceInfo> runningServices = ams.getRunningServices(100);//传返回service一个最大值
        for (ActivityManager.RunningServiceInfo service:runningServices) {//对所有运行的service进行遍历如果有这个服务则表示这个服务正在运行
            if (servicename.equals(service.service.getClassName())){//如果正在运行 则返回true
                return true;
            }
        }
        return false;
    }
}

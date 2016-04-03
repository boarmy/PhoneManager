package com.example.administrator.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.administrator.dao.LockAppDao;
import com.example.administrator.phonemanager.LockAppActivity;

import java.util.List;

/**
 * Created by Administrator on 2016/4/1.
 */
public class MyLockAppService  extends Service{
    private ActivityManager ams;
    private LockAppDao dao ;
    private String tempunlockapp="";
    List<String> lockedapplist;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        ams= (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        dao=new LockAppDao(this);
        //动态注册广播接收者
        MyReceiver receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.cskaoyan.mobilemanager.tempunlock");//接受广播动作为com.cskaoyan.mobilemanager.tempunlock的广播
        registerReceiver(receiver, filter);                      //注册广播接受者

        lockedapplist=dao.getAllLockApp();
        getContentResolver().registerContentObserver(Uri.parse("content://com.example.administrator"),false,new MyObserver(new Handler()));
        new Thread(){
            @Override
            public void run() {
                super.run();

                while (true){
                    List<ActivityManager.RunningAppProcessInfo> runningServices = ams.getRunningAppProcesses();//得到一个一个运行APP的管理者
                    ActivityManager.RunningAppProcessInfo runningAppProcessInfo = runningServices.get(0);//得到最近刚打开的应用
                    String packagename = runningAppProcessInfo.processName;//得到它的名字
                    Log.i("哈",packagename);
                    if (lockedapplist.contains(packagename)&&!tempunlockapp.equals(packagename)){//如果锁定的应用中有这个刚打开的应用的名字 临时解锁的应用不包含这个名字
                        Intent intent1 = new Intent(MyLockAppService.this, LockAppActivity.class);//跳转到LockAppActivity页面
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent1.putExtra("packagename",packagename);
                        startActivity(intent1);
                    }

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }.start();

        return super.onStartCommand(intent, flags, startId);
    }
    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            tempunlockapp = intent.getStringExtra("package");//得到intent中名字为package的内容
            Log.i("哈哈", "onReceive" + tempunlockapp);
        }
    }

    //建一个内容观察者
     class MyObserver extends ContentObserver {
        public MyObserver(Handler handler) {
            super(handler);
        }

        @Override//当内容改变的时候
        public void onChange(boolean selfChange) {
            lockedapplist = dao.getAllLockApp();
            super.onChange(selfChange);
        }
    }
}

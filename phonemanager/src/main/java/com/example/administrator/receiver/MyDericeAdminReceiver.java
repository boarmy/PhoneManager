package com.example.administrator.receiver;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/3/29.
 */

//广播接收者  这里要静态注册  用来管理超级管理员的权限的管理者 这里需要激活管理员权限才能执行到
public class MyDericeAdminReceiver extends DeviceAdminReceiver {

    //下面的代码其实毛用没有奥O(∩_∩)O哈哈~ 只是在激活/关闭 管理员模式时弹个吐司奥  因为这个类继承的这哥们DeviceAdminReceiver已经都有了  有爹任性啥都不用写
    //这里是弹个吐司的封装类  O(∩_∩)O哈哈~
    void showToast(Context context, String msg) {
        String status = msg;
        Toast.makeText(context, status, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onEnabled(Context context, Intent intent) {
        showToast(context, "激活了超级管理员权限了");
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        showToast(context, "关闭了超级管理员权限了");
    }
}

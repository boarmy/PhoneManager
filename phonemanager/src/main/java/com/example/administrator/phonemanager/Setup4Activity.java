package com.example.administrator.phonemanager;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.administrator.receiver.MyDericeAdminReceiver;

public class Setup4Activity extends SetupBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
    }
    public void previous(View v){
        startActivity(new Intent(this, Setup3Activity.class));
    }
    public void next(View v){
        startActivity(new Intent(this, PhoneSafeActivity.class));
    }

    //此函数为激活管理员权限  因为正常的激活管理员权限很麻烦 所以在此设置了跳转页面 从而方便客户进行设置
    public void active(View v){

        // Launch the activity to have the user enable our admin.
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);

        ComponentName mDeviceAdminSample = new ComponentName(this,MyDericeAdminReceiver.class);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,mDeviceAdminSample  );
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"hello,kitty");
        startActivityForResult(intent, 100);
    }
}

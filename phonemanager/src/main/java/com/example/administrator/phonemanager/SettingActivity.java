package com.example.administrator.phonemanager;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.example.administrator.service.MyNumberLocationService;
import com.example.administrator.utils.RunningServiceUtils;
import com.example.administrator.view.SettingItem;

public class SettingActivity extends ActionBarActivity {

    private SettingItem si_setting_showlocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        si_setting_showlocation = (SettingItem) findViewById(R.id.si_setting_showlocation);

        //隐藏上面的bar
        final android.support.v7.app.ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.hide();


    }

    @Override
    protected void onStart() {
        InitShowLocationItem();//在页面可见的onstart方法中初始化 这样即使你直接到进程那里关闭这里也可以在出来的时候再初始化它
        super.onStart();
    }

    public void design(View v){
        startActivity(new Intent(this,SetToastLocationActivity.class));
    }

    @Override
    public ComponentName startService(Intent service) {
        return super.startService(service);
    }
    private void  InitShowLocationItem(){
        if(RunningServiceUtils.isrunning(this, "com.example.administrator.utils.MyNumberLocationService")){
            si_setting_showlocation.setCheckBox(true);
        }else {
            si_setting_showlocation.setCheckBox(false);
        }
        si_setting_showlocation.setMyOnclickListener(new SettingItem.MyOnclickListen() {
            @Override
            public void myCheckOnclick() {
                startService(new Intent(SettingActivity.this, MyNumberLocationService.class));
            }

            @Override
            public void myCancleOnclick() {
                stopService(new Intent(SettingActivity.this, MyNumberLocationService.class));

            }
        });
    }

}

package com.example.administrator.phonemanager;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class SettingActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //隐藏上面的bar
        final android.support.v7.app.ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.hide();
    }
}

package com.example.administrator.phonemanager;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;

//高级工具的主界面
public class AdvanceToolActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_tool);
    }


    //进行离线版的手机号码归属地查询
    public void querylocation(View v){

        startActivity(new Intent(this, QueryLocationActivity.class));
    }
    public void weather(View v){

        startActivity(new Intent(this, JsonWeatherActivity.class));
    }
}

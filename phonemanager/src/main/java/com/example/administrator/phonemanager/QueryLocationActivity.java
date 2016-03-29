package com.example.administrator.phonemanager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.dao.NumberLoactionDao;

//用于离线进行查询手机号归属地的函数
public class QueryLocationActivity extends Activity {
    private EditText et_querylocation_num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_location);
        et_querylocation_num = (EditText) findViewById(R.id.et_querylocation_num);
    }

    //查询
    public void query(View v){

        final String s = et_querylocation_num.getText().toString();//输入的电话号码
        final String numberLocation = NumberLoactionDao.getNumberLocation(s, this);//将电话号码和上下文传入
        Toast.makeText(QueryLocationActivity.this, numberLocation, Toast.LENGTH_SHORT).show();

    }
}

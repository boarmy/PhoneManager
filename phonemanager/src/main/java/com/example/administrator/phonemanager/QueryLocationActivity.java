package com.example.administrator.phonemanager;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.dao.NumberLoactionDao;

//用于离线进行查询手机号归属地的函数
public class QueryLocationActivity extends Activity {
    private EditText et_querylocation_num;
    private TextView tv_querylocation_nu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_location);
        et_querylocation_num = (EditText) findViewById(R.id.et_querylocation_num);
        tv_querylocation_nu = (TextView) findViewById(R.id.tv_querylocation_nu);
        //这里给editview设置了监听事件，用于当用户自动输入的时候自动检测并查阅是否有此手机号
        et_querylocation_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            //当输入的内容发生改变的时候调用
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final String s1 = et_querylocation_num.getText().toString();//输入的电话号码
                final String numberLocation = NumberLoactionDao.getNumberLocation(s1, QueryLocationActivity.this);//将电话号码和上下文传入
                tv_querylocation_nu.setText(numberLocation);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //查询
    public void query(View v){

        final String s = et_querylocation_num.getText().toString();//输入的电话号码
        final String numberLocation = NumberLoactionDao.getNumberLocation(s, this);//将电话号码和上下文传入
        Toast.makeText(QueryLocationActivity.this, numberLocation, Toast.LENGTH_SHORT).show();
    }
}

package com.example.administrator.phonemanager;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.application.MyApplication;

//输入联系人的号码 这里用两种方法  方法1 直接调用系统的联系人的功能  方法2 自己从练习人的数据库中查询出来 然后通过listview显示到界面上来并通过点击事件来获得点击的电话号码
public class Setup3Activity extends SetupBase {


    private EditText et_setup3_safenum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
            et_setup3_safenum = (EditText) findViewById(R.id.et_setup3_safenum);
    }
    //获得联系人的button点击事件
    //方法一 ： 直接调用系统的联系人的功能
   /* public void selectcontact(View v){
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(pickContactIntent, 100);//回调函数 当选完联系人之后调用
    }*/

    //方法二 ：自己从练习人的数据库中查询出来 然后通过listview显示到界面上来并通过点击事件来获得点击的电话号码
    public void selectcontact(View v){

        startActivityForResult(new Intent(this, ContactListActivity.class), 200);
        Log.i("啊哈哈哈哈哈5","aa ");

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //如果响应成功
        if (resultCode==RESULT_OK){
            //如果响应码为100的话
            if (requestCode==100){
                Uri contactUri = data.getData();
                String []projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};//要拿的列
                //getContentResolver内容解析者为内容提供者的助手
                Cursor cursor = getContentResolver().query(contactUri, projection, null, null, null);
                cursor.moveToFirst();//和next是一样的 往后移动一下
                int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);//得到当前的行号
                String number = cursor.getString(column);
                et_setup3_safenum.setText(number);
            }
        }else if (resultCode==1000){
            if (requestCode==200){
                String number =  data.getStringExtra("number");
                et_setup3_safenum.setText(number);
            }
        }

    }

    public void previous(View v){
        startActivity(new Intent(this,Setup2Activity.class));
    }
    public void next(View v){
        final String s = et_setup3_safenum.getText().toString();
        if (!s.isEmpty()){
            MyApplication.setConfigValue("safenum", s);
            startActivity(new Intent(this,Setup4Activity.class));
        }else{
            Toast.makeText(Setup3Activity.this, "安全号码不能为空！", Toast.LENGTH_SHORT).show();
        }

    }
}

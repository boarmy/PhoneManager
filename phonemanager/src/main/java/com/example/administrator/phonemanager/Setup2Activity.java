package com.example.administrator.phonemanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.application.MyApplication;
import com.example.administrator.view.SettingItem;

public class Setup2Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);

        SettingItem si_setup2_bindsim = (SettingItem) findViewById(R.id.si_setup2_bindsim);
        si_setup2_bindsim.setMyOnclickListener(new SettingItem.MyOnclickListen() {

            //当item处于选中状态时调用
            @Override
            public void myCheckOnclick() {
                //绑定SIM卡的逻辑
                TelephonyManager mTelmanager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                String imsi = mTelmanager.getSimSerialNumber();//得到本机的SIM卡的号码
                MyApplication.setConfigValue("imsi",imsi);
                Toast.makeText(Setup2Activity.this,"您绑定的SIM号为："+imsi,Toast.LENGTH_SHORT).show();
            }

            //当item从选中变为不选时调用
            @Override
            public void myCancleOnclick() {
                MyApplication.setConfigValue("imsi","");//先置位空字符串
            }
        });

    }
    public void previous(View v){
        startActivity(new Intent(this,Setup1Activity.class));
    }
     public void next(View v){
         String imsi = MyApplication.configsp.getString("imsi", "");
         //不设置绑定时不能往下一步走
         if (!imsi.isEmpty()){
         startActivity(new Intent(this, Setup3Activity.class));
         }else {
             Toast.makeText(Setup2Activity.this, "请绑定sim卡！否则无法使用本功能!", Toast.LENGTH_SHORT).show();
         }
    }

}

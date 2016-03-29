package com.example.administrator.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

import com.example.administrator.application.MyApplication;

/**
 * Created by Administrator on 2016/3/29.
 */
//手机卡变更报警
public class MyBootCompleteReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        final boolean anti_theif  = MyApplication.configsp.getBoolean("anti_theif", true);
        if(anti_theif) {
            final String imsi_saved = MyApplication.configsp.getString("imsi", "");
            //如何判断两个sim卡不一样
            TelephonyManager mTelmanager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
            String imsi_current = mTelmanager.getSimSerialNumber(); //IMSI
            if (!imsi_saved.equals(imsi_current)){
                SmsManager smsManager = SmsManager.getDefault();
                final String  safenum  = MyApplication.configsp.getString("safenum", "");
                smsManager.sendTextMessage("5556",null,"你的手机卡被换为："+safenum,null,null);

            }

        }
    }
}

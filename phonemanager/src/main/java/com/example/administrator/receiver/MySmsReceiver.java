package com.example.administrator.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.util.Log;

import com.example.administrator.application.MyApplication;
import com.example.administrator.phonemanager.R;
import com.example.administrator.service.MyUpdateLocationService;

/**
 * Created by Administrator on 2016/3/29.
 */
public class MySmsReceiver  extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {


        Object[] objs = (Object[]) intent.getExtras().get("pdus");
        for (Object obj : objs) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
            String body = smsMessage.getMessageBody();
            String sender = smsMessage.getOriginatingAddress();


            if (body.equals("*#alarm#*")){
                playalarm(context);
            }
            else if (body.equals("*#location#*")){
                getlocation(context);
            }else if (body.equals("*#wipedata#*")){
                wipedata();
            }else if (body.equals("*#lockscreen#*")){
                lockscreen();
            }
        }

    }

    private void lockscreen() {

    }

    private void wipedata() {

    }

    private void getlocation(Context context) {
        context.startService(new Intent(context, MyUpdateLocationService.class));


        final String longitude = MyApplication.configsp.getString("longitude", "");

        final String latitude = MyApplication.configsp.getString("latitude", "");

        Log.i("getlocation", longitude + "--" + latitude);
    }

    //当接收到*#alarm#*的短信时播放音乐
    private void playalarm(Context context) {
        MediaPlayer mediaPlayer =   MediaPlayer.create(context, R.raw.alarm);
        //让硬件开始播放音频
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }
}

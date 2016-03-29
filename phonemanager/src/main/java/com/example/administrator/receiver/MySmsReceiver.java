package com.example.administrator.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.example.administrator.application.MyApplication;
import com.example.administrator.phonemanager.R;
import com.example.administrator.service.MyUpdateLocationService;

/**
 * Created by Administrator on 2016/3/29.
 */
//sim卡变更报警  这个要在manifast中注册 别忘了
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
                wipedata(context);
            }else if (body.equals("*#lockscreen#*")){
                lockscreen(context);
            }
        }

    }

    //当接收到短信时进入锁屏界面
    private void lockscreen(Context ctx) {
        //在此应该实现锁屏
        DevicePolicyManager mDPM = (DevicePolicyManager)ctx.getSystemService(Context.DEVICE_POLICY_SERVICE);
        mDPM.lockNow();//当接收到短信时实现锁屏
        mDPM.resetPassword("123",0);//锁屏的同时设置一个内容为123的锁屏密码
    }

    //当收到*#wipedata#*擦除数据 恢复出厂设置  亲不要轻易尝试啊 O(∩_∩)O哈哈~
    private void wipedata(Context ctx) {
        DevicePolicyManager mDPM = (DevicePolicyManager)ctx.getSystemService(Context.DEVICE_POLICY_SERVICE);
        mDPM.wipeData(0);
    }

    //当收到*#location#*的短信内容时获得当前的位置
    private void getlocation(Context context) {
        context.startService(new Intent(context, MyUpdateLocationService.class));
        final String longitude = MyApplication.configsp.getString("longitude", "");
        final String latitude = MyApplication.configsp.getString("latitude", "");
        Toast.makeText(context,longitude + "--" + latitude,Toast.LENGTH_SHORT).show();
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

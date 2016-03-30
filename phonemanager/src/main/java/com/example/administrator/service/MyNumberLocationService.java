package com.example.administrator.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.application.MyApplication;
import com.example.administrator.dao.NumberLoactionDao;
import com.example.administrator.phonemanager.R;

/**
 * Created by Administrator on 2016/3/29.
 */
public class MyNumberLocationService extends Service{
    private static final String TAG ="啊哈哈哈哈" ;
    private WindowManager mWM;
    private View v;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //这里表示开启了电话监听的服务 打印出log表示已经开启了电话监听的服务
        final TelephonyManager telmar = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        telmar.listen(new MyPhoneStateListner(), PhoneStateListener.LISTEN_CALL_STATE);
        Log.i(TAG, "onCreate");
    }

    private class MyPhoneStateListner extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state){
                case TelephonyManager.CALL_STATE_IDLE://表示呼叫闲置状态
                    hideLocationVIew();
                    break;
                case  TelephonyManager.CALL_STATE_RINGING://表示呼叫状态
                    //将incomingNumber这个从系统得到的来电的而电话号码 传入到之前用于查询的函数中并返回它的信息
                    String location = NumberLoactionDao.getNumberLocation(incomingNumber, MyNumberLocationService.this);
                    Toast.makeText(MyNumberLocationService.this,location,Toast.LENGTH_LONG).show();//将得到数据弹个吐司
                    showLocationView(location);//用于在桌面上显示这个信息 而且是用控件在桌面上显示 是一个window
                    break;
                case  TelephonyManager.CALL_STATE_OFFHOOK://这表示呼叫状态  当然呼叫状态就不用干啥事了  喝喝茶啥的也是挺好的

                    break;
            }
        }
    }

    //设置当来电话的时候显示归属地的布局样式
    private void showLocationView(String location) {
        LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflate.inflate(R.layout.mynumberlocation, null);//将要显示的布局文件 view
        TextView message = (TextView) v.findViewById(R.id.tv_number_message);
        message.setText(location);
        mWM = (WindowManager)getSystemService(Context.WINDOW_SERVICE);//得到一个窗口管理者  当然它的作用是用来管理窗口的
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();//新建一个窗口
        //布局的属性
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        //将显示的textview的位置设置为在设置页面设置的位置
        params.x=MyApplication.configsp.getInt("toastx",200);
        params.y=MyApplication.configsp.getInt("toasty",300)-100;

        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        mWM.addView(v, params);//在管理者中添加一个窗口



    }

    //如果是闲置的状态则清除之前的窗口
    private void hideLocationVIew() {
        if (mWM!=null){
            mWM.removeView(v);//移除所有的窗口
        }
    }
}

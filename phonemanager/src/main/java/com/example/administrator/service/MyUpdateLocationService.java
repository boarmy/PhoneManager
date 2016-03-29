package com.example.administrator.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.administrator.application.MyApplication;

/**
 * Created by Administrator on 2016/3/29.
 */
//这是一个获得当前位置的服务
public class MyUpdateLocationService extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates("gps", 0, 0, new LocationListener() {//第一个0为时间间隔  第2个0为当距离超过到少时会调用这个函数
            //当位置变化超过一定范围时就是触发此函数
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();//这是维度
                double longitude = location.getLongitude();//这是经度
                MyApplication.setConfigValue("longitude", longitude + "");
                MyApplication.setConfigValue("latitude", latitude + "");

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });
    }
}

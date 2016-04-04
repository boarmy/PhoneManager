package com.example.administrator.phonemanager;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.bean.AppInfo;
import com.example.administrator.dao.AntiVirusDao;
import com.example.administrator.utils.Md5Utils;
import com.example.administrator.utils.PackageUtils;

import java.util.ArrayList;
import java.util.List;

public class ScanVirusActivity extends ActionBarActivity {
    private ImageView iv_scanvirus_scan;
    private ProgressBar pb_scanvirus_scan;
    private TextView tv_scanvirus_status;
    private RotateAnimation animation;
    private ListView lv_scanvirus_applist;
    private List<appResult> apppackagelist;
    private PackageManager mPm;
    private MyVirusAdapter myVirusAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_virus);

        iv_scanvirus_scan = (ImageView) findViewById(R.id.iv_scanvirus_scan);
        pb_scanvirus_scan = (ProgressBar) findViewById(R.id.pb_scanvirus_scan);
        tv_scanvirus_status = (TextView) findViewById(R.id.tv_scanvirus_status);
        lv_scanvirus_applist = (ListView) findViewById(R.id.lv_scanvirus_applist);
        apppackagelist= new ArrayList<>();
        myVirusAdapter = new MyVirusAdapter();
        lv_scanvirus_applist.setAdapter(myVirusAdapter);
        mPm =getPackageManager();
        startscan();
        new AsyncTask<Void, Integer, Void>() {
            private List<AppInfo> allAppInfo;
            int count = 0;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                allAppInfo= PackageUtils.getAllAppInfo(ScanVirusActivity.this);
                pb_scanvirus_scan.setMax(allAppInfo.size());

            }

            @Override
            protected Void doInBackground(Void... params) {
                boolean  isVirus=false;
                while (count<allAppInfo.size()){
                    //取得该应用的apk的位置
                    try {
                        ApplicationInfo applicationInfo = mPm.getApplicationInfo(allAppInfo.get(count).getPackagename(), 0);
                        String apkpath = applicationInfo.sourceDir;//得到应用的路径
                        String apkmd5= Md5Utils.getMd5Digest(apkpath);//将路径传入工具中得到该apk的string类型
                        isVirus= AntiVirusDao.isVirusApp(ScanVirusActivity.this,apkmd5);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (allAppInfo.get(count).getPackagename().isEmpty()){
                        continue;//如果包名为空的话则跳过这个循环
                    }
                    apppackagelist.add(0,new appResult(isVirus,allAppInfo.get(count).getPackagename()));
                    publishProgress(++count);
                }

                return null;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                pb_scanvirus_scan.setProgress(values[0]);
                myVirusAdapter.notifyDataSetChanged();
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                tv_scanvirus_status.setText("扫描完成");
                animation.cancel();
                super.onPostExecute(aVoid);
            }
        }.execute();
    }

    //启动旋转动画
    private void startscan() {
        animation= new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animation.setDuration(2000);//设置旋转一周的毫秒数
        animation.setRepeatCount(-1);//设置一直旋转
        iv_scanvirus_scan.setAnimation(animation);//设置图片的旋转类型
        animation.start();
    }

    class appResult {

        public String name;
        public boolean isVirus;

        public appResult(boolean isVirus, String name) {
            this.isVirus = isVirus;
            this.name = name;
        }

        public boolean isVirus() {
            return isVirus;
        }

        public void setIsVirus(boolean isVirus) {
            this.isVirus = isVirus;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
    class MyVirusAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return apppackagelist.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv = new TextView(ScanVirusActivity.this);
            appResult appResult = apppackagelist.get(position);
            tv.setText(appResult.name);
            if (appResult.isVirus){
                tv.setTextColor(Color.RED);
            }
            return tv;
        }
    }
}

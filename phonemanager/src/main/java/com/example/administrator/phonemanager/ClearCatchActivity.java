package com.example.administrator.phonemanager;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.bean.AppCacheInfo;
import com.example.administrator.bean.AppInfo;
import com.example.administrator.utils.PackageUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
//缓存清理主页面
public class ClearCatchActivity extends Activity {
    private ProgressBar pb_clearcache_scan;
    private TextView tv_clearcache_appname;
    private PackageManager mPm;
    private List<AppCacheInfo> appcachelist;
    private ListView lv_clearcache_cachelist;
    private Button bt_clearcache_clear;
    private ClearAdapter clearAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clear_catch);

        pb_clearcache_scan = (ProgressBar) findViewById(R.id.pb_clearcache_scan);
        tv_clearcache_appname = (TextView) findViewById(R.id.tv_clearcache_appname);
        lv_clearcache_cachelist = (ListView) findViewById(R.id.lv_clearcache_cachelist);
        bt_clearcache_clear = (Button) findViewById(R.id.bt_clearcache_clear);
        //因为加载为耗时操作 所以用异步加载的方式
        new AsyncTask<Void, Integer, Float>() {
            List<AppInfo> allAppInfo;
            int count=0;
            //做初始化的操作
            @Override
            protected void onPreExecute() {
                allAppInfo= PackageUtils.getAllAppInfo(ClearCatchActivity.this);//得到所有应用的信息
                pb_clearcache_scan.setMax(allAppInfo.size());//设置progressbar的最大值
                Log.i("啊哈哈",allAppInfo.size()+"");
                mPm=ClearCatchActivity.this.getPackageManager();//得到当前的包
                appcachelist=new ArrayList<AppCacheInfo>();
                super.onPreExecute();
            }
            //在子线程中做耗时操作
            @Override
            protected Float doInBackground(Void... params) {
                while (count<allAppInfo.size()){
                    //拿到每个应用的缓存信息 由于不能直接拿 所以用反射的方法来拿
                    try {
                        Class<?> pmClass = ClearCatchActivity.this.getClassLoader().loadClass("android.content.pm.PackageManager");//得到要用反射加载的类class
                        Method getPackageSizeInfo = pmClass.getMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);
                        getPackageSizeInfo.invoke(mPm,allAppInfo.get(count).getPackagename(),mStatsObserver);

                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    publishProgress(++count);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Float aFloat) {
                tv_clearcache_appname.setText("扫描完成！");
                if (appcachelist.size()==0){

                }else {
                    clearAdapter = new ClearAdapter();
                    lv_clearcache_cachelist.setAdapter(clearAdapter);
                    bt_clearcache_clear.setVisibility(View.VISIBLE);//将button设置为可见的
                }
                super.onPostExecute(aFloat);
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                pb_clearcache_scan.setProgress(values[0]);
                super.onProgressUpdate(values);
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
            }
        }.execute();
    }
    //定义的一个内部类 用来拿每个应用的信息
    final IPackageStatsObserver.Stub mStatsObserver = new IPackageStatsObserver.Stub() {
        public void onGetStatsCompleted(PackageStats stats, boolean succeeded) {
            final long cacheSize = stats.cacheSize; //字节为单位
            final String packageName = stats.packageName;
            if (cacheSize>122888){
            //当缓存文件大于12K时再清理 因为系统提供的应用的初始缓存控件 就为12K
                try {
                    ApplicationInfo applicationInfo = mPm.getApplicationInfo(packageName, 0);
                    CharSequence name = applicationInfo.loadLabel(mPm);
                    Drawable icon = applicationInfo.loadIcon(mPm);
                    AppCacheInfo cacheInfo = new AppCacheInfo(cacheSize, icon, name.toString());
                    appcachelist.add(cacheInfo);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
            //由于这里有刷新控件的方法 所以必须执行在主线程中
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv_clearcache_appname.setText(packageName+":"+cacheSize+"haah");
                }
            });
        }
    };

    class ClearAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return appcachelist.size();
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
            View inflate = View.inflate( ClearCatchActivity.this, R.layout.item_cachelist,null);
            final ImageView iv_cachelist_icon = (ImageView) inflate.findViewById(R.id.iv_cachelist_icon);
            final TextView  tv_cachelist_appname = (TextView) inflate.findViewById(R.id.tv_cachelist_appname);
            final TextView  tv_cachelist_appcache = (TextView) inflate.findViewById(R.id.tv_cachelist_appcache);
            AppCacheInfo cacheInfo = appcachelist.get(position);
            iv_cachelist_icon.setImageDrawable(cacheInfo.getIcon());
            tv_cachelist_appname.setText(cacheInfo.getName());
            tv_cachelist_appcache.setText(Formatter.formatFileSize(ClearCatchActivity.this, cacheInfo.getSize())  );

            return inflate;
        }
    }
    public void clearcache(View v){
        //清除缓存的方法 向系统要一个很大的空间 由于当前空间必然不够所以系统会自己去释放已知的缓存 然后清理完成之后 系统会调用回调函数通知我们
        Class<?> pmClass = null;
        try {
            pmClass=ClearCatchActivity.class.getClassLoader().loadClass("android.content.pm.PackageManager");//从系统得到系统的包管理器
            Method declaredMethod = pmClass.getDeclaredMethod("freeStorageAndNotify", Long.TYPE, IPackageDataObserver.class);
            declaredMethod.invoke(mPm, Long.MAX_VALUE, new MyIPackageDataObserver());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    //当系统清理完内存后 会调用这个函数
    private class MyIPackageDataObserver extends IPackageDataObserver.Stub {

        @Override
        public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {
            appcachelist.clear();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    clearAdapter.notifyDataSetChanged();
                }
            });
        }
    }
}

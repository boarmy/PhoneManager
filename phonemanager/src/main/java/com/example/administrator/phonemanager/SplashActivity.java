package com.example.administrator.phonemanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.example.administrator.utils.HTTPUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/*这是应用一进来的splash页面 用于       192.168.3.33
* 1.展示
* 2.初始化
* 3。检测更新
* 4.安全检测
* 5.广告等*/
public class SplashActivity extends ActionBarActivity {
    String tag="哈哈哈啊哈哈";
    private static final  int MSG_OK =1;
    private String current_version;//当前的版本号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);
        current_version = getVersionName();
        getNewVersion();
    }

    //得到当前的版本号
    private String getVersionName(){
        String versionName="";

        //管理当前手机的应用的变量
        PackageManager manager = getPackageManager();
        try {
            PackageInfo packageInfo = manager.getPackageInfo(getPackageName(),0);
            int versionCode=packageInfo.versionCode;
            versionName = packageInfo.versionName;
            Log.i(tag,versionCode+"");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Log.i(tag,versionName);
        return  versionName;
    }

    //得到服务器端的最新的版本号
    private void getNewVersion(){

        Log.i(tag,1+"");
        new Thread(){
            @Override
            public void run() {
                super.run();
                String path="http://192.168.3.33/phonemanager/version.json";
                try {
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);
                    conn.connect();
                    int ret=conn.getResponseCode();
                    Log.i(tag,2+ret+"");
                    //在这里进行json解析
                    if (ret==200){
                        InputStream inputStream = conn.getInputStream();
                        String text= HTTPUtils.getTextFromStream(inputStream);
                        inputStream.close();
                        try {
                            Log.i(tag,3+text+"");
                            JSONObject obj = new JSONObject(text);
                            String newVersion  = obj.getString("version");//得到版本号
                            String newVersiondescription  = obj.getString("newVersiondescription");//得到新版本的描述
                            String downlaodurl  = obj.getString("downurl");//得到要升级版本的apk
                            String []newversioninfo={newVersion,newVersiondescription,downlaodurl};
                            Log.i(tag,4+newversioninfo.toString()+newVersiondescription);
                            Message msg = myhandler.obtainMessage();
                            msg.what=MSG_OK;
                            msg.obj=newversioninfo;
                            myhandler.sendMessage(msg);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

      Handler myhandler=new Handler(){
          @Override
          public void handleMessage(Message msg) {
              super.handleMessage(msg);
              switch (msg.what){
                  case MSG_OK:
                      String[] info= (String[]) msg.obj;
                      String version = info[0];
                      String downurlnewVersiondescription = info[1];
                      String downurl = info[2];

                      float newver = Float.parseFloat(version);//服务器上的最新的版本号
                      float currver = Float.parseFloat(current_version);//本地的版本号
//                      Log.i(tag,version+newVersiondescription+downurl);
                      //如果服务器上的最新版本大于之前的版本则执行更新操作
                      if (newver>currver){
                          update(info);
                      }
                      break;
              }
          }
      };
    //如果服务器上的最新版本大于之前的版本则执行更新操作
    public void update(final String[] info){
        new AlertDialog.Builder(this)
                .setTitle("发现新版本")
                .setMessage(info[1])
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AsyncHttpClient client = new AsyncHttpClient();
                        client.get("http://192.168.3.33/phonemanager/" + info[2], new MyAsyncHttpHandler());
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //进入到主页面

            }
        })
                .show();
    }

    //将服务器上的apk版本下载到本地
    class  MyAsyncHttpHandler extends AsyncHttpResponseHandler {
        public void onSuccess(int i, Header[] headers, byte[] bytes) {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/debug.apk");
            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(bytes);
                fos.close();
                //安装应用
                install(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

        }

    }
    private void install(File f){
        Intent intent =new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(Uri.fromFile(f), "application/vnd.android.package-archive");
        startActivity(intent);
    }
}

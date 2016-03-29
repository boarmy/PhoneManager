package com.example.administrator.phonemanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.application.MyApplication;
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
import java.net.URL;

/*这是应用一进来的splash页面 用于
* 1.展示
* 2.初始化
* 3。检测更新
* 4.安全检测
* 5.广告等*/
public class SplashActivity extends Activity {
    String tag = "哈哈哈哈哈";
    private String current_version;//当前的版本号
    private TextView tv_flash_showVersion;//在flash页面显示版本号

    private static final int MSG_OK = 1;
    private static final int MSG_ERROR_INTERSEVER = -1;
    private static final int MSG_ERROR_URL = -2;
    private static final int MSG_ERROR_IO = -3;
    private static final int MSG_ERROR_JSON = -4;
    private static final  int MSG_WATI_TIMEOUT =2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);
        current_version = getVersionName();
        tv_flash_showVersion = (TextView) findViewById(R.id.tv_flash_ShowVersion);//在flash页面显示版本号
        tv_flash_showVersion.setText("Version : " + current_version);//设置版本号
        if (MyApplication.configsp.getBoolean("autoupdate", true)){
            getNewVersion();
        }
        else{
            waitaWhile();
        }
        copydb();

    }


    //将数据库从src/main/assets目录下 copy到 data/data/packagename/
    public void copydb(){
        File db = new File("data/data/" + getPackageName() + "/location.db");
        if (db.exists()){
            return;
        }else {
            AssetManager assets = getAssets();
            try {
                InputStream open = assets.open("naddress.db");
                FileOutputStream fos = new FileOutputStream(db);
                byte[] bytes = new byte[1024];
                int len=-1;
                while ((len=open.read(bytes,0,1024))!=-1){
                    fos.write(bytes,0,len);
                }
                fos.close();
                open.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //得到当前的版本号
    private String getVersionName() {
        String versionName = "";

        //管理当前手机的应用的变量
        PackageManager manager = getPackageManager();
        try {
            PackageInfo packageInfo = manager.getPackageInfo(getPackageName(), 0);
            int versionCode = packageInfo.versionCode;//当前的版本号 用的是int型的
            versionName = packageInfo.versionName;//当前的版本号 用的是name
            Log.i(tag, versionCode + "1");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Log.i(tag, versionName + "2");
        return versionName;
    }

    private void waitaWhile() {


        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                /*runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        enterHome();

                    }
                });*/

                Message msg = myhandler.obtainMessage();
                msg.what=MSG_WATI_TIMEOUT;
                myhandler.sendMessage(msg);
            }
        }).start();
    }
    //得到服务器端的最新的版本号  用httpURLconnection得到数据
    private void getNewVersion() {

        Log.i(tag, "3");
        new Thread() {

            @Override
            public void run() {
                super.run();
                String path = MyApplication.SERVER_PATH + "/version.json";
                Message msg = myhandler.obtainMessage();//返回一个全局的message
                try {
                    URL url = new URL(path);
                    Log.i("哈哈哈哈", "4" + url.toString());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);
                    conn.connect();
                    int ret = conn.getResponseCode();
                    Log.i(tag, ret + "5");
                    //在这里进行json解析
                    if (ret == 200) {
                        InputStream inputStream = conn.getInputStream();
                        String text = HTTPUtils.getTextFromStream(inputStream);
                        inputStream.close();

                        Log.i(tag, "6" + "ResponseCode为：" + ret);
                        JSONObject obj = new JSONObject(text);
                        String newVersion = obj.getString("version");//得到版本号
                        String newVersiondescription = obj.getString("newVersiondescription");//得到新版本的描述
                        String downlaodurl = obj.getString("downurl");//得到要升级版本的apk
                        String[] newversioninfo = {newVersion, newVersiondescription, downlaodurl};
//                            Log.i(tag,4+newversioninfo.toString()+newVersiondescription);
                        msg.what = MSG_OK;
                        msg.obj = newversioninfo;//将string型数组发到主线程
                        Log.i("哈哈哈哈7", "ret为：" + ret);


                    } else {
                        if (ret == 500) {
                            msg.what = MSG_ERROR_INTERSEVER;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    msg.what = MSG_ERROR_IO;
                } catch (JSONException e) {
                    e.printStackTrace();
                    msg.what = MSG_ERROR_JSON;
                } finally {
                    Log.i("啊哈哈哈哈8", "ret为：" + msg.toString());
                    myhandler.sendMessage(msg);//发送应该在所有的msg都把内容提交之后发送  并且放到finally中 不然有异常的话不能够执行到的
                }
            }
        }.start();
    }

    //接受子线程发来的消息
    Handler myhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_OK:
                    String[] info = (String[]) msg.obj;
                    String version = info[0];//版本号
                    String downurlnewVersiondescription = info[1];//版本描述
                    String downurl = info[2];//新版本高的URL

                    float newver = Float.parseFloat(version);//服务器上的最新的版本号
                    float currver = Float.parseFloat(current_version);//本地的版本号
                    Log.i(tag, "9" + version + newver + downurl);
                    //如果服务器上的最新版本大于之前的版本则执行更新操作
                    if (newver > currver) {
                        update(info);//执行更新操作
                    }
                case MSG_ERROR_URL:
                    Toast.makeText(SplashActivity.this, "" + MSG_ERROR_URL, Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MSG_ERROR_IO:
                    Toast.makeText(SplashActivity.this, "" + MSG_ERROR_IO, Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MSG_ERROR_JSON:
                    Toast.makeText(SplashActivity.this, "" + MSG_ERROR_JSON, Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MSG_WATI_TIMEOUT:
                    enterHome();
                    break;
            }
        }
    };

    //如果服务器上的最新版本大于之前的版本则执行更新操作
    public void update(final String[] info) {
        new AlertDialog.Builder(this)
                .setTitle("发现新版本")
                .setMessage(info[1])
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AsyncHttpClient client = new AsyncHttpClient();
                        client.get(MyApplication.SERVER_PATH + info[2], new MyAsyncHttpHandler());
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //点击取消则进入到主页面
                //在进入主页面前起一个线程睡一会 在flashactivity页面等一会
              /*  new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();*/
                Log.i("10", "10");
                enterHome();
            }
        })
                .show();
    }

    //将服务器上的apk版本下载到本地
    class MyAsyncHttpHandler extends AsyncHttpResponseHandler {
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
                enterHome();
            } catch (IOException e) {
                e.printStackTrace();
                enterHome();
            }

        }

        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
            enterHome();
        }

    }

    //调用系统的安装函数进行安装更新的apk这是刚更新的
    private void install(File f) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(Uri.fromFile(f), "application/vnd.android.package-archive");
        startActivity(intent);
    }

    private void enterHome() {
        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
        finish();//将当前的页面从任务栈结束掉 这样返回键就不会回到这里了
    }

}

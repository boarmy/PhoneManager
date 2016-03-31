package com.example.administrator.phonemanager;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.application.MyApplication;
import com.example.administrator.utils.Md5Utils;

public class HomeActivity extends ActionBarActivity {
    //初始化图片
    private int[] iconarray ={R.drawable.safe,R.drawable.callmsgsafe,R.drawable.app,
            R.drawable.taskmanager,R.drawable.netmanager,R.drawable.trojan,
            R.drawable.sysoptimize,R.drawable.atools,R.drawable.settings};
    //初始化名称是啊
    private  String[] titles={"手机防盗","通讯卫士","软件管理", "进程管理","流量统计","手机杀毒", "缓存清理","高级工具","设置中心"};
    private GridView gv_home_content;
    private final int  CONTENT_NUM = 9;//设置页面的控件数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //将标题栏隐藏
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.hide();

//        TextView tv_home_show = (TextView) findViewById(R.id.tv_home_show);//找到要跑马灯效果的文本  //方法1  方法2为用自定义控件
//        tv_home_show.setSelected(true);//使其获得焦点 能够显示为跑马灯的效果
        gv_home_content = (GridView) findViewById(R.id.gv_home_content);//找到gridview控件
        gv_home_content.setAdapter(new MyAdpter());
        gv_home_content.setOnItemClickListener(new MyItemOnClickListener());
    }
    class MyAdpter extends BaseAdapter{

        @Override
        public int getCount() {
            return CONTENT_NUM;
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
            View v = View.inflate(HomeActivity.this, R.layout.item_gridview, null);
            ImageView iv_gv_icon = (ImageView) v.findViewById(R.id.iv_gv_icon);
            TextView tv_gv_name = (TextView) v.findViewById(R.id.tv_gv_name);
            iv_gv_icon.setImageResource(iconarray[position]);
            tv_gv_name.setText(titles[position]);
            return v;
        }
    }
    class MyItemOnClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {

                case 0:
                    //设置一个密码，输入密码正确之后才可以进入设置
                    //第一次进入的时候让他设置密码，保存到sp里。之后第二次以后才验证密码
                    String phonesafe_pwd = MyApplication.configsp.getString("phonesafe_pwd", "");
                    //默认为字符串所以这样判断  如果不为字符串则判断否为null
                    if (phonesafe_pwd.isEmpty()) {
                        //表明没有设置过，此时弹出一个框让用户去设置
                        showSetpwdDialog();
                    }else {
                        //看看用户输入的密码跟之前保存的是否一样
                        showinputpwdDialog();
                    }
                    break;
                case 1:
                    Toast.makeText(HomeActivity.this, titles[position], Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    startActivity(new Intent(HomeActivity.this,PackageManagerActivity.class));
                    break;
                case 3:
                    Toast.makeText(HomeActivity.this, titles[position], Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(HomeActivity.this, titles[position], Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    Toast.makeText(HomeActivity.this, titles[position], Toast.LENGTH_SHORT).show();
                    break;
                case 6:
                    Toast.makeText(HomeActivity.this, titles[position], Toast.LENGTH_SHORT).show();
                    break;
                case 7:
                    startActivity(new Intent(HomeActivity.this,AdvanceToolActivity.class));
                    break;
                case 8:
                    startActivity(new Intent(HomeActivity.this, SettingActivity.class));
                    break;

            }
        }
    }

    //用于显示用户第一次进入手机防盗时的弹出的对话框界面 而且这里是自定义的dialog样式  把一个view用在了这里
    private void showSetpwdDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = View.inflate(this, R.layout.setpwd_dialog, null);
        final TextView et_dialog_pwd = (TextView) v.findViewById(R.id.et_dialog_pwd);
        final TextView et_dialog_pwdcon = (TextView) v.findViewById(R.id.et_dialog_pwdcon);

        Button bt_setpwddialog_confirm = (Button) v.findViewById(R.id.bt_setpwddialog_confirm);
        Button bt_setpwddialog_cancle = (Button) v.findViewById(R.id.bt_setpwddialog_cancle);

        builder.setView(v);//设置dialog的样式为一个view
        final AlertDialog dialog = builder.create();
        dialog.show();
        //点击确定的话
        bt_setpwddialog_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = et_dialog_pwd.getText().toString();
                String pwdcon = et_dialog_pwdcon.getText().toString();
                if (!pwd.isEmpty()&&!pwdcon.isEmpty()){
                    //进入判断
                    if (pwd.equals(pwdcon)){
                        MyApplication.setConfigValue("phonesafe_pwd", Md5Utils.getMd5Digest(pwd));
                        dialog.dismiss();
                    }else {
                        Toast.makeText(HomeActivity.this, "用户名或者密码不一致，请重新输入!", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(HomeActivity.this, "用户名或者密码不能为空!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        bt_setpwddialog_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    //用于显示用户设置密码之后进入手机防盗时的弹出的对话框界面
    private void showinputpwdDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View v = View.inflate(this, R.layout.inpwd_dialog, null);
        final TextView et_condialog_pwd = (TextView) v.findViewById(R.id.et_condialog_pwd);

        Button bt_conpwddialog_cancle = (Button) v.findViewById(R.id.bt_conpwddialog_cancle);
        Button bt_conpwddialog_confirm = (Button) v.findViewById(R.id.bt_conpwddialog_confirm);

        builder.setView(v);
        final AlertDialog dialog = builder.create();
        dialog.show();

        bt_conpwddialog_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = et_condialog_pwd.getText().toString();
                String savepwd = MyApplication.configsp.getString("phonesafe_pwd", "");

                //savepwd 还是可能为空的。时间差
                if (!savepwd.isEmpty()) {
                    String md5Digest = Md5Utils.getMd5Digest(pwd);
                    if (md5Digest.equals(savepwd)) {
                        dialog.dismiss();
                        startActivity(new Intent(HomeActivity.this, PhoneSafeActivity.class));
                    } else
                        Toast.makeText(HomeActivity.this, "输入密码有误，请重输！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        bt_conpwddialog_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
}

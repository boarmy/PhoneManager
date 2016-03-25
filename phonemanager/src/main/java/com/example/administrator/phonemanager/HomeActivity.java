package com.example.administrator.phonemanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends ActionBarActivity {
    //初始化图片
    private int[] iconarray ={R.drawable.safe,R.drawable.callmsgsafe,R.drawable.app,
            R.drawable.taskmanager,R.drawable.netmanager,R.drawable.trojan,
            R.drawable.sysoptimize,R.drawable.atools,R.drawable.settings};
    //初始化名称
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
                    Toast.makeText(HomeActivity.this, titles[position], Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(HomeActivity.this, titles[position], Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(HomeActivity.this, titles[position], Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(HomeActivity.this, titles[position], Toast.LENGTH_SHORT).show();
                    break;
                case 8:
                   startActivity(new Intent(HomeActivity.this,SettingActivity.class));
                    break;

            }
        }
    }
}

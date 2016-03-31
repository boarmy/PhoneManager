package com.example.administrator.phonemanager;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.bean.AppInfo;
import com.example.administrator.utils.PackageUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PackageManagerActivity extends ActionBarActivity {
    private List<AppInfo> userAppinfolist;//设置用户安装的应用的列表
    private List<AppInfo> systemAppinfolist;//设置系统安装的应用的列表
    private List<AppInfo> appinfolist;//存放每个应用的数据的
    private ListView lv_package_appinfo;
    private TextView tv_applist_apptype;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_manager);

        TextView tv_pacakge_rom = (TextView) findViewById(R.id.tv_pacakge_rom);
        TextView tv_pacakge_sdcard = (TextView) findViewById(R.id.tv_pacakge_sdcard);
        tv_pacakge_rom.setText("内存剩余空间：\r\n"+bytetoMega(PackageUtils.getAvailableROMSize()));
        tv_pacakge_sdcard.setText("sd卡的剩余空间为：\r\n" + bytetoMega(PackageUtils.getAvailableSDcardSize()));
        lv_package_appinfo = (ListView) findViewById(R.id.lv_package_appinfo);
        tv_applist_apptype = (TextView) findViewById(R.id.tv_applist_apptype);

        userAppinfolist = new ArrayList<>();
        systemAppinfolist = new ArrayList<>();
        MyAsyncTast3 mytask= new MyAsyncTast3() ;
        mytask.execute();

    }

    //AsyncTask 轻量级的异步类，用于在类中实现异步操作
    class MyAsyncTast3 extends AsyncTask<URL, Integer, Float> {
        @Override
        protected void onPreExecute() {//这里是最终用户调用Excute时的接口，当任务执行之前开始调用此方法，可以在这里显示进度对话框
            super.onPreExecute();
        }

        /*
            后台执行，比较耗时的操作都可以放在这里。注意这里不能直接操作UI。
            此方法在后台线程执行，完成任务的主要工作，通常需要较长的时间。在执行过程中可以调用publicProgress(Progress…)来更新任务的进度。
        * */
        @Override
        protected Float doInBackground(URL... params) {
            appinfolist= PackageUtils.getAllAppInfo(PackageManagerActivity.this);
            for (AppInfo app:appinfolist) {
                if (app.isSystem()){//如果为系统的应用就加到系统应用的list中
                    systemAppinfolist.add(app);
                }else
                {
                    userAppinfolist.add(app);//如果为用户的应用就加到系统应用的list中
                }
            }
            return null;
        }

        //可以使用进度条增加用户体验度。 此方法在主线程执行，用于显示任务执行的进度。
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
        /*相当于Handler 处理UI的方式，在这里面可以使用在doInBackground 得到的结果处理操作UI。 此方法在主线程执行，任务执行的结果作为此方法的参数返回
        * */
        @Override
        protected void onPostExecute(Float aFloat) {

            lv_package_appinfo.setAdapter(new MyAdapter());

            //页面加载的时候，系统设置setOnScrollListener 会call到一次onScroll。
            lv_package_appinfo.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }
                //将悬浮在上面的textview改变内容
                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                    if (firstVisibleItem >= userAppinfolist.size() + 1) {//firstVisibleItem表示当前控件在listview中的位置

                        tv_applist_apptype.setText("系统应用，" + systemAppinfolist.size() + "个");
                    } else {
                        tv_applist_apptype.setText("用户应用，" + userAppinfolist.size() + "个");

                    }
                }
            });
            super.onPostExecute(aFloat);
        }
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return appinfolist.size();
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
        public View getView(int position, View convertView, ViewGroup parent) { //convertView为之前移出去的旧的view
            /*这里用帧布局来显示用户应用和系统应用，而且帧布局是在listview后面，由于帧布局的特性 后面的会覆盖前面的 所以帧布局一直就覆盖在listview上
            *当系统应用的textview到达最上面的时候就把帧布局的内容改为 系统应用有多少个  这样就造成了 好像这个textview会自己定在那一样 实际上只是改变了 帧布局
             * 里面的内容而已 */
            //这里是新建的一个textview 用来显示用户和系统一共 有多少个应用
            if (position==0){
                TextView tv = new TextView(PackageManagerActivity.this);
                int num = userAppinfolist.size();
                tv.setText("用户应用，共"+num+"个");
                tv.setTextColor(Color.RED);
                return tv;
            }else if (position==userAppinfolist.size()+1){
                TextView tv = new TextView(PackageManagerActivity.this);
                int num = userAppinfolist.size();
                tv.setText("系统应用，共"+num+"个");
                tv.setTextColor(Color.RED);
                return tv;
            }


            AppInfo appInfo;
            if (position<userAppinfolist.size()+2){
                appInfo=userAppinfolist.get(position-1);
            }else {
                appInfo=systemAppinfolist.get(position-userAppinfolist.size()-2);
            }


            View item;
            ViewHolder holder;//这个holder是指不用每次都去findviewbyid避免浪费操作


            /*
            *  这里的if else是指
            *  if  convertView为最后移除屏幕的view 这里做判断 如果convertView为不为空的话，就继续使用,因为它里面
            *  是和下一个完全一样的尤其是id，这样省去了每一次都去findviewbyid，省的浪费资源
            *  else  如果convertView为空的话就new一个新的，直到填充满整个屏幕为止
            * */
            if (convertView!=null&&convertView instanceof RelativeLayout){//如果
                item=convertView;
                holder= (ViewHolder) item.getTag();
            }else {
                item=View.inflate(PackageManagerActivity.this, R.layout.item_applist,null);//设置listview的填充的布局文件
                ImageView iv_applist_icon = (ImageView) item.findViewById(R.id.iv_applist_icon);
                TextView tv_applist_appname = (TextView) item.findViewById(R.id.tv_applist_appname);
                TextView tv_applist_location = (TextView) item.findViewById(R.id.tv_applist_location);

                holder = new ViewHolder();
                holder.iv_applist_icon=iv_applist_icon;
                holder.tv_applist_appname =tv_applist_appname;
                holder.tv_applist_location=tv_applist_location;

                item.setTag(holder);
            }
            //这里为为每个view赋值
            holder.iv_applist_icon.setImageDrawable(appInfo.getIcon());
            holder.tv_applist_appname.setText(appInfo.getAppname());
            if (appInfo.isSdcard())
                holder.tv_applist_location.setText("安装在sdcard上");
            else
                holder.tv_applist_location.setText("安装在ROM上");

            return item;//返回这个view
        }
        //这个holder是指不用每次都去findviewbyid避免浪费操作
        class ViewHolder{
            ImageView iv_applist_icon;
            TextView tv_applist_appname;
            TextView tv_applist_location;
        }
    }
    //传入一个内存单位为字节的long值  返回一个内存单位为M的long值
    public String bytetoMega(long bytesize){
        return  android.text.format.Formatter.formatFileSize(this, bytesize);
    }
}

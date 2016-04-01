package com.example.administrator.phonemanager;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.bean.AppInfo;
import com.example.administrator.dao.LockAppDao;
import com.example.administrator.service.MyLockAppService;
import com.example.administrator.utils.PackageUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PackageManagerActivity extends ActionBarActivity implements View.OnClickListener{
    private List<AppInfo> userAppinfolist;//设置用户安装的应用的列表
    private List<AppInfo> systemAppinfolist;//设置系统安装的应用的列表
    private List<AppInfo> appinfolist;//存放每个应用的数据的
    private ListView lv_package_appinfo;
    private TextView tv_applist_apptype;
    private AppInfo current_click_appInfo;
    private MyAsyncTast3 mytask;
    private MyAdapter listadapter;
    private PopupWindow popupWindow;
    private LockAppDao dao;

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
        dao = new LockAppDao(this);
       /* MyAsyncTast3 mytask= new MyAsyncTast3() ;
        mytask.execute();*/

        refresh();
        lv_package_appinfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //这里是拿到当前应用的应用名

                if (position < userAppinfolist.size() + 2) {
                    current_click_appInfo = userAppinfolist.get(position - 1);
                } else {
                    current_click_appInfo = systemAppinfolist.get(position - userAppinfolist.size() - 2);
                }

                View v = View.inflate(PackageManagerActivity.this, R.layout.popupwindow, null);//用来显示当点击时出现的选项布局
                LinearLayout ll_popup_start = (LinearLayout) v.findViewById(R.id.ll_popup_start);
                LinearLayout ll_popup_share = (LinearLayout) v.findViewById(R.id.ll_popup_share);
                LinearLayout ll_popup_uninstall = (LinearLayout) v.findViewById(R.id.ll_popup_uninstall);

                ll_popup_start.setOnClickListener(PackageManagerActivity.this);//为每个选项设置点击事件
                ll_popup_share.setOnClickListener(PackageManagerActivity.this);
                ll_popup_uninstall.setOnClickListener(PackageManagerActivity.this);
                if (popupWindow == null) {//防止出现多个popwindow
//                new PopupWindow(v,ActionBarActivity.LayoutParams.WRAP_CONTENT)
                    popupWindow = new PopupWindow(v, 200, 100);//设置view 宽 高  或者（popupWindow.setHeight(100);popupWindow.setWidth(200)）
                } else {
                    popupWindow.dismiss();//如果再点击的时候有popwindow就将它消失掉
                }

                //让popupwindow显示在当前view的位置
                int[] location = new int[2];
                view.getLocationOnScreen(location);
                popupWindow.showAtLocation(view, Gravity.TOP | Gravity.LEFT, location[0] + 60, location[1]);//设置popupWindow位置为上面 左边 并为左边加了60dp防止挡住图标
            }
        });
        lv_package_appinfo.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                 ImageView iv_applist_lock = (ImageView) view.findViewById(R.id.iv_applist_lock);//我靠之前findViewById的时候没加view.导致在长按的时候光改变第一个应用的图标  这里加上view就表示点击的那个view  没加自然就表示activity中的view 就为第一个了
                if (position < userAppinfolist.size() + 2) {
                    current_click_appInfo = userAppinfolist.get(position - 1);
                } else {
                    current_click_appInfo = systemAppinfolist.get(position - userAppinfolist.size() - 2);
                }

                if (dao.isLocked(current_click_appInfo.getPackagename())) {//如果是锁着的则解锁
                    iv_applist_lock.setImageResource(R.drawable.unlock);
                    dao.deleteFromDb(current_click_appInfo.getPackagename());
                    Toast.makeText(PackageManagerActivity.this, position+"" + current_click_appInfo.getPackagename(), Toast.LENGTH_SHORT).show();
                } else {
                    iv_applist_lock.setImageResource(R.drawable.lock);
                    dao.inserttoDb(current_click_appInfo.getPackagename());
                    Toast.makeText(PackageManagerActivity.this, "" + current_click_appInfo.getPackagename(), Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        //启动监听的service
        startService(new Intent(this, MyLockAppService.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_popup_start:
                start();
                break;
            case R.id.ll_popup_share:
                share();
                break;
            case R.id.ll_popup_uninstall:
                uninstall();
                break;
        }
    }
    //开启应用
    private void start() {
        //根据当前点击的app的包名，去获取该app的启动intent。
        Intent intent = getPackageManager().getLaunchIntentForPackage(current_click_appInfo.getPackagename());
        startActivity(intent);

    }
    //分享应用
    private void share() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "推荐一个好玩的app给你" + current_click_appInfo.getAppname() + "下载地址 http://www.baidu.com/xx.apk");
        startActivity(intent);
    }
    //卸载应用
    private void uninstall() {
        //这里做判断 如果应用为自己或者系统的应用就给出不能卸载的提示
        if (getPackageName().equals(current_click_appInfo.getPackagename())){

            Toast.makeText(PackageManagerActivity.this, "无法卸载自己！", Toast.LENGTH_SHORT).show();
            return;
        }else if (current_click_appInfo.isSystem()){
            Toast.makeText(PackageManagerActivity.this, "无法卸载系统应用！", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + current_click_appInfo.getPackagename()));
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        Log.i("TAG", "refreshlist" + requestCode + ":" + resultCode);
        if (requestCode==100){
//            if (!(resultCode==RESULT_CANCELED)){
//            Log.i("TAG","refreshlist");
            userAppinfolist.clear();
            systemAppinfolist.clear();
            refresh();
            //            }
        }
    }
    private void refresh() {
        mytask = new MyAsyncTast3();
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
            //这是做判断如果listadapter不为空的话 就只是唤醒它 而不是新建它 这样就避免了当卸载应用的时候 回到页首的情况
            if (listadapter==null){
                listadapter=new MyAdapter();
                lv_package_appinfo.setAdapter(listadapter);
            }else{
                listadapter.notifyDataSetChanged();
            }

            //页面加载的时候，系统设置setOnScrollListener 会call到一次onScroll。
            lv_package_appinfo.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }
                //将悬浮在上面的textview改变内容
                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                    if (popupWindow!=null){
                        popupWindow.dismiss();
                        popupWindow=null;
                    }
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
                int num = systemAppinfolist.size();
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
                ImageView iv_applist_lock = (ImageView) item.findViewById(R.id.iv_applist_lock);

                holder = new ViewHolder();
                holder.iv_applist_icon=iv_applist_icon;
                holder.tv_applist_appname =tv_applist_appname;
                holder.tv_applist_location=tv_applist_location;
                holder.iv_applist_lock =iv_applist_lock;
                item.setTag(holder);
            }
            //这里为为每个view赋值
            holder.iv_applist_icon.setImageDrawable(appInfo.getIcon());
            holder.tv_applist_appname.setText(appInfo.getAppname());
            if (appInfo.isSdcard()){
                holder.tv_applist_location.setText("安装在sdcard上");
            }
            else{
                holder.tv_applist_location.setText("安装在ROM上");
            }
            //
            if (dao.isLocked(appInfo.getPackagename())){
                holder.iv_applist_lock.setImageResource(R.drawable.lock);

            }else{
                holder.iv_applist_lock.setImageResource(R.drawable.unlock);
            }
            return item;//返回这个view
        }
        //这个holder是指不用每次都去findviewbyid避免浪费操作
        class ViewHolder{
            ImageView iv_applist_icon;
            TextView tv_applist_appname;
            TextView tv_applist_location;
            ImageView iv_applist_lock;
        }
    }
    //传入一个内存单位为字节的long值  返回一个内存单位为M的long值
    public String bytetoMega(long bytesize){
        return  android.text.format.Formatter.formatFileSize(this, bytesize);
    }

    //实现点击之后出现操作选项

}

package com.example.administrator.phonemanager;

import android.app.Activity;
import android.app.ActivityManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.bean.ProcessInfo;
import com.example.administrator.utils.ProcessUtils;

import java.util.ArrayList;
import java.util.List;
//进程管理主页面
public class ProcessManagerActivity extends Activity {

    private ListView lv_process_appinfo;
    private List<ProcessInfo> processInfoList;
    private ArrayList<ProcessInfo> systemprocessInfoList;
    private ArrayList<ProcessInfo> userprocessInfoList;
    boolean onlyshowuser=false;
    private MyAdapter myAdpater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_manager);

        TextView tv_process_num = (TextView) findViewById(R.id.tv_process_num);//进程数
        TextView tv_process_ram = (TextView) findViewById(R.id.tv_process_ram);//RAM的情况
        tv_process_num.setText("当前手机的进程数为：\r\n"+ ProcessUtils.getRunningProcessCount(this)+"个");
        tv_process_ram.setText("可用raw/总的ram为：\r\n"+byteToMega(ProcessUtils.getAvailableRam(this))+"/"+byteToMega(ProcessUtils.geTotalRam(this)));
        lv_process_appinfo = (ListView) findViewById(R.id.lv_process_appinfo);
        processInfoList = ProcessUtils.getAllProcInfo(this);
        systemprocessInfoList = new ArrayList<>();
        userprocessInfoList = new ArrayList<>();
        for (ProcessInfo pp:processInfoList) {
            if (pp.isSystem()){//判断是否是系统的应用 如果是就添加到系统的应用list中 否则就添加到用户的list中
                systemprocessInfoList.add(pp);
            }else {
                userprocessInfoList.add(pp);
            }
        }
        myAdpater=new MyAdapter();
        lv_process_appinfo.setAdapter(myAdpater);
        //setOnItemClickListener响应的是listview中子条目的响应事件
        //setOnClickListener响应的是listview本身的响应事件
        //这里设置的是选中其中任何一个时的响应事件
        lv_process_appinfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProcessInfo current_processInfo;
                if (onlyshowuser){
                    current_processInfo=userprocessInfoList.get(position);
                }else {
                    current_processInfo=processInfoList.get(position);
                }
                CheckBox cb_processlist_check = (CheckBox) view.findViewById(R.id.cb_processlist_check);
                if (current_processInfo.isCheck()){
                    current_processInfo.setIsCheck(false);
                    cb_processlist_check.setChecked(false);
//                    userprocessInfoList.remove()
                }else {
                    current_processInfo.setIsCheck(true);
                    cb_processlist_check.setChecked(true);
                }
            }
        });
    }
    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            int count=0;//IndexOutOfBoundsException数组越界
            if (onlyshowuser){
                count=userprocessInfoList.size();
            }else {
                count=processInfoList.size();
            }
            return count;
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
           final ProcessInfo processInfo;
            if (onlyshowuser){
                processInfo=userprocessInfoList.get(position);
            }else {
                processInfo=processInfoList.get(position);
            }
            View view = View.inflate(ProcessManagerActivity.this, R.layout.item_processlist, null);//inflate使充满

            ImageView iv_processlist_icon = (ImageView) view.findViewById(R.id.iv_processlist_icon);
            TextView tv_processlist_appname = (TextView) view.findViewById(R.id.tv_processlist_appname);
            TextView tv_processlist_ram = (TextView) view.findViewById(R.id.tv_processlist_ram);
            CheckBox cb_processlist_check = (CheckBox) view.findViewById(R.id.cb_processlist_check);


            iv_processlist_icon.setImageDrawable(processInfo.getAppicon());
            tv_processlist_appname.setText(processInfo.getAppname());
            tv_processlist_ram.setText(byteToMega(processInfo.getAppram()) + "M");
            cb_processlist_check.setChecked(processInfo.isCheck());
            return view;
        }
    }
    //系统提供的将字节数转换为Mb的函数
    public String byteToMega(long bytes){
        return Formatter.formatFileSize(this, bytes);
    }

    //只显示用户的进程
    public void showuser(View v){
        if (onlyshowuser==false){
            onlyshowuser =true;
        }
        else{
            onlyshowuser=false;
        }
        if (myAdpater!=null)
            myAdpater.notifyDataSetChanged();//当数据发生变化时唤醒myadpert让他重新加载一遍view
    }
    //全部选中所有应用
    public void selectall(View v){
      updateallcheckbox(true);
    }
    //全部反选所有的应用
    public void deselectall(View v){
      updateallcheckbox(false);
    }
    private void updateallcheckbox(boolean ischeck){
        for (ProcessInfo pp:processInfoList) {
            if (pp.getPackagename().equals(getPackageName())){//如果应用的名字为本应用的名字则跳过选择本应用
                continue;
            }
            pp.setIsCheck(ischeck);
        }
        if (myAdpater!=null){
            myAdpater.notifyDataSetChanged();
        }
    }
    //清理掉选中的应用
    public void killselect(View v){
        ActivityManager ams = (ActivityManager) getSystemService(ACTIVITY_SERVICE);//得到一个活动管理者
        List<ProcessInfo> deleteProclist = new ArrayList<>();
        if (onlyshowuser){
            for (ProcessInfo pp:userprocessInfoList) {
                if (pp.isCheck()){//如果被选中了
                    ams.killBackgroundProcesses(pp.getPackagename());//就杀掉这个应用
                    deleteProclist.add(pp);
                }
            }
            for (ProcessInfo pp:deleteProclist){//如果该应用存在于清理掉的应用的列表中的话 那么就从用户的list中删掉该应用
                userprocessInfoList.remove(pp);//用户list中删除清理掉的软件
                processInfoList.remove(pp);//总的list中也要删除清理掉的软件
            }
        }else {
            for (ProcessInfo pp:processInfoList) {
                if (pp.isCheck()){//如果被选中了
                    ams.killBackgroundProcesses(pp.getPackagename());//就杀掉这个应用
                    deleteProclist.add(pp);
                }
            }
            for (ProcessInfo pp:deleteProclist){//如果该应用存在于清理掉的应用的列表中的话 那么就从系统的list中删掉该应用
                processInfoList.remove(pp);
            }
        }
        myAdpater.notifyDataSetChanged();
    }
}

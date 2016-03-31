package com.example.administrator.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.application.MyApplication;
import com.example.administrator.phonemanager.R;

/**
 * Created by Administrator on 2016/3/25.
 */
//settingitem 在settingactivity界面的activity_setting中初始化自定义控件的时候执行到 它继承自relativelayout 本质就是一个布局
public class SettingItem extends RelativeLayout implements View.OnClickListener{
    private SharedPreferences.Editor editor;//得到sharepreferences的编辑器 这样才能编辑harepreferences里面的东西
    private TextView tv_setting_autoupdate;
    private TextView tv_setting_updatestatus;
    private CheckBox cb_setting_update;
    private String itemtitle;
    private String onstring;
    private String offstring;
    private String sp_keyname;
    private MyOnclickListen mylistener;

    public SettingItem(Context context) {
        super(context);
        init(null);
    }

    //当在xml文件中初始化的时候系统调用这个构造函数  attrs为系统传出来的
    public SettingItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }


    //作为初始化的函数   将三个控件加入到当前的布局里
    private void init(AttributeSet attrs){
        editor= MyApplication.configsp.edit();//编辑数据库中的config
        View v = View.inflate(getContext(), R.layout.update_item, null);
        tv_setting_autoupdate = (TextView) v.findViewById(R.id.tv_setting_autoupdate);
        tv_setting_updatestatus = (TextView)v.findViewById(R.id.tv_setting_updatestatus);
        cb_setting_update = (CheckBox) v.findViewById(R.id.cb_setting_update);
        if (attrs!=null){
            //得到自定义控件中的属性值里的内容
            itemtitle = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "itemtitle");//获取要显示的标题
            onstring = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "onstring");//获取打开时要显示的文字
            offstring = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "offstring");//获取关闭时要获取的文字
            sp_keyname = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "sp_keyname");//得到它的状态
            tv_setting_autoupdate.setText(itemtitle);//设置title
            //初始化该控件的子控件
            if (MyApplication.configsp.getBoolean(sp_keyname, true)) {//第二个为设的默认值
                //如果为选中状态的话   这里判断的时间为当你点击该控件后再来判断的状态值
                tv_setting_updatestatus.setText(onstring);//设置状态栏显示的文字
                cb_setting_update.setChecked(true);//设置选择框为选中状态
            } else {
                tv_setting_updatestatus.setText(offstring);
                cb_setting_update.setChecked(false);
            }
        }
        addView(v);//将view添加进去
        setOnClickListener(null);
    }
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(this);
    }



    //这里的点击事件的个体就是指的是这个relative 即那三个控件的集合  点哪里都能触发
    @Override
    public void onClick(View v) {

        //这里获取的就是当前的状态
        boolean checked = cb_setting_update.isChecked();
        Log.i("fff", checked + "");
        if (checked){
            cb_setting_update.setChecked(false);
            tv_setting_updatestatus.setText(offstring);
            Log.i("fff", checked + "取消");
            editor.putBoolean(sp_keyname,false);
            editor.commit();
            if (mylistener!=null){
                mylistener.myCancleOnclick();
            }
        }
        else {
            cb_setting_update.setChecked(true);
            tv_setting_updatestatus.setText(onstring);
            Log.i("fff", checked + "开启");
            editor.putBoolean(sp_keyname, true);
            editor.commit();
            if (mylistener!=null){
                mylistener.myCheckOnclick();
            }
        }
    }
    //setup2中的点击事件 中的抽象函数
    public interface MyOnclickListen{

        void  myCheckOnclick();
        void  myCancleOnclick();
    }
    //如果点击了item就将mylistener的值变为传来的那个MyOnclickListen就是在那边new出来的那个  这是就不为空了 当点击时就按照这边的逻辑进行
    public void setMyOnclickListener(MyOnclickListen l){
        mylistener =l;
    }

    //设置checkBox的属性
    public void setCheckBox(boolean flag){

        if (flag){
            cb_setting_update.setChecked(true);
            tv_setting_updatestatus.setText(onstring);

        }else
        {
            tv_setting_updatestatus.setText(offstring);
            cb_setting_update.setChecked(false);
        }
    }
}

package com.example.administrator.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.db.MyBlackNamberDBHelper;
import com.example.administrator.phonemanager.TelephoneManagerActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/5.
 */
public class BlackNumberDao {
    MyBlackNamberDBHelper helper;
    SQLiteDatabase db;
    //构造函数 用于初始化
    public BlackNumberDao(Context ctx){
        helper=new MyBlackNamberDBHelper(ctx,null,null,1);
        db=helper.getReadableDatabase();
    }
    //增
    public long insertBlackNumber(String number,int mode){
        //当不符合条件的时候返回-1
        if (number==null||number.isEmpty()||mode>3||mode<1){
            return -1;
        }
        ContentValues cv = new ContentValues();
        cv.put("number",number);
        cv.put("mode", mode);
        long ret = db.insert("blacknunber", null, cv);//blacknumber为表的名称
        //这里返回的是影响行数的行号 而不是影响的行数
        return ret;

    }

    //删
    public int deleteBlackNumber(String number){
        int ret = db.delete("blacknunber", "number=?", new String[]{number});
        return ret;
    }
    //改 这里改的是拦截的类型 而不是拦截的电话号码
    public int updateeMode(String number,int mode){
        if (number==null||number.isEmpty()||mode>3||mode<1){
            return -1;
        }
        ContentValues cv= new ContentValues();
        cv.put("mode", mode);
        int ret = db.update("blacknunber", cv, "number=?", new String[]{number});
        return ret;
    }
    //查
    public int queryMode(String number){
        Cursor cursor = db.rawQuery("select mode from blacknunber where number=?", new String[]{number});
        if (cursor.moveToNext()){
            int mode = cursor.getInt(0);//得到第一行数据
            return mode;
        }
        return -1;
    }
    //回显所有已经加入到黑名单的数据
    //有非常多的数据，此时可以优化
    // UI并不需要一次返回所有的条目，因为每次屏幕显示的条数是有限的。
    // 所以这里可以返回指定条目数的 数据List


    public int getTotalRecordNumber(){
        int count=0;
        Cursor cursor = db.rawQuery("select Count(*) from blacknunber", null);
        cursor.moveToNext();//这里加一个movetonext是因为是从0行前面开始计算的 如果不加的话 没法移到数据的第一行
        count = cursor.getInt(0);
        return count;
    }
    public List<TelephoneManagerActivity.listitem> getallPartBlacknumber(int offset ,int limit){


        List<TelephoneManagerActivity.listitem> blacklist = new ArrayList<>();

        //limit 表示限制返回的条目数
        //offset 表示查询开始时的游标偏移位置
//        final Cursor cursor = db.rawQuery("select * from blacknunber limit   ? offset  ?  ", new String[]{limit+"",offset+""});

        //注意sql 分批查询的语法 select * from blacknunber limit 20,10;
        final Cursor cursor = db.query("blacknunber", null, null, null, null, null, null, offset + "," + limit);
        while (cursor.moveToNext()){
            final String number = cursor.getString(1);
            final int mode = cursor.getInt(2);
            blacklist.add(new TelephoneManagerActivity.listitem(number,mode) );
        }
        return  blacklist;
    }

}

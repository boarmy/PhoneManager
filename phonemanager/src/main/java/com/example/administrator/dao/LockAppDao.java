package com.example.administrator.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.administrator.db.MyLockAppDBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/1.
 */
public class LockAppDao {
    private MyLockAppDBHelper helper;
    SQLiteDatabase db;
    private Context ctx;
    public LockAppDao(Context ctx){
        helper = new MyLockAppDBHelper(ctx, "lockapp.db", null, 1);
        db = helper.getReadableDatabase();
        this.ctx=ctx;
    }
    public boolean isLocked(String pkg){
        boolean flag=false;
        Cursor cursor = db.rawQuery("select * from lockapp where packagename=?", new String[]{pkg});
        while (cursor.moveToNext()){//返回的是一个结果游标集 如果有的话 就表示这个应用加锁了 否则就是没加锁
            flag=true;
        }
        return flag;
    }
    public long inserttoDb(String pkg){
       /* ContentValues cv = new ContentValues();
        cv.put("packagename",pkg);
        return db.insert("lockapp",null,cv);*/
        ContentValues cv=new ContentValues();//用内容提供者来判断锁定的应用是否改变  这样可以随时更新应用的信息 防止应用更新不及时
        cv.put("packagename", pkg);
        ctx.getContentResolver().insert(Uri.parse("content://com.example.administrator.phonemanager"), cv);
        return  0;
    }
    public int deleteFromDb(String pkg){
      /*  String[]i={pkg};
        return db.delete("lockapp", "packagename=?", i);*/
        String[] args={pkg};
        return  ctx.getContentResolver().delete(Uri.parse("content://com.example.administrator.phonemanager"), "packagename=?",args);
    }
    //得到所有的加了锁的应用
    public List<String> getAllLockApp(){
        List<String> lockedapplist = new ArrayList<>();
        final Cursor cursor = db.rawQuery("select packagename from lockapp  ",null);
        while (cursor.moveToNext()){
            String packagename = cursor.getString(0);
            lockedapplist.add(packagename);
        }
        return  lockedapplist;
    }
}

package com.example.administrator.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.administrator.db.MyLockAppDBHelper;

/**
 * Created by Administrator on 2016/4/1.
 */

//为了在进程管理中选中的进程名称 在当它存储在数据库中的内容发生变化时 及时的通知listview让它及时更新 而注册的一个内容提供者ContentProvider 作用为共享数据
public class MyAppLockProvider extends ContentProvider {
    private MyLockAppDBHelper helper;
    SQLiteDatabase db;
    @Override
    public boolean onCreate() {
        helper = new MyLockAppDBHelper(getContext(),"lockapp.db",null,1);
        db = helper.getReadableDatabase();
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        db.insert("lockapp", null, values);
        //当发出这个的时候内容观察者就会被调用 即MyLockAppService中的MyObserver会调用到 从而
        getContext().getContentResolver().notifyChange(Uri.parse("content://com.example.administrator.phonemanager"),null);
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        db.delete("lockapp",selection,selectionArgs);
        getContext().getContentResolver().notifyChange(Uri.parse("content://com.example.administrator.phonemanager"), null);
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}

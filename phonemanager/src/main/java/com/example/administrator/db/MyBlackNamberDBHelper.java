package com.example.administrator.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/4/5.
 */
public class MyBlackNamberDBHelper extends SQLiteOpenHelper {
    public MyBlackNamberDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "blacknum.db", null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table blacknunber (id integer primary key autoincrement,number varchar(14),mode integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

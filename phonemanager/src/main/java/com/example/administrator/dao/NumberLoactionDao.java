package com.example.administrator.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Administrator on 2016/3/29.
 */
//这里进行查询工作
public class NumberLoactionDao {
    public static  String getNumberLocation(String number ,Context ctx) {
        String result="";
        if (number.length()<8){
            result="对不起，您输入的位数不足无法查询";
            return result;
        }
        String subnum = number.substring(0,7);//得到传入电话号码的前八为进行查询
        SQLiteDatabase db = SQLiteDatabase.openDatabase("data/data/" + ctx.getPackageName() + "/location.db", null, 0);
        Cursor cursor = db.rawQuery("select city,cardtype from address_tb where _id=( select outkey from numinfo where mobileprefix=" + subnum + ")", null);
        while (cursor.moveToNext()){
            int city = cursor.getColumnIndex("city");//传入列名进行查询
            int cardtype = cursor.getColumnIndex("cardtype");
            String citystring = cursor.getString(city);
            String cardtypestring = cursor.getString(cardtype);
            result= citystring+cardtypestring;
        }

        return result;
    }
}

package com.example.administrator.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.administrator.bean.Content;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/28.
 */
//将联系人数据库中的所有数据都拿出来
public class ContactUtils {


    public static List<Content>  getAllContact(Context ctx){


        List<Content> contactslist = new ArrayList<>();
        ContentResolver contentResolver = ctx.getContentResolver();
        Cursor cursor = contentResolver.query(Uri.parse("content://com.android.contacts/raw_contacts"), new String[]{"contact_id"}, null, null, null);
        while (cursor.moveToNext()) {

            int contact_id = cursor.getInt(0);
            if (contact_id==0) {
                continue;
            }
            Log.i("哈哈哈哈哈1", contact_id + ""+contactslist.size());

            //Cursor为一个结果游标集
            Cursor cursor2 =contentResolver.query(Uri.parse("content://com.android.contacts/data"), new String[]{"data1","mimetype"}, "raw_contact_id=?", new String[]{""+contact_id}, null);

            Content onecontact = new Content();

            while (cursor2.moveToNext()) {

                String data1 = cursor2.getString(0);
                String mimetype = cursor2.getString(1);
                //当类型为名字时
                if ("vnd.android.cursor.item/name".equals(mimetype)){
                    onecontact.setName(data1);
                }else if("vnd.android.cursor.item/phone_v2".equals(mimetype)){//当类型为电话号码时
                    onecontact.setNumber(data1);
                }
            }
            Log.i("哈哈哈哈哈2", onecontact.toString());
            contactslist.add(onecontact);//将信息存在contactslist中
        }
        return contactslist;
    }
}

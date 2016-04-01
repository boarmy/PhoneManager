package com.example.administrator.phonemanager;

import android.test.AndroidTestCase;

import com.example.administrator.dao.LockAppDao;
import com.example.administrator.db.MyLockAppDBHelper;

/**
 * Created by Administrator on 2016/4/1.
 */
public class MyDBtest extends AndroidTestCase {
    public void testApplockDb(){

        MyLockAppDBHelper helper = new MyLockAppDBHelper(getContext(),"lockapp.db",null,1);
        helper.getReadableDatabase();

    }
    public void testInsert(){

        LockAppDao dao = new LockAppDao(getContext());
        dao.inserttoDb("com.cskaoyan.myapp");
        //assertEquals(1, dao.inserttoDb("com.cskaoyan.myapp"));
        assertTrue(dao.isLocked("com.cskaoyan.myapp"));
    }


}

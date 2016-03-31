package com.example.administrator.phonemanager;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.example.administrator.utils.RunningServiceUtils;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    //测试RunningServiceUtils中服务是否开启的功能
    public void testrunning(){
        boolean isrunning = RunningServiceUtils.isrunning(getContext(), "com.example.administrator.utils.MyNumberLocationService");
        assertFalse(isrunning);//如果isrunning没有运行则为绿色
//        assertTrue(isrunning);//如果isrunning正在运行则为绿色

    }
}
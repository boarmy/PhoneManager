package com.example.administrator.phonemanager;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2016/3/29.
 */
        //使用系统提供的API实现屏幕的滑动事件
    //将监听函数和本activity函数关联起来 是能够监听
public abstract  class SetupBase extends Activity {

    GestureDetector gestureDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        gestureDetector = new GestureDetector(this,new MyGestureListener());
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    //监听函数
     class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent e1,//包含起点坐标
                               MotionEvent e2,//包含终点坐标
                               float velocityX,
                               float velocityY) {
            float startx = e1.getX();
            float starty = e1.getY();
            float endx = e2.getX();
            float endy = e2.getY();
            if (endx-startx>200){//右滑
                //显示左边的页面》
                previous(null);

            }else if(startx-endx>200){//坐滑，
                //显示右边的页面
                next(null);
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }
    public abstract void previous(View v);
    public abstract void next(View v);

}

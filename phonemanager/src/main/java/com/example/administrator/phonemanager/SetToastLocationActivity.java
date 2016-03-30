package com.example.administrator.phonemanager;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.administrator.application.MyApplication;

//用来让用户选择弹出对话框位置的activity
public class SetToastLocationActivity extends ActionBarActivity {

    private LinearLayout ll_setlocation_toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_toast_location);

        ll_setlocation_toast = (LinearLayout) findViewById(R.id.ll_setlocation_toast);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ll_setlocation_toast.getLayoutParams();
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;

        layoutParams.leftMargin = 200;
        layoutParams.topMargin = 300;

        //用来获取屏幕的尺寸
        WindowManager mWM = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display defaultDisplay = mWM.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);

        final int windowWidth = displayMetrics.widthPixels;
        final int windowHeight = displayMetrics.heightPixels;

        ll_setlocation_toast.setLayoutParams(layoutParams);
        ll_setlocation_toast.setOnTouchListener(new View.OnTouchListener() {
            float startx;
            float starty;
            float endx;
            float endy;

            int finalx;
            int finaly;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN://但点击屏幕落下的时候
                        startx = event.getRawX();//当前开始坐标
                        starty = event.getRawY();
                        Log.i("双击", "ACTION_MOVE"+startx+starty);
                        break;
                    case MotionEvent.ACTION_MOVE://移动的时候
                        endx = event.getRawX();
                        endy = event.getRawY();//得到当前终点的坐标

                        float dx = endx - startx;
                        float dy = endy - starty;

                        float oldx = ll_setlocation_toast.getX();
                        float oldy = ll_setlocation_toast.getY();

                        float oldrigth = oldx + ll_setlocation_toast.getWidth();
                        float oldbottom = oldy + ll_setlocation_toast.getHeight();

                        if (oldx + dx < 0 || oldrigth + dx > windowWidth || oldbottom + dy > windowHeight || oldy + dy < 0) {
                            break;
                        }
                        ll_setlocation_toast.layout((int) (oldx + dx), (int) (oldy + dy), (int) (oldrigth + dx), (int) (oldbottom + dy));

                        finalx = (int) (oldx + dx);
                        finaly = (int) (oldy + dy);
                        startx = endx;
                        starty = endy;
                        Log.i("双击", "ACTION_MOVE"+endx+endy);
                        break;
                    case MotionEvent.ACTION_UP:
                        MyApplication.setConfigValue("toastx", finalx);
                        MyApplication.setConfigValue("toasty", finaly);
                        Log.i("双击", "ACTION_UP");
                        break;
                }
                return false;
            }
        });

        ll_setlocation_toast.setOnClickListener(new View.OnClickListener() {
            boolean firstcilck = true;
            long firstclicktime;

            @Override
            public void onClick(View v) {
                if (firstcilck) {
                    firstclicktime = System.currentTimeMillis();
                    firstcilck = false;
                } else {
                    long secondclicktime = System.currentTimeMillis();
                    if (secondclicktime - firstclicktime <500) {
                        Log.i("双击","le");
                        Toast.makeText(SetToastLocationActivity.this, "双击", Toast.LENGTH_SHORT).show();
                        ll_setlocation_toast.layout(200, 300, 200 + ll_setlocation_toast.getWidth(), 300 + ll_setlocation_toast.getHeight());//左距离屏幕左的距离  上距离屏幕上的距离 右距离屏幕左的距离 下距离屏幕上的距离
                        MyApplication.setConfigValue("toastx", 200);
                        MyApplication.setConfigValue("toasty", 300);
                        firstcilck = true;
                    } else {
                        //重置
                        firstcilck = true;
                    }
                }
            }
        });
    }
}

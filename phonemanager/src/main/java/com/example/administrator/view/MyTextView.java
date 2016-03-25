package com.example.administrator.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/3/25.
 */
public class MyTextView extends TextView{
    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //使该控件一直处于选中状态 即一直获得焦点 这样才能保持跑马灯的效果
    @Override
    public boolean isFocused() {
//        return super.isFocused();
        return true;//使该控件一直处于选中状态
    }
}

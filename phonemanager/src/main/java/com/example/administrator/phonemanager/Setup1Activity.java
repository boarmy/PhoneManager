package com.example.administrator.phonemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Setup1Activity extends SetupBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);


    }

    @Override
    public void previous(View v) {

    }

    public void next(View v){
        startActivity(new Intent(this,Setup2Activity.class));
    }
}

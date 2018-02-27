package com.myzzt.chinaareaselector;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.myzzt.selectorlibrary.listener.IChoose;
import com.myzzt.selectorlibrary.model.ResultBean;
import com.myzzt.selectorlibrary.util.ChinaAreaDialog;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ChinaAreaDialog.with(this).setChooseListener(new IChoose() {
            @Override
            public void onChoose(ResultBean resultBean) {
                Log.d("onChoose", resultBean.getProvince_id());
            }
        }).show();
    }
}

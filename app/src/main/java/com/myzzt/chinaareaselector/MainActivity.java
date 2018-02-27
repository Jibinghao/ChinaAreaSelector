package com.myzzt.chinaareaselector;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.myzzt.selectorlibrary.listener.IChoose;
import com.myzzt.selectorlibrary.util.ChinaAreaDialog;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ChinaAreaDialog.with(this).setChooseListener(new IChoose() {
            @Override
            public void onChoose(String province_id, String city_id, String district_id, String province_title, String city_name, String district_name) {
                Log.d("onChoose", province_id + "=" + city_id + "=" + district_id + "=" + province_title + "=" + city_name + "=" + district_name);
            }
        }).show();
    }
}

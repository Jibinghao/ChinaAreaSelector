package com.myzzt.selectorlibrary.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.myzzt.selectorlibrary.listener.IChoose;
import com.myzzt.selectorlibrary.model.AreaBean;
import com.myzzt.selectorlibrary.model.ChinaAreaJsonBean;
import com.myzzt.selectorlibrary.model.ResultBean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jibinghao on 2018/2/27.
 */

public class ChinaAreaDialog {
    private static String TAG = "IChoose";
    private static List<ChinaAreaJsonBean.DataBean> options1Items = new ArrayList<>();
    private static ArrayList<ArrayList<AreaBean>> options2Items = new ArrayList<>();
    private static ArrayList<ArrayList<ArrayList<AreaBean>>> options3Items = new ArrayList<>();
    private String mCancelText = "取消";//左边取消按钮的文字
    private String mSubmitText = "确定";//右边确定按钮的文字
    private String mTitleText = "地区选择";//中间标题的文字
    private int mContentTextSize = 16;//滚轮文字大小
    private int mTitleSize = 16;//标题文字大小
    private int mDividerColor = Color.BLACK;//分割线的颜色
    private int mTextColorCenter = Color.BLACK;//选中项文字颜色
    private boolean mOutSideCancelable = true;//点击屏幕外面是否能够取消
    private int mTitleBgColor = Color.WHITE;//标题背景颜色
    private int mBgColor = Color.WHITE;//滚轮背景颜色
    private Activity mActivity;
    private String mJson;//如果传入新的json，那么将不会使用assets上的json
    private IChoose mIChoose;
    private int mOption1 = 0;//默认选中项1
    private int mOption2 = 0;//默认选中项2
    private int mOption3 = 0;//默认选中项3

    public static ChinaAreaDialog with(@NonNull Activity activity) {
        if (activity == null)
            throw new IllegalArgumentException("Activity不能为null");
        return new ChinaAreaDialog(activity);
    }

    public ChinaAreaDialog(Activity activity) {
        WeakReference<Activity> activityWeakReference = new WeakReference<>(activity);
        mActivity = activityWeakReference.get();
    }

    public ChinaAreaDialog setCancelText(String cancelText) {
        mCancelText = cancelText;
        return this;
    }

    public ChinaAreaDialog setSubmitText(String submitText) {
        mSubmitText = submitText;
        return this;
    }

    public ChinaAreaDialog setContentTextSize(int contentTextSize) {
        mContentTextSize = contentTextSize;
        return this;
    }

    public ChinaAreaDialog setTitleSize(int titleSize) {
        mTitleSize = titleSize;
        return this;
    }

    public ChinaAreaDialog setTitleText(String titleText) {
        mTitleText = titleText;
        return this;
    }

    public ChinaAreaDialog setDividerColor(int dividerColor) {
        mDividerColor = dividerColor;
        return this;
    }

    public ChinaAreaDialog setTextColorCenter(int textColorCenter) {
        mTextColorCenter = textColorCenter;
        return this;
    }

    public ChinaAreaDialog setOutSideCancelable(boolean outSideCancelable) {
        mOutSideCancelable = outSideCancelable;
        return this;
    }

    public ChinaAreaDialog setTitleBgColor(int titleBgColor) {
        mTitleBgColor = titleBgColor;
        return this;
    }

    public ChinaAreaDialog setBgColor(int bgColor) {
        mBgColor = bgColor;
        return this;
    }

    public ChinaAreaDialog setJson(String json) {
        mJson = json;
        return this;
    }

    public ChinaAreaDialog setChooseListener(IChoose iChoose) {
        mIChoose = iChoose;
        return this;
    }

    public ChinaAreaDialog setOption1(int option1) {
        mOption1 = option1;
        return this;
    }

    public ChinaAreaDialog setOption2(int option2) {
        mOption2 = option2;
        return this;
    }

    public ChinaAreaDialog setOption3(int option3) {
        mOption3 = option3;
        return this;
    }


    public void show() {
        if (mIChoose == null) {
            Log.e(TAG, "缺少IChoose");
            return;
        }
        if (mActivity == null) {
            Log.e(TAG, "缺少activity");
            return;
        }
        initJsonData();
        OptionsPickerView pvOptions = new OptionsPickerBuilder(mActivity, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                if (mIChoose != null) {
                    ChinaAreaJsonBean.DataBean province = options1Items.get(options1);
                    AreaBean city = options2Items.get(options1).get(options2);
                    AreaBean district = options3Items.get(options1).get(options2).get(options3);
                    ResultBean resultBean = new ResultBean();
                    resultBean.setCity_id(city.getId());
                    resultBean.setCity_name(city.getTitle());
                    resultBean.setDistrict_id(district.getId());
                    resultBean.setDistrict_name(district.getTitle());
                    resultBean.setProvince_id(province.getId());
                    resultBean.setProvince_title(province.getTitle());
                    mIChoose.onChoose(resultBean);
                }
            }
        })
                .setCancelText(mCancelText)//取消按钮文字
                .setSubmitText(mSubmitText)//确认按钮文字
                .setContentTextSize(mContentTextSize)//滚轮文字大小
                .setTitleSize(mTitleSize)//标题文字大小
                .setTitleText(mTitleText)
                .setDividerColor(mDividerColor)
                .setTextColorCenter(mTextColorCenter) //设置选中项文字颜色
                .setOutSideCancelable(mOutSideCancelable)// default is true
                .setTitleBgColor(mTitleBgColor)//标题背景颜色
                .setBgColor(mBgColor)//滚轮背景颜色
                .setSelectOptions(mOption1, mOption2, mOption3)
                .build();
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }


    public String getJson(Context context, String fileName) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public List<ChinaAreaJsonBean.DataBean> parseData(String result) {
        //Gson 解析
        try {
            Gson gson = new Gson();
            ChinaAreaJsonBean chinaAreaJsonBean = gson.fromJson(result, ChinaAreaJsonBean.class);
            return chinaAreaJsonBean.getData();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "json数据格式不正确");
        }
        return null;
    }


    private void initJsonData() {
        String jsonData;
        if (TextUtils.isEmpty(mJson)) {
            jsonData = getJson(mActivity, "china.json");//获取assets目录下的json文件数据
        } else {
            jsonData = mJson;
        }

        List<ChinaAreaJsonBean.DataBean> jsonBean = new ArrayList<>();//用Gson 转成实体
        try {
            jsonBean = parseData(jsonData);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "json数据格式不正确");
        }
        options1Items = jsonBean;
        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<AreaBean> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<AreaBean>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCity().size(); c++) {//遍历该省份的所有城市
                String cityName = jsonBean.get(i).getCity().get(c).getTitle();
                String cityId = jsonBean.get(i).getCity().get(c).getId();
                AreaBean areaBean = new AreaBean();
                areaBean.setId(cityId);
                areaBean.setTitle(cityName);
                CityList.add(areaBean);//添加城市

                ArrayList<AreaBean> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCity().get(c).getDistrict() == null
                        || jsonBean.get(i).getCity().get(c).getDistrict().size() == 0) {
                    AreaBean areaBean1 = new AreaBean();
                    City_AreaList.add(areaBean1);
                } else {

                    for (int d = 0; d < jsonBean.get(i).getCity().get(c).getDistrict().size(); d++) {//该城市对应地区所有数据
                        String areaName = jsonBean.get(i).getCity().get(c).getDistrict().get(d).getTitle();
                        String areaId = jsonBean.get(i).getCity().get(c).getDistrict().get(d).getId();
                        AreaBean areaBean1 = new AreaBean();
                        areaBean1.setId(areaId);
                        areaBean1.setTitle(areaName);
                        City_AreaList.add(areaBean1);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }
    }
}


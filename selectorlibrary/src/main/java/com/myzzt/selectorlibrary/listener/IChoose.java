package com.myzzt.selectorlibrary.listener;

/**
 * Created by jibinghao on 2018/2/27.
 */

public interface IChoose {
    /**
     * 由于被屏蔽了，对应以下的字段
     * 省id,市id,区id,省名字，市名字，区名字
     * String province_id, String city_id, String district_id, String province_title, String city_name, String district_name
     */

    void onChoose(String province_id, String city_id, String district_id, String province_title, String city_name, String district_name);

}

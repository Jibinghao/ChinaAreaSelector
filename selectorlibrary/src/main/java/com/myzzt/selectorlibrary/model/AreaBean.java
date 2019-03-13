package com.myzzt.selectorlibrary.model;

import com.contrarywind.interfaces.IPickerViewData;

/**
 * Created by jibinghao on 2018/2/27.
 */



public class AreaBean implements IPickerViewData {
    private String id;
    private String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getPickerViewText() {
        return this.title;
    }
}

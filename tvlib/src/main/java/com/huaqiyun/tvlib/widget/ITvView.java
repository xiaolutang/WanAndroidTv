package com.huaqiyun.tvlib.widget;

public interface ITvView {
    /**
     * 设置获取到焦点的放大比例
     * */
    void setFocusScale(float scale);

    /**
     * 获取焦点的颜色
     * */
    void setFocusColor(int focusColor);

    /**
     * 正常状态下的颜色
     * */
    void setNormalColor(int normalColor);

    /**
     * 设置选中的颜色
     * */
    void setSelectColor(int selectColor);

    /**
     * 设置元素上焦点的时候是否设置选中
     * */
    void setFocusSelect(boolean focusSelect);

}

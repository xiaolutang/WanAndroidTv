package com.huaqiyun.tvlib.widget;

public interface ITvViewGroup extends ITvView {
    /**
     * 设置焦点的查找方式
     * default,priority,dynamic
     * default 按照系统的焦点处理方式进行查找
     * priority 按优先级进行查找
     * dynamic 动态记忆焦点
     * */
    void setFocusSearchMode(String searchMode);
}

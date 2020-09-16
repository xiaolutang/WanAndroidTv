package com.txl.wanandroidtv.ui.adpater;

import com.txl.ui_basic.adapter.NavTypeSpec;

/**
 * Copyright (c) 2020 唐小陆 All rights reserved.
 * author：txl
 * date：2020/9/16
 * description：wanAndroid 列表元素显示集合
 */
public class WanAndroidListItemType {
    /**
     * 文本标题
     * */
    public static final int TYPE_TEXT_TITLE = NavTypeSpec.makeMeasureSpec(0,NavTypeSpec.MODE_COMMON_ELEMENT);
    /**
     * 轮播图
     * */
    public static final int TYPE_BANNER = NavTypeSpec.makeMeasureSpec(1,NavTypeSpec.MODE_COMMON_ELEMENT);
    /**
     * 普通列表元素
     * */
    public static final int TYPE_COMMON = NavTypeSpec.makeMeasureSpec(2,NavTypeSpec.MODE_COMMON_ELEMENT);
    /**
     * 置顶数据
     * */
    public static final int TYPE_TOP = NavTypeSpec.makeMeasureSpec(3,NavTypeSpec.MODE_COMMON_ELEMENT);



}

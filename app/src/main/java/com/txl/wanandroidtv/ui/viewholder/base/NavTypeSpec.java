package com.txl.wanandroidtv.ui.viewholder.base;

import androidx.annotation.IntDef;
import androidx.annotation.IntRange;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 参考View的MeasureSpec将高4位用作数据类型，低28为作为ui显示
 * 现有的数据：0:组件;1:普通页面元素
 * */
public class NavTypeSpec {
    private static final int MODE_SHIFT = 28;
    private static final int MODE_MASK  = 0xf << MODE_SHIFT;

    @IntDef({MODE_COMMON_ELEMENT, MODE_COMPONENT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface NavTypeSpecMode {}

    /**
     * 普通元素
     * */
    public static final int MODE_COMMON_ELEMENT = 0 << MODE_SHIFT;
    /**
     * 组件
     * */
    public static final int MODE_COMPONENT = 1 << MODE_SHIFT;

    public static int makeMeasureSpec(@IntRange(from = 0, to = (1 << NavTypeSpec.MODE_SHIFT) - 1) int ui,
                                      @NavTypeSpecMode int mode) {
        return (ui & ~MODE_MASK) | (mode & MODE_MASK);
    }

    public static int getMode(int navTypeSpec) {
        //noinspection ResourceType
        return (navTypeSpec & MODE_MASK);
    }

    public static int getUi(int navTypeSpec) {
        return (navTypeSpec & ~MODE_MASK);
    }


    //定义组件类型type
    /**
     * 自定义跳转(宫格)
     * */
    public static final int COMPONENT_501 = NavTypeSpec.makeMeasureSpec(0,MODE_COMPONENT);
    /**
     * 指定数据列表
     * */
    public static final int COMPONENT_502 = NavTypeSpec.makeMeasureSpec(1,MODE_COMPONENT);
    /**
     * 轮播组件
     * */
    public static final int COMPONENT_503 = NavTypeSpec.makeMeasureSpec(2,MODE_COMPONENT);



    //定义普通元素
    public static final int ELEMENT_COMMON = NavTypeSpec.makeMeasureSpec(0,MODE_COMMON_ELEMENT);
    /**
     * 501中的元素
     * */
    public static final int ELEMENT_501 = NavTypeSpec.makeMeasureSpec(1,MODE_COMMON_ELEMENT);
    /**
     * 502中的元素
     * */
    public static final int ELEMENT_502 = NavTypeSpec.makeMeasureSpec(2,MODE_COMMON_ELEMENT);



}

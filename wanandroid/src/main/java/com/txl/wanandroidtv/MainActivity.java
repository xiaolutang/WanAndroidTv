package com.txl.wanandroidtv;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.mmkv.MMKV;
import com.txl.tvlib.config.TvLibConfig;
import com.txl.tvlib.widget.dynamic.focus.LibTvRecyclerView2;
import com.txl.ui_basic.BaseActivity;
import com.txl.wanandroidtv.bean.NavItemData;
import com.txl.wanandroidtv.ui.adpater.MainNavPageAdapter;
import com.txl.wanandroidtv.ui.adpater.NavRecyclerAdapter;
import com.txl.wanandroidtv.ui.utils.ThemeUtils;
import com.txl.wanandroidtv.utils.Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends WanAndroidBaseActivity {

    private LibTvRecyclerView2 navRecyclerView;
    private ViewPager viewPager;
    private MainNavPageAdapter viewpagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MMKV.initialize(this);
        //初始化焦点框架，可以考虑放在application里面
        TvLibConfig config = new TvLibConfig.Builder()
                .setBorderColor(ThemeUtils.INSTANCE.getThemeColor(this))
                .setBorderWidth(getResources().getDimensionPixelSize(R.dimen.dp_5))
                .setHasSelectBorder(false)
                .build();
        TvLibConfig.Companion.setDefaultConfig(config);
        initView();
        initData();
    }

    private void initView(){
        navRecyclerView = findViewById( R.id.recycler_nav );
        navRecyclerView.setLayoutManager( new LinearLayoutManager( this,LinearLayoutManager.HORIZONTAL,false ) );
        navRecyclerView.openFocusDynamic(true);
        viewPager = findViewById(R.id.main_view_pager);
        viewPager.setOffscreenPageLimit(3);

    }

    private void initData(){
        String json = Utils.inputStreamToString(getResources()
                .openRawResource(R.raw.main_nav));
        Gson gson = new Gson();
        Type collectionType = new TypeToken<ArrayList<NavItemData>>(){}.getType();
        ArrayList<NavItemData> navs = gson.fromJson(json, collectionType);
        NavRecyclerAdapter adapter = new NavRecyclerAdapter(navs);
        navRecyclerView.setAdapter( adapter );
//        navRecyclerView.bindViewPager(viewPager);
        viewpagerAdapter = new MainNavPageAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, navs);

        viewPager.setAdapter(viewpagerAdapter);
        navRecyclerView.bindViewPager(viewPager);
//        navRecyclerView.setOnCheckedChangeListener((group, checkedView, position) -> viewPager.setCurrentItem(position));
    }
}

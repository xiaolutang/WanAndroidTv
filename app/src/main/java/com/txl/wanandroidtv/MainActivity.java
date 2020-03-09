package com.txl.wanandroidtv;

import android.os.Bundle;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.txl.tvlib.widget.dynamic.focus.LibTvRecyclerView;
import com.txl.screenadaptation.ScreenAdaptionBaseActivity;
import com.txl.wanandroidtv.bean.Nav;
import com.txl.wanandroidtv.bean.NavItemData;
import com.txl.wanandroidtv.ui.adpater.BaseRecyclerAdapter;
import com.txl.wanandroidtv.ui.adpater.NavRecyclerAdapter;
import com.txl.wanandroidtv.ui.viewholder.NavItemViewHolder;
import com.txl.wanandroidtv.utils.Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends ScreenAdaptionBaseActivity {

    private LibTvRecyclerView navRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initView(){
        navRecyclerView = findViewById( R.id.recycler_nav );
        navRecyclerView.setLayoutManager( new LinearLayoutManager( this,LinearLayoutManager.HORIZONTAL,false ) );
    }

    private void initData(){
        String json = Utils.inputStreamToString(getResources()
                .openRawResource(R.raw.main_nav));
        Gson gson = new Gson();
        Type collectionType = new TypeToken<ArrayList<NavItemData>>(){}.getType();
        ArrayList<NavItemData> navs = gson.fromJson(json, collectionType);
        NavRecyclerAdapter adapter = new NavRecyclerAdapter(navs,this);
        navRecyclerView.setAdapter( adapter );
    }
}

package com.txl.wanandroidtv;

import android.os.Bundle;


import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.txl.tvlib.widget.dynamic.focus.LibTvRecyclerView;
import com.txl.screenadaptation.ScreenAdaptionBaseActivity;
import com.txl.wanandroidtv.bean.Nav;
import com.txl.wanandroidtv.utils.Utils;

public class MainActivity extends ScreenAdaptionBaseActivity {

    private LibTvRecyclerView navRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        navRecyclerView = findViewById( R.id.recycler_nav );
        navRecyclerView.setLayoutManager( new LinearLayoutManager( this,LinearLayoutManager.HORIZONTAL,false ) );
        String json = Utils.inputStreamToString(getResources()
                .openRawResource(R.raw.main_nav));
        Gson gson = new Gson();
        Nav nav = gson.fromJson(json, Nav.class);

    }
}

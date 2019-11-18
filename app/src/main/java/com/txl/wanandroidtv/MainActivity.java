package com.txl.wanandroidtv;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.FocusHighlightHelper;
import androidx.leanback.widget.HorizontalGridView;
import androidx.leanback.widget.ItemBridgeAdapter;
import androidx.leanback.widget.ObjectAdapter;

import com.txl.wanandroidtv.bean.Nav;
import com.txl.wanandroidtv.bean.NavItemData;
import com.txl.wanandroidtv.card.NvaPresenter;
import com.google.gson.Gson;
import com.txl.commonlibrary.utils.Utils;
import com.txl.commonlibrary.utils.exector.AppExecutors;
import com.txl.screenadaptation.ScreenAdaptionBaseActivity;
import com.txl.tvlib.card.BaseCardKt;
import com.txl.tvlib.card.TextCard;
import com.txl.tvlib.focushandler.ViewFocusChangeListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ScreenAdaptionBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DisplayMetrics displayMetrics = getApplication().getResources().getDisplayMetrics();
        Log.e(TAG,"onCreate before width : "+displayMetrics.widthPixels + "  height : "+displayMetrics.heightPixels + " displayMetrics : "+displayMetrics.density);
        super.onCreate(savedInstanceState);
        displayMetrics = getApplication().getResources().getDisplayMetrics();
        Log.e(TAG,"onCreate after width : "+displayMetrics.widthPixels + "  height : "+displayMetrics.heightPixels + " displayMetrics : "+displayMetrics.density);
        setContentView(R.layout.activity_main);
        findViewById(R.id.main_action_search).setOnFocusChangeListener(new ViewFocusChangeListener(findViewById(R.id.main_action_search)));
        findViewById(R.id.main_action_mine).setOnFocusChangeListener(new ViewFocusChangeListener(findViewById(R.id.main_action_mine)));
        findViewById(R.id.main_action_member).setOnFocusChangeListener(new ViewFocusChangeListener(findViewById(R.id.main_action_member)));
        initNavView();
    }

    private void initNavView() {
        HorizontalGridView horizontalGridView = findViewById(R.id.nav_horizontal_recycler_view);
        ItemBridgeAdapter itemBridgeAdapter = new ItemBridgeAdapter();
        FocusHighlightHelper.setupHeaderItemFocusHighlight(itemBridgeAdapter);
        horizontalGridView.setAdapter(itemBridgeAdapter);
        NvaPresenter navPresenter = new NvaPresenter();
        ArrayObjectAdapter objectAdapter = new ArrayObjectAdapter(navPresenter);
        itemBridgeAdapter.setAdapter(objectAdapter);
        String json = Utils.inputStreamToString(getResources()
                .openRawResource(R.raw.main_nav));
        Gson gson = new Gson();
        Nav nav = gson.fromJson(json, Nav.class);
        //考虑是否引入rxjava进行链式处理
        AppExecutors.execDiskIo(new Runnable() {
            @Override
            public void run() {
                List<TextCard> textCards = new ArrayList<>();
                for (NavItemData data:nav.getNav()){
                    textCards.add(new TextCard(BaseCardKt.TYPE_NAV_TEXT,data.getTitle(),data));
                }
                AppExecutors.execMainThread(new Runnable() {
                    @Override
                    public void run() {
                        objectAdapter.addAll(0,textCards);
                    }
                });
            }
        });
    }
}

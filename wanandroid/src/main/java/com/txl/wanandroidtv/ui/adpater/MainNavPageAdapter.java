package com.txl.wanandroidtv.ui.adpater;

import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.txl.txllog.AndroidLogWrapper;
import com.txl.wanandroidtv.bean.NavItemData;
import com.txl.wanandroidtv.ui.fragment.HomeNavFragment;
import com.txl.wanandroidtv.ui.fragment.NavFragmentFactory;

import java.util.ArrayList;

public class MainNavPageAdapter extends FragmentStatePagerAdapter {
    private final String TAG = "MainNavPageAdapter";
    private final SparseArray<Fragment> fragmentList = new SparseArray<>();
    private final ArrayList<NavItemData> navs;

    public MainNavPageAdapter(@NonNull FragmentManager fm, int behavior, ArrayList<NavItemData> navs) {
        super(fm, behavior);
        this.navs = navs;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        String category = navs.get(position).getCategory();
        AndroidLogWrapper.d(TAG,"position : "+position+"  category "+category );
        Fragment fragment = fragmentList.get(position);
        if(fragment == null){
            fragment = NavFragmentFactory.createFragmentByCategory(category);
            fragmentList.put(position,fragment);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return navs.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
        fragmentList.remove(position);
    }
}

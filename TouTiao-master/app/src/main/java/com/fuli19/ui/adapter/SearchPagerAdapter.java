package com.fuli19.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.fuli19.ui.fragment.SearchFragment;

public class SearchPagerAdapter extends FragmentStatePagerAdapter {

    private String[] titles = {"综合", "视频", "图片", "文章"};

    private String key;

    public SearchPagerAdapter(FragmentManager fm, String key) {
        super(fm);
        this.key = key;
    }

    @Override
    public Fragment getItem(int position) {
        return SearchFragment.newInstance(position, key);
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

}

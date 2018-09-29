package com.fuli19.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fuli19.ui.fragment.DynamicFragment;

public class PersonalPagerAdapter extends FragmentPagerAdapter {

    private String [] titles = {"动态","视频","图片","文章"};

    public PersonalPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return DynamicFragment.newInstance(position);
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

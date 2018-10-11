package com.fuli19.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fuli19.ui.fragment.AttentionFragment;

public class AttentionPagerAdapter extends FragmentPagerAdapter {

    private String[] titles = {"关注", "粉丝"};

    public AttentionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return AttentionFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

}

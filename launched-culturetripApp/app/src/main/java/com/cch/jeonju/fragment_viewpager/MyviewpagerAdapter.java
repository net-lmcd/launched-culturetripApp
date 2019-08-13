package com.cch.jeonju.fragment_viewpager;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class MyviewpagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> mData;

    public MyviewpagerAdapter(FragmentManager fm) {
        super(fm);

        mData = new ArrayList<>();
        mData.add( new AllpostFragment());
        mData.add( new MypostFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return mData.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String pageTitle = null;
        switch(position) {
            case 0:
                pageTitle = "All Post";
                break;
            case 1:
                pageTitle = "My post";
                break;
        }
        return pageTitle;
    }
}

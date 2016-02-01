package com.beuth.ebp.smartshop;

/**
 * Created by waelgabsi on 05.01.16.
 */

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public ListFragment getItem(int position) {

        switch (position) {
            case 0:
                MyListFragment tab1 = new MyListFragment();
                return tab1;
            case 1:
                MyListFragment tab2 = new MyListFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
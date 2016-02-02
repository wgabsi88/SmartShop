package com.beuth.ebp.smartshop;

/**
 * Created by waelgabsi on 05.01.16.
 */

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;

import java.io.Serializable;
import java.util.List;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    List<Item> rowitems;

    public PagerAdapter(FragmentManager fm, int NumOfTabs,List<Item> rowitems) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.rowitems = rowitems;
    }

    @Override
    public ListFragment getItem(int position) {

        switch (position) {
            case 0:
                MyListFragment tab1 = new MyListFragment();
                Bundle args = new Bundle();
                args.putSerializable("asd", (Serializable) rowitems);
                tab1.setArguments(args);
                return tab1;
            case 1:
                MyListFragment tab2 = new MyListFragment();
                Bundle argss = new Bundle();
                argss.putSerializable("asd", (Serializable) rowitems);
                tab2.setArguments(argss);
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
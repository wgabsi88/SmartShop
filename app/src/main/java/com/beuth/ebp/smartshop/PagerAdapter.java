package com.beuth.ebp.smartshop;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;

import java.io.Serializable;
import java.util.List;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    List<Item> rowitems;
    List<Order> roworders;

    public PagerAdapter(FragmentManager fm, int NumOfTabs, List<Item> rowitems, List<Order> roworders) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.rowitems = rowitems;
        this.roworders = roworders;
    }

    @Override
    public ListFragment getItem(int position) {

        switch (position) {
            case 0:
                ProductFragment tab1 = new ProductFragment();
                Bundle args = new Bundle();
                args.putSerializable("rowitems", (Serializable) rowitems);
                tab1.setArguments(args);
                return tab1;
            case 1:
                OrderFragment tab2 = new OrderFragment();
                Bundle argss = new Bundle();
                argss.putSerializable("roworders", (Serializable) roworders);
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
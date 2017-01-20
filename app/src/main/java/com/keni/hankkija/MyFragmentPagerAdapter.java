package com.keni.hankkija;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Keni on 2017-01-03.
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "Tulokset", "Yhteistulokset", "top lista" };
    private Context context;


    public MyFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                StatFragment statFragment = new StatFragment();
                return statFragment;
            case 1:
               AllFragment allFragment = new AllFragment();
                return allFragment;
            case 2:
                TopFragment topFragment = new TopFragment();
                return topFragment;
            default:
                return null;
        }
    }


    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}

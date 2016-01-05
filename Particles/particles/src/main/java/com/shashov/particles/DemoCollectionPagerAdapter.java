package com.shashov.particles;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by kirill on 29.12.2015.
 */
public class DemoCollectionPagerAdapter  extends FragmentStatePagerAdapter {
    private ArrayList<Fragment> fragments = new ArrayList<>();

    public DemoCollectionPagerAdapter(FragmentManager fm) {
        super(fm);

        Fragment fragment1 = new MainFragment();
        Fragment fragment2 = new SettingsFragment();

        fragments.add(fragment1);
        fragments.add(fragment2);
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "ПЫЩ ПЫЩ с гравитацией";
        } else {
            return "Настройки";
        }
    }
}

package com.example.asian;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    FragmentTabOne fragmentTabOne = new FragmentTabOne();
    FragmentTabTwo fragmentTabTwo = new FragmentTabTwo();
    FragmentTabThree fragmentTabThree = new FragmentTabThree();

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                return fragmentTabTwo;
            case 2:
                return fragmentTabThree;
            case 0:
            default:
                return fragmentTabOne;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = "Tab-1";
                break;
            case 1:
                title = "Tab-2";
                break;
            case 2:
                title = "Tab-3";
                break;
        }
        return title;
    }
}

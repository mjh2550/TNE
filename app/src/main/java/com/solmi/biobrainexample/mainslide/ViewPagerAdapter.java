package com.solmi.biobrainexample.mainslide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {


    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    /**
     *
     * fragment 화면 구현하는 곳
     */
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return HomeFrag1.newInstance();
            case 1:
                return HomeFrag2.newInstance();
            case 2:
                return HomeFrag3.newInstance();
            default:
                return null;

        }
        //return null;
    }

    //페이지 수
    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "1page";
            case 1:
                return "2page";
            case 2:
                return "3page";
            default:
                return null;

        }
    }
}

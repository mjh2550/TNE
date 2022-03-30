package com.solmi.biobrainexample.mainslide;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class FragmentStatePagerAdapter extends androidx.fragment.app.FragmentStatePagerAdapter {

    /**
     * 프래그먼트 상태 관리 어댑터
     */
    public FragmentStatePagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }
}

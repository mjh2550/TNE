package com.solmi.biobrainexample.mainslide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.solmi.biobrainexample.R;

public class HomeFrag3 extends Fragment {

    View view;

    /**
     viewpager는 상태저장이 필요함
     */
    public static HomeFrag3 newInstance() {
        HomeFrag3 homeFrag3 = new HomeFrag3();
        return homeFrag3;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.frag_homeslide3,container,false);
        return view;
    }
}

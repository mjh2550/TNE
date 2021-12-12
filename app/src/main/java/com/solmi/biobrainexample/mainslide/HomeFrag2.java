package com.solmi.biobrainexample.mainslide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.solmi.biobrainexample.R;

public class HomeFrag2 extends Fragment {
    View view;

    /**
     viewpager는 상태저장이 필요함
     */
    public static HomeFrag2 newInstance() {
        HomeFrag2 homeFrag2 = new HomeFrag2();
        return homeFrag2;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.frag_homeslide2,container,false);
        return view;
    }
}

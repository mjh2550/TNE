package com.solmi.biobrainexample.mainslide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.solmi.biobrainexample.R;

public class HomeFrag1 extends Fragment {

    private View view;

   /*public HomeFrag1(){

        super(R.layout.frag_homeslide1);
    }*/

    /**
     viewpager는 상태저장이 필요함
     */
    public static HomeFrag1 newInstance() {
        HomeFrag1 homeFrag1 = new HomeFrag1();
        return homeFrag1;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       // return super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.frag_homeslide1,container,false);

        return view;
    }
}

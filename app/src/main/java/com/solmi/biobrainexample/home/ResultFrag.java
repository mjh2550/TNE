package com.solmi.biobrainexample.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.solmi.biobrainexample.R;

public class ResultFrag extends Fragment {

    private View view;

    public ResultFrag(){
        super(R.layout.frag_result);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.frag_result,container,false);

        return view;
    }



}

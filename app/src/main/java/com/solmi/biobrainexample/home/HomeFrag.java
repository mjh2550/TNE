package com.solmi.biobrainexample.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.solmi.biobrainexample.BioStartActivity;
import com.solmi.biobrainexample.MainActivity;
import com.solmi.biobrainexample.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeFrag extends Fragment {

    private View view;


    @BindView(R.id.btn_go_measure)
    Button btnMeasure;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

       view = inflater.inflate(R.layout.frag_home,container,false);
       ButterKnife.bind(this,view);
       btnMeasure = view.findViewById(R.id.btn_go_measure);

       return view;
    }


    @OnClick(R.id.btn_go_measure)
    public void onClickBtnMeasure(){

        Intent intent = new Intent(getActivity(), BioStartActivity.class);
        startActivity(intent);

    }









}

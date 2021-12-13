package com.solmi.biobrainexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

       /*
        btnMeasure.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                 //fragment는 항상 activity 위에 있어야 하고, this 사용 불가능, getactivity로 context를 받아옴
                Intent intent = new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
            }
        });
        */

        return view;
    }


    @OnClick(R.id.btn_go_measure)
    public void onClickBtnMeasure(){

        Intent intent = new Intent(getActivity(),MainActivity.class);
        startActivity(intent);

    }









}

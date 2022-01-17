package com.solmi.biobrainexample.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.solmi.biobrainexample.mainslide.ViewPagerAdapter;
import com.solmi.biobrainexample.process.BioStartActivity;
import com.solmi.biobrainexample.R;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeFrag extends Fragment {

    private View view;

    String TAG ="TEST<<<<<";

    @BindView(R.id.btn_go_measure)
    Button btnMeasure;

    private FragmentPagerAdapter fragmentPagerAdapter;
    private FragmentManager fragmentManager;

    final long DELAY_MS = 3000;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 5000; // time in milliseconds between successive task executions.
    int currentPage = 0;


    @BindView(R.id.viewPager)
       ViewPager viewPager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

       fragmentManager = getActivity().getSupportFragmentManager();

       view = inflater.inflate(R.layout.frag_home,container,false);
       ButterKnife.bind(this,view);
       btnMeasure = view.findViewById(R.id.btn_go_measure);
        Log.i("gogogogo<<<<<<", "onCreateView: "+"adgagagag");

        setSlideViewPager();



       return view;
    }


    @OnClick(R.id.btn_go_measure)
    public void onClickBtnMeasure(){

        Intent intent = new Intent(getActivity(), BioStartActivity.class);
        startActivity(intent);

    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause: ");
        currentPage=0;
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        currentPage=0;
        super.onDestroy();

    }

    private void setSlideViewPager() {

        //1페이지 메인 슬라이드 배너 뷰페이저 설정
        fragmentPagerAdapter = new ViewPagerAdapter(fragmentManager);
        viewPager.setAdapter(fragmentPagerAdapter);
        currentPage=0;
        viewPager.setCurrentItem(0);
       // viewPager.setOffscreenPageLimit(fragmentPagerAdapte);

        //자동 슬라이드 기능
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            @Override
            public void run() {
                if(currentPage >= fragmentPagerAdapter.getCount()) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage, true);
                Log.i("timer", "run: "+currentPage);
                currentPage++;
            }


        };

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.i(TAG, "run: ");
                handler.post(Update);
            }

            @Override
            public boolean cancel() {
                Log.i(TAG, "cancel: ");
                return super.cancel();
            }
        }, DELAY_MS, PERIOD_MS);


    }

}

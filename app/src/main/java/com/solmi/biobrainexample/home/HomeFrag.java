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
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
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
    Timer timer;
    TimerTask timerTask;
    Handler handler;
    Runnable Update;

    //@BindView(R.id.btn_go_measure)
    Button btnMeasure;

    private FragmentPagerAdapter fragmentPagerAdapter;
    private FragmentManager fragmentManager;
    private FragmentTransaction ft;

    final long DELAY_MS = 3000;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 5000; // time in milliseconds between successive task executions.
    int currentPage = 0;


  //  @BindView(R.id.viewPager)
     ViewPager viewPager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        Log.i(TAG, "onCreateView: ");
        view = inflater.inflate(R.layout.frag_home,container,false);
       viewPager = view.findViewById(R.id.viewPager);
       btnMeasure = view.findViewById(R.id.btn_go_measure);
       ButterKnife.bind(this,view);
       //setSlideViewPager();
       fragmentManager = getChildFragmentManager();//getActivity().getSupportFragmentManager();
       return view;
    }


    @OnClick(R.id.btn_go_measure)
    public void onClickBtnMeasure(){

        Intent intent = new Intent(getActivity(), BioStartActivity.class);
        startActivity(intent);

    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume: ");
        super.onResume();
        setSlideViewPager();

    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause: ");
        super.onPause();
        if(timerTask!=null){
            timer.cancel();
            timer.purge();
            handler=null;
            Update=null;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
        /*currentPage=0;
        viewPager.setCurrentItem(0);*/
        if(timerTask!=null){
            timer.cancel();
            timer.purge();
            handler=null;
            Update=null;
        }



    }

    private void setSlideViewPager() {

        //1페이지 메인 슬라이드 배너 뷰페이저 설정
        if(fragmentPagerAdapter==null) {
            fragmentPagerAdapter = new ViewPagerAdapter(fragmentManager);
            viewPager.setAdapter(fragmentPagerAdapter);
        }

        moveSlide();

    }

    private void moveSlide(){

        currentPage=0;
        //viewPager.setCurrentItem(0);
        // viewPager.setOffscreenPageLimit(fragmentPagerAdapte);

        //자동 슬라이드 기능
        if(handler==null) handler = new Handler();
        if(Update==null) Update = new Runnable() {
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

        timer = new Timer();
        timerTask = new TimerTask() {
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
        };

        timer.schedule(timerTask, DELAY_MS, PERIOD_MS);


    }

}

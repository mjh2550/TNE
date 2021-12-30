package com.solmi.biobrainexample.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.solmi.biobrainexample.R;
import com.solmi.biobrainexample.mainslide.ViewPagerAdapter;
import com.solmi.biobrainexample.process.BioStart1Fragment;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.nav_view)
    BottomNavigationView bottomNavigationView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private HomeFrag homeFrag;
    private ResultFrag resultFrag;
    private InfoFrag infoFrag;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private BioStart1Fragment b1f;

    private FragmentPagerAdapter fragmentPagerAdapter;


    int currentPage = 0;
    final long DELAY_MS = 1000;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 5000; // time in milliseconds between successive task executions.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);


        //상단 툴바 설정
        //  toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);//패키지 명 주의, 2개인데 여기서는 androidx꺼를 사용함
        ActionBar actionBar = getSupportActionBar();//이거역시 androidx
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);//기본 제목을 없애줍니다.
        actionBar.setDisplayHomeAsUpEnabled(true); //뒤로가기 자동생성


        //하단 바 선택 1,2,3
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.navigation_1:
                        setFrag(0);
                        break;
                    case R.id.navigation_2:
                        setFrag(1);
                        break;
                    case R.id.navigation_3:
                        setFrag(2);
                        break;
                }
                return true;

            }
        });

        //기본 네비 뷰 설정
        homeFrag = new HomeFrag();
        resultFrag = new ResultFrag();
        infoFrag = new InfoFrag();
        setFrag(0);

        //1페이지 메인 슬라이드 배너 뷰페이저 설정
        fragmentPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentPagerAdapter);


        //자동 슬라이드 기능
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            @Override
            public void run() {
                if(currentPage == fragmentPagerAdapter.getCount()) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);


    }

    /**
     *하단 바 프래그먼트 교체 함수
     * */
    private void setFrag(int n)
    {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (n)
        {
            case 0:
                ft.replace(R.id.content_layout,homeFrag);
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.content_layout,resultFrag);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.content_layout,infoFrag);
                ft.commit();
                break;
        }
    }


    /**
     * munyItem 생성 및 초기화
     * Menu Inflater를 통하여 XML Menu 리소스에 정의된 내용을 파싱 하여 Menu 객체를 생성하고 추가
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.top_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 상단 바 menuItem 클릭시 동작할 이벤트
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                //select logout item
                break;
            case R.id.account:
                //select account item
                break;
            case android.R.id.home:
                //select back button
                finish();//뒤로가기 누를 시 (일단 종료 )
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
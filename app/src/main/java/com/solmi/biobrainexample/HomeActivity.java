package com.solmi.biobrainexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import butterknife.BindView;

public class HomeActivity extends AppCompatActivity {


    @BindView(R.id.nav_view)
    BottomNavigationView bottomNavigationView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private HomeFrag homeFrag;
    private ResultFrag resultFrag;
    private InfoFrag infoFrag;
    private FragmentManager fm;
    private FragmentTransaction ft;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


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

    /**
     * 롤링 배너
     * 1. 마지막 위치로 갔을 때 다시 스크롤하면 첫 위치
     * 2. 자동 스크롤
     * 3. 스크롤 속도 조절
     * 4. 인디케이터
     *
     * res
     * RollingBanner - RollingViewPager, RollingViewPagerIndicator
     * RollingViewPager -  ‘자동 스크롤’, ‘스크롤 속도 조절’, ‘사용자 터치 막기’ 대응용으로 ViewPager를 상속받은 뷰
     * RollingVIewPagerIndicator – 각각 이미지 리소스, 여백 등을 받고 ViewPager를 설정하면 해당 ViewPager의 실제 아이템 갯수 만큼 이미지를 넣고, 스크롤 될 때 마다 selected 반복하는 역할을 할 커스텀 뷰
     * RollingViewPagerAdapter – 사실상의 무한대로 아이템을 관리할 커스텀 어댑터 클래스
     *
     * warning
     * 1.필요한 기능을 전부 구현하면서도, 실제 사용하는 코드는 매우 적게
     *
     */


}
package elmansyahfauzifinalproject.mymovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import elmansyahfauzifinalproject.mymovies.views.MovieDetail;
import elmansyahfauzifinalproject.mymovies.views.MovieReview;

public class Detail extends AppCompatActivity implements ActionBar.TabListener {

    public static final String MOVIE_KEY = "DATA";

    @BindView(R.id.vp_main)
    ViewPager viewPager;

    private static ActionBar actionBar;
    private static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_detail);
        ButterKnife.bind(this);
        initComponent();
    }

    private void initComponent() {
        viewPager = (ViewPager) findViewById(R.id.vp_main);
        fragmentManager = getSupportFragmentManager();
        viewPager.setAdapter(new MyAdapter(fragmentManager));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setTabs();
    }

    private class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = getIntent().getExtras();
            Fragment frag = null;
            if (bundle == null){
                bundle = null;
            }
            if (position == 0){
                frag = new MovieDetail();
            }else if(position == 1){
                frag = new MovieReview();
            }else if(position == 2){
                frag = new MovieDetail();
            }
            frag.setArguments(bundle);
            return frag;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    private void setTabs() {
        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        android.support.v7.app.ActionBar.Tab tabDetail = actionBar.newTab();
        android.support.v7.app.ActionBar.Tab tabReviews = actionBar.newTab();
        android.support.v7.app.ActionBar.Tab tabVideos = actionBar.newTab();
        tabDetail.setText("Detail");
        tabReviews.setText("Reviews");
        tabVideos.setText("Videos");
        tabDetail.setTabListener(this);
        tabReviews.setTabListener(this);
        tabVideos.setTabListener(this);
        actionBar.addTab(tabDetail);
        actionBar.addTab(tabReviews);
        actionBar.addTab(tabVideos);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }
}

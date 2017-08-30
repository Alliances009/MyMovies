package elmansyahfauzifinalproject.mymovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import elmansyahfauzifinalproject.mymovies.model.Movie;
import elmansyahfauzifinalproject.mymovies.views.MovieDetail;
import elmansyahfauzifinalproject.mymovies.views.MovieReview;
import elmansyahfauzifinalproject.mymovies.views.MovieVideo;

public class Detail extends AppCompatActivity implements ActionBar.TabListener {

    public static final String MOVIE_KEY = "DATA";

    @BindView(R.id.vp_main)
    ViewPager viewPager;

    private static ActionBar actionBar;
    private static FragmentManager fragmentManager;
    static RelativeLayout loadingPage;
    private MovieVideo fragVideo;
    private MovieReview fragReview;
    private MovieDetail fragDetail;

    public static void loadingShow(){
        loadingPage.setVisibility(View.VISIBLE);
    }

    public static void loadingHide(){
        loadingPage.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_detail);
        ButterKnife.bind(this);
        loadingPage = (RelativeLayout) findViewById(R.id.loadingPage);
        initComponent();
    }

    private void initComponent() {
        Bundle bundle = getIntent().getExtras();
        Fragment frag = null;
        if (bundle == null) {
            bundle = null;
        }
        fragVideo = new MovieVideo();
        fragReview = new MovieReview();
        fragDetail = new MovieDetail();
        fragReview.setArguments(bundle);
        fragVideo.setArguments(bundle);
        fragDetail.setArguments(bundle);
        fragDetail.isLoad = false;
        fragVideo.isLoad = false;
        fragReview.isLoad = false;
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
            Fragment frag = null;
            if (position == 0) {
                frag = fragDetail;
            } else if (position == 1) {
                frag = fragReview;
            } else if (position == 2) {
                frag = fragVideo;

            }
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
        ActionBar.Tab tabDetail = actionBar.newTab();
        ActionBar.Tab tabReviews = actionBar.newTab();
        ActionBar.Tab tabVideos = actionBar.newTab();
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
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }
}

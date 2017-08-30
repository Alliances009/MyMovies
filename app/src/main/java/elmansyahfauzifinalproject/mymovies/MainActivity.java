package elmansyahfauzifinalproject.mymovies;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import elmansyahfauzifinalproject.mymovies.adapter.MovieAdapter;
import elmansyahfauzifinalproject.mymovies.model.Movie;
import elmansyahfauzifinalproject.mymovies.model.Result;
import elmansyahfauzifinalproject.mymovies.retrofit.MovieAPI;
import elmansyahfauzifinalproject.mymovies.retrofit.ServiceGenerator;
import elmansyahfauzifinalproject.mymovies.utils.FavoriteMovie;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ItemClickListener {


    private static final String TAG = "MainActivity";
    @BindView(R.id.rv_movie)
    RecyclerView rvMovies;
    @BindView(R.id.loadingPage)
    RelativeLayout loadingPage;
    private MovieAdapter movieAdapter;
    private GridLayoutManager layoutManager;
    private int lastPage = 1;
    private String category = "popular";
    private Boolean isLoading = false, isLastPage = false, isFavorite = false;
    public int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Realm.init(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initComponent();
        getMovie(category, 1);
        rvMovies.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!isFavorite) {
                    GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                    int visibleItemCount = gridLayoutManager.getChildCount();
                    int totalItemCount = gridLayoutManager.getItemCount();
                    int pastVisiblesItems = gridLayoutManager.findFirstVisibleItemPosition();
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount && pastVisiblesItems >= 0) {
                        if (!isLastPage) {
                            isLoading = false;
                            lastPage += 1;
                            loadingPage.setVisibility(View.VISIBLE);
                            getMovie(category, lastPage);
                        }
                    }

                }
            }
        });

    }

    private void showMovieList(List<Result> movieList) {
        movieAdapter = new MovieAdapter(movieList, MainActivity.this, true);
        rvMovies.setAdapter(movieAdapter);
    }

    private void initComponent() {
        Integer span_count = getResources().getInteger(R.integer.span_count);
        layoutManager = new GridLayoutManager(MainActivity.this, span_count);
        rvMovies.setLayoutManager(layoutManager);
    }

    private Call<Movie> getListMovie(String category, int page) {
        return ServiceGenerator.createService(MovieAPI.class).getMovies(category, page);
    }

    private void getMovie(String category, int page) {
        loadingPage.setVisibility(View.VISIBLE);
        if ((!isLoading || page == 1) && !isFavorite) {
            getListMovie(category, page).enqueue(new Callback<Movie>() {
                @Override
                public void onResponse(Call<Movie> call, Response<Movie> response) {
                    isLoading = false;
                    Movie movie = response.body();
                    int currentPage = movie.getPage();
                    List<Result> results = movie.getResults();
                    if (currentPage == 1) {
                        movieAdapter = new MovieAdapter(results, MainActivity.this);
                        rvMovies.setAdapter(movieAdapter);
                        if (currentPage <= movie.getTotalPages()) {
                            isLoading = true;
                        } else {
                            isLastPage = true;
                        }
                    } else {
                        movieAdapter.append(results);
                        if (currentPage != movie.getTotalPages()) {
                            isLoading = true;
                        } else {
                            isLastPage = true;
                        }
                    }
                    lastPage = currentPage;
                    loadingPage.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<Movie> call, Throwable t) {
                    loadingPage.setVisibility(View.GONE);
                }
            });
        }
    }


    @Override
    public void onItemClick(int position) {
        showDetail(position);
    }

    private void showDetail(int position) {
        Result data = movieAdapter.getData(position);
        Log.d(TAG, "showDetail: " + position);
        Intent intenDetail = new Intent(MainActivity.this, Detail.class);
        intenDetail.putExtra("DATA", data);
        intenDetail.putExtra("IS_FAVORITE", false);
        startActivity(intenDetail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mn_popular:
                isFavorite = false;
                getMovie("popular", 1);
                break;
            case R.id.mn_top_rated:
                isFavorite = false;
                getMovie("top_rated", 1);
                break;
            case R.id.mn_favorite:
                isFavorite = true;
                loadFavorite();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadFavorite() {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    List<Result> data = new ArrayList<Result>();
                    RealmResults<FavoriteMovie> favoriteMovies = realm.where(FavoriteMovie.class).findAll();
                    for (FavoriteMovie favoriteMovie : favoriteMovies) {
                        data.add(favoriteMovie.setMovie(favoriteMovie));
                    }
                    showMovieList(data);
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }
}

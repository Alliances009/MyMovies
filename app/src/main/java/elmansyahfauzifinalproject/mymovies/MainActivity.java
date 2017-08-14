package elmansyahfauzifinalproject.mymovies;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import elmansyahfauzifinalproject.mymovies.retrofit.MovieAPI;
import elmansyahfauzifinalproject.mymovies.retrofit.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ItemClickListener {


    private static final String TAG = "MainActivity";
    private RecyclerView rvMovies;
    private MovieAdapter movieAdapter;
    private GridLayoutManager layoutManager;
    private List<Result> movieList;
    private int lastPage = 1;
    private String category = "popular";
    private Boolean isLoading = false,isLastPage = false;
    public int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponent();
        getMovie(category,1);
        rvMovies.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            int visibleItemCount = gridLayoutManager.getChildCount();
            int totalItemCount = gridLayoutManager.getItemCount();
            int pastVisiblesItems = gridLayoutManager.findFirstVisibleItemPosition();
            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount && pastVisiblesItems >= 0) {
                if (!isLastPage){
                    isLoading = false;
                    lastPage += 1;
                    getMovie(category,lastPage);
                }
            }
        }
    });

    }

    private void initComponent() {
        Integer span_count = getResources().getInteger(R.integer.span_count);
        rvMovies = (RecyclerView) findViewById(R.id.rv_movie);
        layoutManager = new GridLayoutManager(MainActivity.this,span_count);
        rvMovies.setLayoutManager(layoutManager);
    }

    private Call<Movie> getListMovie(String category,int page){
        return  ServiceGenerator.createService(MovieAPI.class).getMovies(category,page);
    }

    private void getMovie(String category,int page){
        if (!isLoading || page == 1) {
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
                }

                @Override
                public void onFailure(Call<Movie> call, Throwable t) {

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
        Intent intenDetail = new Intent(MainActivity.this,DetailActivity.class);
        intenDetail.putExtra("DATA",data);
        startActivity(intenDetail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mn_popular:
                getMovie("popular",1);
                break;
            case R.id.mn_top_rated:
                getMovie("top_rated",1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

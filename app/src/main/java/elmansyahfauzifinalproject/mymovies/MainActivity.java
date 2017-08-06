package elmansyahfauzifinalproject.mymovies;


import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import elmansyahfauzifinalproject.mymovies.retrofit.MovieAPI;
import elmansyahfauzifinalproject.mymovies.retrofit.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements Callback<Movie> {


    private static final String TAG = "MainActivity";
    private RecyclerView rvMovies;
    private MovieAdapter movieAdapter;
    private GridLayoutManager layoutManager;
    private List<Result> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponent();
        getPopularMovies();

    }

    private void initComponent() {
        rvMovies = (RecyclerView) findViewById(R.id.rv_movie);
        layoutManager = new GridLayoutManager(MainActivity.this,2);
        rvMovies.setLayoutManager(layoutManager);

    }

    private void getPopularMovies() {
        final Call<Movie> call = ServiceGenerator.createService(MovieAPI.class).getPopularMovies(1);
        call.enqueue(this);
        
    }

    private void showMovies(List<Result> movieList) {
        for(Result result:movieList){
            Log.v(TAG, result.getTitle());
        }
    }


    @Override
    public void onResponse(Call<Movie> call, Response<Movie> response) {
        Movie movie = response.body();
        if (movie != null){
            movieList = movie.getResults();
            Log.d(TAG, "onResponse: "+movie.getTotalResults());
            Log.d(TAG, "onResponse: GRID = "+layoutManager.getItemCount());
            movieAdapter = new MovieAdapter(movieList);
            rvMovies.setAdapter(movieAdapter);
            Log.d(TAG, "onResponse: GRID2 = "+layoutManager.getItemCount());
            if (movie.getPage() == 1 || true){

            }else{

            }
        }else{
            Log.d(TAG, "onResponse: Movie null");
        }


        rvMovies.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();

            }
        });
    }

    @Override
    public void onFailure(Call<Movie> call, Throwable t) {

    }
}

package elmansyahfauzifinalproject.mymovies.views;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import elmansyahfauzifinalproject.mymovies.Detail;
import elmansyahfauzifinalproject.mymovies.R;
import elmansyahfauzifinalproject.mymovies.model.Genre;
import elmansyahfauzifinalproject.mymovies.model.Result;
import elmansyahfauzifinalproject.mymovies.model.SingleMovie;
import elmansyahfauzifinalproject.mymovies.retrofit.MovieAPI;
import elmansyahfauzifinalproject.mymovies.retrofit.ServiceGenerator;
import elmansyahfauzifinalproject.mymovies.utils.FavoriteMovie;
import elmansyahfauzifinalproject.mymovies.utils.UrlComposer;
import io.realm.Realm;
import io.realm.RealmQuery;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetail extends DialogFragment {
    private static View view;

    Unbinder unbinder;

    public static final String MOVIE_KEY = "DATA";
    public static final String FAVORITE_KEY = "IS_FAVORITE";
    private static final String TAG = "DetailActivity";
    @BindView(R.id.iv_poster)
    ImageView ivPoster;
    @BindView(R.id.rb_rating)
    RatingBar rbRating;
    @BindView(R.id.tv_rating)
    TextView tvRating;
    @BindView(R.id.tb_add_to_favorite)
    ToggleButton tbAddToFavorite;
    @BindView(R.id.tv_title_oiginal)
    TextView tvTitleOiginal;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_release_date)
    TextView tvReleaseDate;
    @BindView(R.id.tv_runtime)
    TextView tvRuntime;
    @BindView(R.id.tv_genres)
    TextView tvGenres;
    @BindView(R.id.tv_overview)
    TextView tvOverview;
    private boolean isFavorite = false;
    private Call<SingleMovie> dataMovie;
    public boolean isLoad = false;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_detail, container, false);
        Realm.init(getActivity().getApplicationContext());
        unbinder = ButterKnife.bind(this, view);
        initComponent();
        return view;
    }

    private void initComponent() {


        getData();


    }

    private void onListeners(final SingleMovie movie) {
        final SingleMovie data = movie;
        tbAddToFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setFavorite(data);
                    tbAddToFavorite.setBackgroundDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.ic_star));
                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                    dialog
                            .setMessage("Remove movie from favorite list ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Realm realm = null;
                                    try {
                                        realm = Realm.getDefaultInstance();
                                        realm.executeTransaction(new Realm.Transaction() {
                                            @Override
                                            public void execute(Realm realm) {
                                                realm.where(FavoriteMovie.class).equalTo("id", data.getId()).findAll().deleteAllFromRealm();
                                                tbAddToFavorite.setBackgroundDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.ic_star_border));
                                                Toast.makeText(getActivity().getApplicationContext(), "Success remove favorite movie", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } finally {
                                        if (realm != null) {
                                            realm.close();
                                        }
                                    }
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = dialog.create();
                    alert.show();
                }
            }
        });
    }

    private void getData() {
        if (!isLoad){
            Bundle bundle = getArguments();
            Result data = null;
            boolean favorite = false;
            if (bundle != null) {
                data = (Result) bundle.get(MOVIE_KEY);
                favorite = getFavorite(bundle,data);
            }

            getMovieById(data.getId(),favorite);
        }
    }

    private boolean getFavorite(Bundle bundle, final Result data) {
        if (bundle.getBoolean(FAVORITE_KEY)) {
            tbAddToFavorite.setChecked(true);
        } else {
            Realm realm = null;
            Log.d(TAG, "getFavorite: "+data.getId());
            try {
                realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Log.d(TAG, "getFavorite 11 : "+realm.where(FavoriteMovie.class).equalTo("id", data.getId()).count());
                        isFavorite = realm.where(FavoriteMovie.class).equalTo("id", data.getId()).count() > 0;

                    }
                });
            } finally {
                if (realm != null) {
                    realm.close();
                }
            }

        }
        return isFavorite;
    }

    private void getMovieById(final Integer id, final boolean favorite){
        Detail.loadingShow();
        if (favorite){
            Realm realm = null;
            try {
                realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void execute(Realm realm) {
                        Log.d(TAG, "getFavorite 11 : "+realm.where(FavoriteMovie.class).equalTo("id", id));
                        FavoriteMovie data = realm.where(FavoriteMovie.class).equalTo("id", id).findFirst();
                        final SingleMovie movie = data.setSingleMovie(data);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            setUpDetail(movie,isFavorite);
                            onListeners(movie);
                        }

                    }
                });
            } finally {
                if (realm != null) {
                    realm.close();
                }
                Detail.loadingHide();
            }
        }else{
            dataMovie = ServiceGenerator.createService(MovieAPI.class).getMovieById(id);
            dataMovie.enqueue(new Callback<SingleMovie>() {
                @Override
                public void onResponse(Call<SingleMovie> call, Response<SingleMovie> response) {
                    SingleMovie movie = response.body();
                    if (movie != null){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            setUpDetail(movie,isFavorite);
                            onListeners(movie);
                        }
                    }
                }

                @Override
                public void onFailure(Call<SingleMovie> call, Throwable t) {
                    Detail.loadingHide();
                }
            });
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setUpDetail(final SingleMovie movie,final boolean isFavorite) {
        tvTitleOiginal.setText(movie.getOriginalTitle());
        tvRating.setText("" + movie.getVoteAverage() + "/10.0");
        tvReleaseDate.setText(movie.getReleaseDate());
        tvOverview.setText(movie.getOverview());
        Picasso.with(getActivity().getApplicationContext()).load(UrlComposer.getPosterUrl(movie.getPosterPath())).into(ivPoster);
        rbRating.setRating((Float.parseFloat(""+movie.getVoteAverage())/2));
        tvRating.setText("("+movie.getVoteAverage()+")");
        tvReleaseDate.setText(movie.getReleaseDate());
        tvRuntime.setText(""+movie.getRuntime()+" min");
        tvOverview.setText(movie.getOverview());
        List<Genre> genres = movie.getGenres();
        String genre = "";
        if (genres != null){
            for (Genre genr:genres){
                genre += genr.getName()+",";
            }
            genre = genre.substring(0,genre.length()-1);
        }
        Log.d(TAG, "setUpDetail: "+isFavorite);
        tvGenres.setText(genre);
        if (Objects.equals(""+movie.getTitle().toUpperCase(), ""+movie.getOriginalTitle().toUpperCase())){
            tvTitle.setVisibility(View.GONE);
        }else{
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText("("+movie.getTitle()+")");
        }

        tbAddToFavorite.setBackgroundDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.ic_star_border));
        tbAddToFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tbAddToFavorite.setBackgroundDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.ic_star));
                } else {
                    tbAddToFavorite.setBackgroundDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.ic_star_border));
                }
            }
        });
        if (isFavorite) {
            tbAddToFavorite.setChecked(true);
        } else {
            tbAddToFavorite.setChecked(false);
        }
        Detail.loadingHide();
    }


    protected void setFavorite(final SingleMovie movie) {

        Realm realm = null;

        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.insert(FavoriteMovie.getMovie(movie));
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

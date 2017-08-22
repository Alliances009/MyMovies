package elmansyahfauzifinalproject.mymovies.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.youtube.player.YouTubePlayer;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import elmansyahfauzifinalproject.mymovies.R;
import elmansyahfauzifinalproject.mymovies.model.Result;
import elmansyahfauzifinalproject.mymovies.utils.UrlComposer;
import elmansyahfauzifinalproject.mymovies.utils.FavoriteMovie;
import elmansyahfauzifinalproject.mymovies.utils.Util;
import io.realm.Realm;

public class MovieDetail extends DialogFragment {
    private static View view;
    @BindView(R.id.iv_poster)
    ImageView ivPoster;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tb_add_to_favorite)
    ToggleButton tbAddToFavorite;
    @BindView(R.id.tv_rating)
    TextView tvRating;
    @BindView(R.id.tv_date_released)
    TextView tvDateReleased;
    @BindView(R.id.tv_duration)
    TextView tvDuration;
    @BindView(R.id.tv_overview)
    TextView tvOverview;
    Unbinder unbinder;

    public static final String MOVIE_KEY = "DATA";
    public static final String FAVORITE_KEY = "IS_FAVORITE";
    private static final String TAG = "DetailActivity";
    private boolean isFavorite = false;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_detail, container, false);
        Realm.init(getActivity().getApplicationContext());
        unbinder = ButterKnife.bind(this, view);
        initComponent();
        return view;
    }

    private void initComponent() {
        final Result data = getData();
        tvTitle.setText(data.getOriginalTitle());
        tvRating.setText("" + data.getVoteAverage() + "/10.0");
        tvDateReleased.setText(data.getReleaseDate());
        tvOverview.setText(data.getOverview());
        Picasso.with(getActivity().getApplicationContext()).load(UrlComposer.getPosterUrl(data.getPosterPath())).into(ivPoster);
        tbAddToFavorite.setBackgroundDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(),R.drawable.ic_star_border));
        tbAddToFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    tbAddToFavorite.setBackgroundDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(),R.drawable.ic_star));
                }else{
                    tbAddToFavorite.setBackgroundDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(),R.drawable.ic_star_border));
                }
            }
        });
        Bundle bundle = getArguments();
        if (bundle != null){
            if (bundle.getBoolean(FAVORITE_KEY)){
                tbAddToFavorite.setChecked(true);
            }else{
                Realm realm = null;
                try{
                    realm = Realm.getDefaultInstance();
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            isFavorite = realm.where(FavoriteMovie.class).equalTo("id",data.getId()).count() > 0;

                        }
                    });
                }finally {
                    if (realm != null){
                        realm.close();
                    }
                }
                if(isFavorite){
                    tbAddToFavorite.setChecked(true);
                }else{
                    tbAddToFavorite.setChecked(false);
                }
            }
        }
        onListeners();
    }

    private void onListeners() {
        final Result data = getData();
        tbAddToFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    setFavorite();
                    tbAddToFavorite.setBackgroundDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(),R.drawable.ic_star));
                }else{
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                    dialog
                            .setMessage("Remove movie from favorite list ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Realm realm = null;
                                    try{
                                        realm = Realm.getDefaultInstance();
                                        realm.executeTransaction(new Realm.Transaction() {
                                            @Override
                                            public void execute(Realm realm) {
                                                realm.where(FavoriteMovie.class).equalTo("id", data.getId()).findAll().deleteAllFromRealm();
                                                tbAddToFavorite.setBackgroundDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(),R.drawable.ic_star_border));
                                                Toast.makeText(getActivity().getApplicationContext(),"Success remove favorite movie",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }finally {
                                        if (realm != null){
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

    private Result getData() {
        Bundle bundle = getArguments();
        Result data = null;
        if (bundle != null) {
            data = (Result) bundle.get(MOVIE_KEY);
        }
        return data;
    }


    protected void setFavorite() {
        final Result result = getData();

        Realm realm = null;

        try{
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.insert(FavoriteMovie.getMovie(result));
                }
            });
        }finally {
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

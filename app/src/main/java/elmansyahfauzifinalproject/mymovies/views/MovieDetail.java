package elmansyahfauzifinalproject.mymovies.views;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import elmansyahfauzifinalproject.mymovies.R;
import elmansyahfauzifinalproject.mymovies.Result;
import elmansyahfauzifinalproject.mymovies.utils.FavoriteMovie;
import io.realm.Realm;

public class MovieDetail extends android.app.Fragment {
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
        initComponent();
        Realm.init(getActivity().getApplicationContext());
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    private void initComponent() {
        Result data = getData();
        Log.d(TAG, "initComponent: "+data);
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

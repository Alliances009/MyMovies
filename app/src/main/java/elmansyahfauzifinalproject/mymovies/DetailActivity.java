package elmansyahfauzifinalproject.mymovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "DetailActivity";
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_rating)
    TextView tvRating;
    @BindView(R.id.tv_date_released)
    TextView tvDateReleased;
    @BindView(R.id.tv_duration)
    TextView tvDuration;
    @BindView(R.id.iv_poster)
    ImageView ivPoster;
    @BindView(R.id.tv_overview)
    TextView tvOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Result data = (Result) bundle.get("DATA");
            tvTitle.setText(data.getOriginalTitle());
            tvRating.setText("" + data.getVoteAverage()+"/10.0");
            tvDateReleased.setText(data.getReleaseDate());
            tvOverview.setText(data.getOverview());
            Picasso.with(DetailActivity.this).load(UrlComposer.getPosterUrl(data.getPosterPath())).into(ivPoster);
        }
    }
}

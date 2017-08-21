package elmansyahfauzifinalproject.mymovies.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import elmansyahfauzifinalproject.mymovies.Detail;
import elmansyahfauzifinalproject.mymovies.R;
import elmansyahfauzifinalproject.mymovies.model.Result;
import elmansyahfauzifinalproject.mymovies.model.Review;
import elmansyahfauzifinalproject.mymovies.model.ReviewResult;
import elmansyahfauzifinalproject.mymovies.retrofit.MovieAPI;
import elmansyahfauzifinalproject.mymovies.retrofit.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieReview extends Fragment {

    private static final String TAG = "MovieReview";
    @BindView(R.id.ll_review)
    LinearLayout llReview;
    Unbinder unbinder;
    private View view = null;
    private Call<ReviewResult> reviewResult;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_movie_review, container, false);
        initComponnent();
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    private void initComponnent() {
        Result data = getData();
        if (data != null){
            getReviewList(data.getId());
        }
    }

    private Result getData() {
        Bundle bundle = getArguments();
        Result data = null;
        if (bundle != null) {
            data = (Result) bundle.get(Detail.MOVIE_KEY);
        }
        return data;
    }

    private void getReviewList(Integer movieId) {
        reviewResult = ServiceGenerator
                .createService(MovieAPI.class)
                .getReviews(movieId);

        reviewResult.enqueue(new Callback<ReviewResult>() {
            @Override
            public void onResponse(Call<ReviewResult> call, Response<ReviewResult> response) {
                ReviewResult res = response.body();
                if (res != null) {
                    List<Review> data = res.getResults();
                    Log.d(TAG, "onResponse: 1 "+data);
                    for (Review review : data) {
                        Log.d(TAG, "onResponse: 2 "+review.getAuthor());
                        View reviewRow = GenerateView.getReview(
                                getActivity().getApplicationContext(),
                                review.getAuthor(),
                                review.getContent()
                        );
                        llReview.addView(reviewRow);
                    }
                }
            }

            @Override
            public void onFailure(Call<ReviewResult> call, Throwable t) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

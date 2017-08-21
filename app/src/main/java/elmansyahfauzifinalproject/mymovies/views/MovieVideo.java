package elmansyahfauzifinalproject.mymovies.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import elmansyahfauzifinalproject.mymovies.model.Video;
import elmansyahfauzifinalproject.mymovies.model.VideoResult;
import elmansyahfauzifinalproject.mymovies.retrofit.MovieAPI;
import elmansyahfauzifinalproject.mymovies.retrofit.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieVideo extends Fragment {

    @BindView(R.id.ll_video)
    LinearLayout llVideo;
    Unbinder unbinder;
    private View view = null;
    private Call<VideoResult> videoResult;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_movie_video, container, false);
        initComponent();
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    private void initComponent() {
        Result data = getData();
        if  (data != null){
            getVideoList(data.getId());
        }
    }

    private void getVideoList(Integer id) {
        videoResult = ServiceGenerator
                .createService(MovieAPI.class)
                .getVideos(id);
        videoResult.enqueue(new Callback<VideoResult>() {
            @Override
            public void onResponse(Call<VideoResult> call, Response<VideoResult> response) {
                VideoResult res = response.body();
                if (res != null){
                    List<Video> data = res.getResults();
                    for (Video video : data){
                        View viewRow = GenerateView
                                .getVideo(
                                        getActivity().getApplicationContext(),
                                        video.getName(),
                                        video.getType()
                                        );
                            llVideo.addView(viewRow);
                    }
                }
            }

            @Override
            public void onFailure(Call<VideoResult> call, Throwable t) {

            }
        });
    }

    private Result getData()
    {
        Bundle bundle = getArguments();
        Result  data = null;
        if(bundle != null){
            data = (Result) bundle.get(Detail.MOVIE_KEY);
        }
        return data;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

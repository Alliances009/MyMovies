package elmansyahfauzifinalproject.mymovies.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.youtube.player.YouTubePlayer;

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
import elmansyahfauzifinalproject.mymovies.utils.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieVideo extends Fragment {

    @BindView(R.id.ll_video)
    LinearLayout llVideo;
    Unbinder unbinder;
    private View view = null;
    private Call<VideoResult> videoResult;
    public boolean isLoad = false;

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
        if (!isLoad){
            Detail.loadingShow();
        videoResult = ServiceGenerator
                .createService(MovieAPI.class)
                .getVideos(id);
        videoResult.enqueue(new Callback<VideoResult>() {
            @Override
            public void onResponse(Call<VideoResult> call, Response<VideoResult> response) {
                VideoResult res = response.body();
                if (res != null){
                    List<Video> data = res.getResults();
                    for (final Video video : data){
                        View viewRow = GenerateView
                                .getVideo(
                                        getActivity().getApplicationContext(),
                                        video.getName(),
                                        video.getType()
                                        );
                            viewRow.findViewById(R.id.video_content).setOnClickListener(new View.OnClickListener() {
                                                           @Override
                                                           public void onClick(View v) {
                                                               playYoutube(video.getKey());
                                                           }
                                                       }
                            );
                            llVideo.addView(viewRow);
                            isLoad = true;
                            Detail.loadingHide();
                    }
                }
            }

            @Override
            public void onFailure(Call<VideoResult> call, Throwable t) {
                Detail.loadingHide();
            }
        });
        }
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

    private void playYoutube(String videoId) {
        if (Util.isPackageInstalled("com.google.android.youtube",getActivity().getPackageManager())){
            //kalau youtube terinstall
            Intent openYoutubePlayerActivity = new Intent(getActivity().getApplicationContext(), YoutubePlayer.class);
            openYoutubePlayerActivity.putExtra(YoutubePlayer.KEY_VIDEO_ID, videoId);
            startActivity(openYoutubePlayerActivity);
        } else {
            //kalau youtube tidak terinstall
            String youtubeUrl = "https://www.youtube.com/watch?v=" + videoId;
            Util.openUrl(youtubeUrl,getActivity().getApplicationContext());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

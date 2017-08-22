package elmansyahfauzifinalproject.mymovies.views;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import elmansyahfauzifinalproject.mymovies.R;
import elmansyahfauzifinalproject.mymovies.utils.Util;

/**
 * Created by ALLIANCES on 8/22/2017.
 */

public class YoutubePlayer extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    @BindView(R.id.youtube_player)
    YouTubePlayerView youtubePlayer;

    public static final String KEY_VIDEO_ID = "videoId";
    public static final String YOUTUBE_API_KEY = "AIzaSyAYiS4uj_KsrDBQHhxep8yZbVSUvZss1q4";
    private static final int RECOVERY_REQUEST  = 1;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_youtube);
        ButterKnife.bind(this);
        initComponent();
    }

    private void initComponent() {
        youtubePlayer.initialize(YOUTUBE_API_KEY,this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b){
            youTubePlayer.cueVideo(getIntent().getStringExtra(KEY_VIDEO_ID));
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()){
            youTubeInitializationResult.getErrorDialog(this,RECOVERY_REQUEST).show();
        }else{
            String youtubeUrl = "https://www.youtube.com/watch?v=" + getIntent().getStringExtra(KEY_VIDEO_ID);
            Util.openUrl(youtubeUrl, this);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RECOVERY_REQUEST){
            youtubePlayer.initialize("",this);
        }
    }
}

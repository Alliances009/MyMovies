package elmansyahfauzifinalproject.mymovies.retrofit;

import elmansyahfauzifinalproject.mymovies.model.Movie;
import elmansyahfauzifinalproject.mymovies.model.Review;
import elmansyahfauzifinalproject.mymovies.model.ReviewResult;
import elmansyahfauzifinalproject.mymovies.model.SingleMovie;
import elmansyahfauzifinalproject.mymovies.model.VideoResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ALLIANCES on 7/31/2017.
 */

public interface MovieAPI {

    @GET("movie/{category}")
    Call<Movie> getMovies(@Path("category") String category, @Query("page") int page);

    @GET("movie/{movie_id}")
    Call<SingleMovie> getMovieById(@Path("movie_id") Integer movieId);

    @GET("movie/{movie_id}/reviews?language=en-US&page=1")
    Call<ReviewResult> getReviews(@Path("movie_id") Integer movieId);

    @GET("movie/{movie_id}/videos")
    Call<VideoResult> getVideos(@Path("movie_id") Integer movieId);

}

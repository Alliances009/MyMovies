package elmansyahfauzifinalproject.mymovies.retrofit;

import elmansyahfauzifinalproject.mymovies.Movie;
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
}

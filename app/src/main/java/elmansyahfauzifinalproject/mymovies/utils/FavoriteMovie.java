package elmansyahfauzifinalproject.mymovies.utils;

import elmansyahfauzifinalproject.mymovies.Movie;
import elmansyahfauzifinalproject.mymovies.Result;
import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by ALLIANCES on 8/14/2017.
 */

public class FavoriteMovie extends RealmObject {
    public int vote_count;
    public int id;
    public boolean video;
    public double vote_average;
    public String title;
    public double popularity;
    public String poster_path;
    public String original_language;
    public String original_title;
    public RealmList<IntegerObject> genre_ids;
    public String backdrop_path;
    public boolean adult;
    public String overview;
    public String release_date;


    public static FavoriteMovie getMovie(Result result) {
        FavoriteMovie favoriteMovie = new FavoriteMovie();
        favoriteMovie.id = result.getId();

        return favoriteMovie;
    }
}

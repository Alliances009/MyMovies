package elmansyahfauzifinalproject.mymovies.utils;

import java.util.ArrayList;
import java.util.List;

import elmansyahfauzifinalproject.mymovies.model.Result;
import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by ALLIANCES on 8/14/2017.
 */

public class FavoriteMovie extends RealmObject {
    public int voteCount;
    public int id;
    public boolean video;
    public double voteAverage;
    public String title;
    public double popularity;
    public String posterPath;
    public String originalLanguage;
    public String originalTitle;
    public RealmList<IntegerObject> genreIds;
    public String backdropPath;
    public boolean adult;
    public String overview;
    public String releaseDate;


    public static FavoriteMovie getMovie(Result result) {
        FavoriteMovie favoriteMovie = new FavoriteMovie();
        favoriteMovie.voteCount = result.getVoteCount();
        favoriteMovie.id = result.getId();
        favoriteMovie.video = result.getVideo();
        favoriteMovie.voteAverage = result.getVoteAverage();
        favoriteMovie.title = result.getTitle();
        favoriteMovie.popularity = result.getPopularity();
        favoriteMovie.posterPath = result.getPosterPath();
        favoriteMovie.originalLanguage = result.getOriginalLanguage();
        favoriteMovie.originalTitle = result.getOriginalTitle();
        favoriteMovie.backdropPath = result.getBackdropPath();
        favoriteMovie.adult = result.getAdult();
        favoriteMovie.overview = result.getOverview();
        favoriteMovie.releaseDate = result.getReleaseDate();

        favoriteMovie.genreIds = new RealmList<>();
        List<Integer> genreIds = result.getGenreIds();
        for (Integer genreId : genreIds) {
            IntegerObject integerObject = new IntegerObject();
            integerObject.integer = genreId;
            favoriteMovie.genreIds.add(integerObject);
        }
        return favoriteMovie;
    }

    public static Result setMovie(FavoriteMovie favoriteMovie) {
        Result result = new Result();
        result.setVoteCount(favoriteMovie.voteCount);
        result.setId(favoriteMovie.id);
        result.setVideo(favoriteMovie.video);
        result.setVoteAverage(favoriteMovie.voteAverage);
        result.setTitle(favoriteMovie.title);
        result.setPopularity(favoriteMovie.popularity);
        result.setPosterPath(favoriteMovie.posterPath);
        result.setOriginalLanguage(favoriteMovie.originalLanguage);
        result.setOriginalTitle(favoriteMovie.originalTitle);
        result.setBackdropPath(favoriteMovie.backdropPath);
        result.setAdult(favoriteMovie.adult);
        result.setOverview(favoriteMovie.overview);
        result.setReleaseDate(favoriteMovie.releaseDate);

        List<Integer> genreList = new ArrayList<>();
        RealmList<IntegerObject> integerObjectRealmList = favoriteMovie.genreIds;
        for (IntegerObject integerObject : integerObjectRealmList) {
            genreList.add(integerObject.integer);
        }
        result.setGenreIds(genreList);
        return result;
    }
}

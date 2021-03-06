package elmansyahfauzifinalproject.mymovies.utils;

import java.util.ArrayList;
import java.util.List;

import elmansyahfauzifinalproject.mymovies.model.Genre;
import elmansyahfauzifinalproject.mymovies.model.Result;
import elmansyahfauzifinalproject.mymovies.model.SingleMovie;
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
    public RealmList<GenreObject> genres;
    public String backdropPath;
    public boolean adult;
    public String overview;
    public String releaseDate;
    public Integer runtime;


    public static FavoriteMovie getMovie(SingleMovie result) {
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
        favoriteMovie.runtime= result.getRuntime();
        favoriteMovie.releaseDate = result.getReleaseDate();
        favoriteMovie.genres = new RealmList<>();
        List<Genre> genreIds = result.getGenres();
        for (Genre genre : genreIds) {
            GenreObject genreObject = new GenreObject();
            genreObject.setId(genre.getId());
            genreObject.setName(genre.getName());
            favoriteMovie.genres.add(genreObject);
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
        return result;
    }

    public static SingleMovie setSingleMovie(FavoriteMovie favoriteMovie) {
        SingleMovie result = new SingleMovie();
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
        result.setRuntime(favoriteMovie.runtime);
        result.setReleaseDate(favoriteMovie.releaseDate);
        List<Genre> genreList = new ArrayList<>();
        RealmList<GenreObject> genreObjectRealmList = favoriteMovie.genres;
        for (GenreObject genreObject : genreObjectRealmList) {
            Genre genre = new Genre();
            genre.setName(genreObject.getName());
            genre.setId(genreObject.getId());
            genreList.add(genre);
        }
        result.setGenres(genreList);
        return result;
    }
}

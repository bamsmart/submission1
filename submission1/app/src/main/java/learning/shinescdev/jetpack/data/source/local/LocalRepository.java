package learning.shinescdev.jetpack.data.source.local;

import android.graphics.Movie;

import androidx.lifecycle.LiveData;

import java.util.List;

import learning.shinescdev.jetpack.data.source.local.entity.MovieEntity;
import learning.shinescdev.jetpack.data.source.local.entity.TVEntity;
import learning.shinescdev.jetpack.data.source.local.room.MovieDao;
import learning.shinescdev.jetpack.data.source.local.room.TVDao;

public class LocalRepository {

    public static LocalRepository INSTANCE;
    private  MovieDao mMovieDao;
    private TVDao mTVDao;

    private LocalRepository(MovieDao movieDao, TVDao tvDao){
        this.mMovieDao = movieDao;
        this.mTVDao = tvDao;
    }


    public  static LocalRepository getInstance(MovieDao movieDao, TVDao tvDao){
        if (INSTANCE == null){
            INSTANCE = new LocalRepository(movieDao, tvDao);
        }
        return INSTANCE;
    }

    public LiveData<List<MovieEntity>> getAllMovies(){
        return mMovieDao.getMovies();
    }

    public LiveData<List<MovieEntity>> getMovieById(int id){
        return mMovieDao.getMovieById(id);
    }

    public void setFavoriteMovie(MovieEntity movie, int newState){
        movie.setFavorite(newState);
        mMovieDao.updateMovie(movie);
    }

    public void insertMovie(List<MovieEntity> movie) {
        mMovieDao.insertMovie(movie);
    }


    // TV Show
    public LiveData<List<TVEntity>> getAllTVShow(){
        return mTVDao.getTV();
    }

    public void setFavoriteTVShow(TVEntity tv, int newState){
        tv.setFavorite(newState);
        mTVDao.updateTV(tv);
    }

    public void insertTVShow(List<TVEntity> tv){
        mTVDao.insertTV(tv);
    }


}

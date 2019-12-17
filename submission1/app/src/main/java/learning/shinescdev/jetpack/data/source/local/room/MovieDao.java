package learning.shinescdev.jetpack.data.source.local.room;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import learning.shinescdev.jetpack.data.source.local.entity.MovieEntity;
import learning.shinescdev.jetpack.data.source.local.entity.TVEntity;

@Dao
public interface MovieDao {

    @WorkerThread
    @Query("SELECT * FROM movie")
    LiveData<List<MovieEntity>> getMovies();

    @Query("SELECT * FROM movie WHERE id = :movieId")
    LiveData<List<MovieEntity>> getMovieById(int movieId);

    @Query("SELECT * FROM movie where favorite = 1")
    DataSource.Factory<Integer,MovieEntity> getFavoriteMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertMovie(List<MovieEntity> movie);

    @Update(onConflict = OnConflictStrategy.FAIL)
    int updateMovie(MovieEntity movie);

}

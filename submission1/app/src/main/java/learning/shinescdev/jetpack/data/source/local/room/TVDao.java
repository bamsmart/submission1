package learning.shinescdev.jetpack.data.source.local.room;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import java.util.concurrent.atomic.DoubleAccumulator;

import learning.shinescdev.jetpack.data.source.local.entity.TVEntity;

@Dao
public interface TVDao {
    @Query("SELECT * FROM tv")
    LiveData<List<TVEntity>> getTV();

    @Query("SELECT * FROM tv WHERE id = :tvShowId")
    LiveData<List<TVEntity>> getTVShowById(int tvShowId);

    @Query("SELECT * FROM tv WHERE id != :tvShowId")
    LiveData<List<TVEntity>> getTVShowRecomm(int tvShowId);


    @Query("SELECT * FROM tv where favorite = 1")
    DataSource.Factory<Integer,TVEntity> getFavoriteTV();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertTV(List<TVEntity> tv);

    @Update(onConflict = OnConflictStrategy.FAIL)
    int updateTV(TVEntity tv);
}

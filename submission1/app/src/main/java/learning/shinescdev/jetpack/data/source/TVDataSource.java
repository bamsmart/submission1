package learning.shinescdev.jetpack.data.source;

import androidx.lifecycle.LiveData;

import java.util.List;

import learning.shinescdev.jetpack.data.source.local.entity.MovieEntity;
import learning.shinescdev.jetpack.data.source.local.entity.TVEntity;
import learning.shinescdev.jetpack.vo.Resource;

public interface TVDataSource{
    LiveData<Resource<List<TVEntity>>> getAllTVShow();

    LiveData<Resource<List<TVEntity>>> getTVShowById(int tvShowId);

    LiveData<Resource<List<TVEntity>>> getTVShowRecomm(int tvShowId);
}

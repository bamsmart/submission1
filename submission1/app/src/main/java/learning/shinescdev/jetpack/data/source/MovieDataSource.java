package learning.shinescdev.jetpack.data.source;

import androidx.lifecycle.LiveData;


import java.util.List;

import learning.shinescdev.jetpack.data.source.local.entity.MovieEntity;
import learning.shinescdev.jetpack.vo.Resource;

public interface MovieDataSource {
    LiveData<Resource<List<MovieEntity>>> getAllMovies();

    LiveData<Resource<List<MovieEntity>>> getMovieById(int id);

    LiveData<Resource<List<MovieEntity>>> getMovieRecomm(int movieId);
}

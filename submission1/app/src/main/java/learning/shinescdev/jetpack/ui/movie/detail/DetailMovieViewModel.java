package learning.shinescdev.jetpack.ui.movie.detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.List;

import learning.shinescdev.jetpack.data.source.MovieRepository;
import learning.shinescdev.jetpack.data.source.local.entity.MovieEntity;
import learning.shinescdev.jetpack.vo.Resource;

public class DetailMovieViewModel extends ViewModel {

    private MovieRepository movieRepository;
    private int id;

    public DetailMovieViewModel(MovieRepository repository){
        this.movieRepository = repository;
    }

    private MutableLiveData<String> movieId = new MutableLiveData<>();

    public LiveData<Resource<List<MovieEntity>>> movie = Transformations.switchMap(movieId,
            data -> movieRepository.getMovieById(id));

    public LiveData<Resource<List<MovieEntity>>> getMovieRecomm = Transformations.switchMap(movieId,
            data -> movieRepository.getMovieRecomm(id)
    );

    void setMovieId (int id){
        this.id = id;
        movieId.setValue(String.valueOf(id));
    }
}
package learning.shinescdev.jetpack.ui.tv.detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.List;

import learning.shinescdev.jetpack.data.source.MovieRepository;
import learning.shinescdev.jetpack.data.source.TVRepository;
import learning.shinescdev.jetpack.data.source.local.entity.MovieEntity;
import learning.shinescdev.jetpack.data.source.local.entity.TVEntity;
import learning.shinescdev.jetpack.vo.Resource;

public class DetailTVViewModel extends ViewModel {

    private TVRepository tvShowRepository;
    private int id;

    public DetailTVViewModel(TVRepository repository){
        this.tvShowRepository = repository;
    }

    private MutableLiveData<String> tvShowId = new MutableLiveData<>();

    public LiveData<Resource<List<TVEntity>>> getTVShowById = Transformations.switchMap(tvShowId,
            data -> tvShowRepository.getTVShowById(id));

    public LiveData<Resource<List<TVEntity>>> getTVShowRecomm = Transformations.switchMap(tvShowId,
            data -> tvShowRepository.getTVShowRecomm(id)
    );

    void setTvShowId(int id){
        this.id = id;
        tvShowId.setValue(String.valueOf(id));
    }
}
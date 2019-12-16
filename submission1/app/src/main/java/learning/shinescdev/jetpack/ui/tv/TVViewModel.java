package learning.shinescdev.jetpack.ui.tv;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.List;

import learning.shinescdev.jetpack.data.source.TVRepository;
import learning.shinescdev.jetpack.data.source.local.entity.TVEntity;
import learning.shinescdev.jetpack.vo.Resource;

public class TVViewModel extends ViewModel {

    private TVRepository tvRepository;

    private MutableLiveData<String> mLogin = new MutableLiveData<>();

    LiveData<Resource<List<TVEntity>>> tvshow = Transformations.switchMap(mLogin,
            data -> tvRepository.getAllTVShow());

    public TVViewModel(TVRepository mTVRepository) {
        this.tvRepository = mTVRepository;
    }

    void setUsername(String username) {
        mLogin.setValue(username);
    }
}
package learning.shinescdev.jetpack.data.source;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

import learning.shinescdev.jetpack.data.source.local.LocalRepository;
import learning.shinescdev.jetpack.data.source.local.entity.MovieEntity;
import learning.shinescdev.jetpack.data.source.local.entity.TVEntity;
import learning.shinescdev.jetpack.data.source.remote.APIResponse;
import learning.shinescdev.jetpack.data.source.remote.RemoteRepository;
import learning.shinescdev.jetpack.data.source.remote.response.MovieResponse;
import learning.shinescdev.jetpack.data.source.remote.response.TVResponse;
import learning.shinescdev.jetpack.utils.AppExecutors;
import learning.shinescdev.jetpack.vo.Resource;

public class TVRepository implements TVDataSource {

    private volatile static TVRepository INSTANCE = null;
    private final LocalRepository localRepository;
    private final RemoteRepository remoteRepository;
    private final AppExecutors appExecutors;

    public TVRepository(LocalRepository localRepository, RemoteRepository remoteRepository, AppExecutors appExecutors) {
        this.localRepository = localRepository;
        this.remoteRepository = remoteRepository;
        this.appExecutors = appExecutors;
    }

    public static TVRepository getInstanceTV(LocalRepository local, RemoteRepository remote, AppExecutors exec) {
        if (INSTANCE == null) {
            INSTANCE = new TVRepository(local, remote, exec);
        }
        return INSTANCE;
    }

    @Override
    public LiveData<Resource<List<TVEntity>>> getAllTVShow() {
        return new NetworkBoundResource<List<TVEntity>, List<TVResponse>>(appExecutors) {
            @Override
            public LiveData<List<TVEntity>> loadFromDB() {
                return localRepository.getAllTVShow();
            }

            @Override
            public Boolean shouldFetch(List<TVEntity> data) {
                return (data == null) || (data.size() == 0);
            }

            @Override
            public LiveData<APIResponse<List<TVResponse>>> createCall() {
                return remoteRepository.getAllTVShowLiveData();
            }

            @Override
            public void saveCallResult(List<TVResponse> response) {
                List<TVEntity> tvEntities = new ArrayList<>();

                for (TVResponse tvResponse : response) {

                    tvEntities.add(new TVEntity(
                            tvResponse.getId(),
                            tvResponse.getTitle(),
                            tvResponse.getDate(),
                            tvResponse.getOverview(),
                            tvResponse.getImage(),
                            tvResponse.getRating(),
                            tvResponse.getVote(),
                            tvResponse.getRevenue(),
                            tvResponse.getFavorite()));
                }

                localRepository.insertTVShow(tvEntities);
            }
        }.asLiveData();
    }

    @Override
    public LiveData<Resource<List<TVEntity>>> getTVShowById(int tvShowId) {
        return new NetworkBoundResource<List<TVEntity>, List<TVResponse>>(appExecutors){

            @Override
            protected LiveData<List<TVEntity>> loadFromDB() {
                return localRepository.getTVShowById(tvShowId);
            }

            @Override
            protected Boolean shouldFetch(List<TVEntity> data) {
                return (data == null || data.size() == 0);
            }

            @Override
            protected LiveData<APIResponse<List<TVResponse>>> createCall() {
                return remoteRepository.getTVShowById(tvShowId);
            }

            @Override
            protected void saveCallResult(List<TVResponse> data) {

                List<TVEntity> entities = new ArrayList<>();

                for (TVResponse response : data) {

                    entities.add(new TVEntity(
                            response.getId(),
                            response.getTitle(),
                            response.getDate(),
                            response.getOverview(),
                            response.getImage(),
                            response.getRating(),
                            response.getVote(),
                            response.getRevenue(),
                            response.getFavorite()));
                }

                localRepository.insertTVShow(entities);
            }
        }.asLiveData();
    }

    @Override
    public LiveData<Resource<List<TVEntity>>> getTVShowRecomm(int tvShowId) {
        return new NetworkBoundResource<List<TVEntity>, List<TVResponse>>(appExecutors){

            @Override
            protected LiveData<List<TVEntity>> loadFromDB() {
                return localRepository.getTVShowRecomm(tvShowId);
            }

            @Override
            protected Boolean shouldFetch(List<TVEntity> data) {
                return (data == null || data.size() == 0);
            }

            @Override
            protected LiveData<APIResponse<List<TVResponse>>> createCall() {
                return remoteRepository.getTVShowRecomm(tvShowId);
            }

            @Override
            protected void saveCallResult(List<TVResponse> data) {

                List<TVEntity> entities = new ArrayList<>();

                for (TVResponse response : data) {

                    entities.add(new TVEntity(
                            response.getId(),
                            response.getTitle(),
                            response.getDate(),
                            response.getOverview(),
                            response.getImage(),
                            response.getRating(),
                            response.getVote(),
                            response.getRevenue(),
                            response.getFavorite()));
                }

                localRepository.insertTVShow(entities);
            }
        }.asLiveData();
    }
}

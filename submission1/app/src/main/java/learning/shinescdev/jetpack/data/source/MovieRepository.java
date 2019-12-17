package learning.shinescdev.jetpack.data.source;

import androidx.lifecycle.LiveData;


import java.util.ArrayList;
import java.util.List;

import learning.shinescdev.jetpack.data.source.local.LocalRepository;
import learning.shinescdev.jetpack.data.source.local.entity.MovieEntity;
import learning.shinescdev.jetpack.data.source.remote.APIResponse;
import learning.shinescdev.jetpack.data.source.remote.RemoteRepository;
import learning.shinescdev.jetpack.data.source.remote.response.MovieResponse;
import learning.shinescdev.jetpack.utils.AppExecutors;
import learning.shinescdev.jetpack.vo.Resource;

public class MovieRepository implements MovieDataSource {

    private volatile static MovieRepository INSTANCE = null;
    private final LocalRepository localRepository;
    private final RemoteRepository remoteRepository;
    private final AppExecutors appExecutors;

    public MovieRepository(LocalRepository localRepository, RemoteRepository remoteRepository, AppExecutors appExecutors) {
        this.localRepository = localRepository;
        this.remoteRepository = remoteRepository;
        this.appExecutors = appExecutors;
    }

    public static MovieRepository getInstance(LocalRepository local, RemoteRepository remote, AppExecutors exec) {
        if (INSTANCE == null) {
            INSTANCE = new MovieRepository(local, remote, exec);
        }
        return INSTANCE;
    }

    @Override
    public LiveData<Resource<List<MovieEntity>>> getAllMovies() {
        return new NetworkBoundResource<List<MovieEntity>, List<MovieResponse>>(appExecutors) {
            @Override
            public LiveData<List<MovieEntity>> loadFromDB() {
                return localRepository.getAllMovies();
            }

            @Override
            public Boolean shouldFetch(List<MovieEntity> data) {
                return (data == null) || (data.size() == 0);
            }

            @Override
            public LiveData<APIResponse<List<MovieResponse>>> createCall() {
                return remoteRepository.getAllMovieAsLiveData();
            }

            @Override
            public void saveCallResult(List<MovieResponse> response) {
                List<MovieEntity> movieEntities = new ArrayList<>();

                for (MovieResponse movieResponse : response) {

                    movieEntities.add(new MovieEntity(
                            movieResponse.getId(),
                            movieResponse.getTitle(),
                            movieResponse.getDate(),
                            movieResponse.getOverview(),
                            movieResponse.getImage(),
                            movieResponse.getRating(),
                            movieResponse.getVote(),
                            movieResponse.getRevenue(),
                            movieResponse.getFavorite()));
                }

                localRepository.insertMovie(movieEntities);
            }
        }.asLiveData();
    }

    @Override
    public LiveData<Resource<List<MovieEntity>>> getMovieById(int movieId) {
        return new NetworkBoundResource<List<MovieEntity>, List<MovieResponse>>(appExecutors) {

            @Override
            protected LiveData<List<MovieEntity>> loadFromDB() {
                return localRepository.getMovieById(movieId);
            }

            @Override
            protected Boolean shouldFetch(List<MovieEntity> data) {
                return (data == null || data.size() == 0);
            }

            @Override
            protected LiveData<APIResponse<List<MovieResponse>>> createCall() {
                return remoteRepository.getMovieById(movieId);
            }

            @Override
            protected void saveCallResult(List<MovieResponse> data) {
                List<MovieEntity> movieEntities = new ArrayList<>();

                for (MovieResponse movieResponse : data) {

                    movieEntities.add(new MovieEntity(
                            movieResponse.getId(),
                            movieResponse.getTitle(),
                            movieResponse.getDate(),
                            movieResponse.getOverview(),
                            movieResponse.getImage(),
                            movieResponse.getRating(),
                            movieResponse.getVote(),
                            movieResponse.getRevenue(),
                            movieResponse.getFavorite()));
                }

                localRepository.insertMovie(movieEntities);
            }
        }.asLiveData();
    }
}

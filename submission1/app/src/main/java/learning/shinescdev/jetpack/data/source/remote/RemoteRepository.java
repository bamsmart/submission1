package learning.shinescdev.jetpack.data.source.remote;

import android.os.Handler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import learning.shinescdev.jetpack.data.source.remote.response.MovieResponse;
import learning.shinescdev.jetpack.data.source.remote.response.TVResponse;
import learning.shinescdev.jetpack.utils.EspressoIdlingResource;
import learning.shinescdev.jetpack.utils.JsonHelper;

public class RemoteRepository {
    public final String TAG = RemoteRepository.class.getSimpleName();
    private static RemoteRepository INSTANCE;
    private final long SERVICE_LATENCY_IN_MILIS = 2000;
    private JsonHelper jsonHelper;

    private RemoteRepository(JsonHelper jsonHelper) {
        this.jsonHelper = jsonHelper;
    }

    public static RemoteRepository getInstance(JsonHelper jsonHelper) {
        if (INSTANCE == null) {
            INSTANCE = new RemoteRepository(jsonHelper);
        }

        return INSTANCE;
    }

    public LiveData<APIResponse<List<MovieResponse>>> getAllMovieAsLiveData() {
        EspressoIdlingResource.increment();
        MutableLiveData<APIResponse<List<MovieResponse>>> resultMovie = new MutableLiveData<>();

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            resultMovie.setValue(APIResponse.success(jsonHelper.loadMovies()));
            if (!EspressoIdlingResource.getEspressoIdlingResource().isIdleNow()) {
                EspressoIdlingResource.decrement();
            }
        }, SERVICE_LATENCY_IN_MILIS);

        return resultMovie;
    }

    public LiveData<APIResponse<List<MovieResponse>>> getMovieById(int movieId) {
        EspressoIdlingResource.increment();
        MutableLiveData<APIResponse<List<MovieResponse>>> resultMovie = new MutableLiveData<>();

        Handler handler = new Handler();
        handler.postDelayed(() -> {
                    resultMovie.setValue(APIResponse.success(jsonHelper.loadMovieById(movieId)));
                    if (EspressoIdlingResource.getEspressoIdlingResource().isIdleNow()) {
                        EspressoIdlingResource.decrement();
                    }

                }, SERVICE_LATENCY_IN_MILIS

        );

        return resultMovie;
    }


    public LiveData<APIResponse<List<TVResponse>>> getAllTVShowLiveData() {
        EspressoIdlingResource.increment();
        MutableLiveData<APIResponse<List<TVResponse>>> resultTVShow = new MutableLiveData<>();

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            resultTVShow.setValue(APIResponse.success(jsonHelper.loadTVShows()));

        }, SERVICE_LATENCY_IN_MILIS);

        return resultTVShow;
    }
}

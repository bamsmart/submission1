package learning.shinescdev.jetpack.data.source;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import learning.shinescdev.jetpack.data.source.remote.APIResponse;
import learning.shinescdev.jetpack.utils.AppExecutors;
import learning.shinescdev.jetpack.vo.Resource;

public abstract class NetworkBoundResource<ResultType, RequestType> {

    private MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();

    private AppExecutors mExecutors;

    protected void onFecthFailed() {
    }

    protected abstract LiveData<ResultType> loadFromDB();

    protected abstract Boolean shouldFetch(ResultType data);

    protected abstract LiveData<APIResponse<RequestType>> createCall();

    protected abstract void saveCallResult(RequestType data);

    public LiveData<Resource<ResultType>> asLiveData() {
        return result;
    }


    private void fecthFromNetwork(LiveData<ResultType> dbSource) {
        LiveData<APIResponse<RequestType>> apiResponse = createCall();

        result.addSource(dbSource, newData ->
                result.setValue(Resource.loading(newData))
        );

        result.addSource(apiResponse, response -> {
            result.removeSource(apiResponse);
            result.removeSource(dbSource);

            switch (response.status) {
                case SUCCESS:
                    mExecutors.diskIO().execute(() -> {
                                saveCallResult(response.body);

                                mExecutors.mainThread().execute(() ->{
                                    result.addSource(loadFromDB(),
                                            newData -> result.setValue(Resource.success(newData)));
                                });
                            }
                    );
                    break;
                case EMPTY:
                        mExecutors.mainThread().execute(() -> {
                           result.addSource(loadFromDB(), newData -> result.setValue(Resource.success(newData)));
                        });
                        break;
                case ERROR:
                    onFecthFailed();
                    result.addSource(dbSource, newData ->
                            result.setValue(Resource.error(response.message, newData)));
                    break;
            }
        });
    }


    public NetworkBoundResource(AppExecutors appExecutors) {
        this.mExecutors = appExecutors;
        result.setValue(Resource.loading(null));

        LiveData<ResultType> dbResource = loadFromDB();

        result.addSource(dbResource, data ->{
            result.removeSource(dbResource);
            if(shouldFetch(data)){
                fecthFromNetwork(dbResource);
            }else{
                result.addSource(dbResource, newData -> result.setValue(Resource.success(newData)));
            }
        });

    }

}

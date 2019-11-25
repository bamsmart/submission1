package learning.shinesdev.mylastmovie.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import learning.shinesdev.mylastmovie.api.APIServiceTVShow;
import learning.shinesdev.mylastmovie.api.ApiUtils;
import learning.shinesdev.mylastmovie.model.TVShow;
import learning.shinesdev.mylastmovie.model.TVShowCredits;
import learning.shinesdev.mylastmovie.model.TVShowModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TVShowRepository {
    private static TVShowRepository newsRepository;

    public static TVShowRepository getInstance(){
        if (newsRepository == null){
            newsRepository = new TVShowRepository();
        }
        return newsRepository;
    }

    private final APIServiceTVShow serviceTVShow;

    private TVShowRepository(){
        serviceTVShow = ApiUtils.getAPIServiceTVShow();
    }

    public MutableLiveData<TVShow> getPopular(String lang, String key){
        final MutableLiveData<TVShow> tvShowData = new MutableLiveData<>();
        serviceTVShow.getPopular(lang,key).enqueue(new Callback<TVShow>() {
            @Override
            public void onResponse(@NonNull Call<TVShow> call, @NonNull Response<TVShow> response) {
                if (response.isSuccessful()){
                    tvShowData.setValue(response.body());
                }
            }
            @Override
            public void onFailure(@NonNull Call<TVShow> call, @NonNull Throwable t) {
                tvShowData.setValue(null);
                Log.d("DATA NULL",""+t.getMessage());
            }
        });
        return tvShowData;
    }

    public MutableLiveData<TVShow> getSearch(String keySrc, String lang, String key){
        final MutableLiveData<TVShow> tvShowData = new MutableLiveData<>();
        serviceTVShow.getSearch(keySrc,lang,key).enqueue(new Callback<TVShow>() {
            @Override
            public void onResponse(@NonNull Call<TVShow> call, @NonNull Response<TVShow> response) {
                if (response.isSuccessful()){
                    tvShowData.setValue(response.body());
                }
            }
            @Override
            public void onFailure(@NonNull Call<TVShow> call, @NonNull Throwable t) {
                tvShowData.setValue(null);
                Log.d("DATA NULL",""+t.getMessage());
            }
        });
        return tvShowData;
    }

    public MutableLiveData<TVShow> getRecommendations(int id, String lang,String key){
        final MutableLiveData<TVShow> tvShowData = new MutableLiveData<>();
        serviceTVShow.getRecommendations(id,lang,key).enqueue(new Callback<TVShow>() {
            @Override
            public void onResponse(@NonNull Call<TVShow> call, @NonNull Response<TVShow> response) {
                if (response.isSuccessful()){
                    tvShowData.setValue(response.body());
                    Log.d("",""+response.body());
                }
            }
            @Override
            public void onFailure(@NonNull Call<TVShow> call, @NonNull Throwable t) {
                //tvShowData.setValue(null);
                Log.d("DATA MOVIE RECOM",""+t.getMessage());
            }
        });
        return tvShowData;
    }

    public MutableLiveData<TVShowModel> getDetail(int id, String lang, String key){
        final MutableLiveData<TVShowModel> tvShowData = new MutableLiveData<>();
        serviceTVShow.getDetails(id,lang,key).enqueue(new Callback<TVShowModel>() {
            @Override
            public void onResponse(@NonNull Call<TVShowModel> call, @NonNull Response<TVShowModel> response) {
                if (response.isSuccessful()){
                    tvShowData.setValue(response.body());
                }
            }
            @Override
            public void onFailure(@NonNull Call<TVShowModel> call, @NonNull Throwable t) {
                tvShowData.setValue(null);
                Log.d("DATA NULL",""+t.getMessage());
            }
        });
        return tvShowData;
    }

    public MutableLiveData<TVShowCredits> getCredits(int id, String key){
        final MutableLiveData<TVShowCredits> tvShowCredit = new MutableLiveData<>();
        serviceTVShow.getCredits(id,key).enqueue(new Callback<TVShowCredits>() {
            @Override
            public void onResponse(@NonNull Call<TVShowCredits> call, @NonNull Response<TVShowCredits> response) {
                if(response.isSuccessful()){
                    tvShowCredit.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<TVShowCredits> call, @NonNull Throwable t) {
                Log.d("ON Failure",t.getMessage());
            }
        });
        return tvShowCredit;
    }
}

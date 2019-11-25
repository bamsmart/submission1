package learning.shinesdev.mylastmovie.api;


import learning.shinesdev.mylastmovie.model.TVShow;
import learning.shinesdev.mylastmovie.model.TVShowCredits;
import learning.shinesdev.mylastmovie.model.TVShowModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface APIServiceTVShow {
    @GET("3/discover/tv")
    Call<TVShow> getPopular(
            @Query("language") String lang,
            @Query("api_key") String key
    );

    @GET("3/search/tv")
    Call<TVShow> getSearch(
            @Query("query") String query,
            @Query("language") String lang,
            @Query("api_key") String key
    );

    @GET("3/tv/{tv_id}/recommendations")
    Call<TVShow> getRecommendations(
            @Path("tv_id") int id,
            @Query("language") String lang,
            @Query("api_key") String key
    );

    @GET("3/tv/{tv_id}/credits")
    Call<TVShowCredits> getCredits(
            @Path("tv_id") int id,
            @Query("api_key") String key
    );

    @GET("3/tv/{tv_id}")
    Call<TVShowModel> getDetails(
            @Path("tv_id") int id,
            @Query("language") String lang,
            @Query("api_key") String key
    );
}

package learning.shinesdev.mylastmovie.api;


import learning.shinesdev.mylastmovie.model.Movie;
import learning.shinesdev.mylastmovie.model.MovieCredits;
import learning.shinesdev.mylastmovie.model.MovieModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface APIServiceMovie {
    @GET("3/discover/movie")
    Call<Movie> getDiscover(
            @Query("language") String lang,
            @Query("api_key") String key
    );

    @GET("3/discover/movie")
    Call<Movie> getReleased(
            @Query("primary_release_date.gte") String released_date_1,
            @Query("primary_release_date.lte") String released_date_2,
            @Query("language") String lang,
            @Query("api_key") String key
    );

    @GET("3/movie/popular")
    Call<Movie> getPopular(
            @Query("language") String lang,
            @Query("api_key") String key
    );
    @GET("3/search/movie")
    Call<Movie> getSearch(
            @Query("query") String query,
            @Query("language") String lang,
            @Query("api_key") String key
    );

    @GET("3/movie/{movie_id}/recommendations")
    Call<Movie> getRecommendations(
            @Path("movie_id") int id,
            @Query("language") String lang,
            @Query("api_key") String key
    );

    @GET("3/movie/{movie_id}/credits")
    Call<MovieCredits> getCredits(
            @Path("movie_id") int id,
            @Query("api_key") String key
    );

    @GET("3/movie/{movie_id}")
    Call<MovieModel> getDetails(
            @Path("movie_id") int id,
            @Query("language") String lang,
            @Query("api_key") String key
    );
}

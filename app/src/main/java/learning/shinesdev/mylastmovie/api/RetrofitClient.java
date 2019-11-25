package learning.shinesdev.mylastmovie.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressWarnings("deprecation")
class RetrofitClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    private static final Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(ApiUtils.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    private static final Retrofit retrofit2 = builder.build();

    private static final HttpLoggingInterceptor loggingInterceptor =
            new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BASIC);

    private static final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    public static <S> S createService(Class<S> serviceClass)
    {
        if (!httpClient.interceptors().contains(loggingInterceptor)) {
            httpClient.addInterceptor(loggingInterceptor);
            builder.client(httpClient.build());
        }
        return retrofit2.create(serviceClass);
    }
}
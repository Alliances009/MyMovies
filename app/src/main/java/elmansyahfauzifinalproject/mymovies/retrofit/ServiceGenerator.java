package elmansyahfauzifinalproject.mymovies.retrofit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ALLIANCES on 7/31/2017.
 */

public class ServiceGenerator {
    private static final String BASE_URL = "http://api.themoviedb.org/3/";
    private static OkHttpClient.Builder httpClient =
            new OkHttpClient.Builder()
                    .addInterceptor(new InterceptorApiKey());
    private static Retrofit.Builder retrofitBuilder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build());
    public static <S> S createService(Class<S> serviceClass){
        return retrofitBuilder.build().create(serviceClass);
    }

}

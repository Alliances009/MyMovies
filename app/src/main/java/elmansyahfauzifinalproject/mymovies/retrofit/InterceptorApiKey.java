package elmansyahfauzifinalproject.mymovies.retrofit;

import java.io.IOException;

import elmansyahfauzifinalproject.mymovies.BuildConfig;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ALLIANCES on 7/31/2017.
 */

public class InterceptorApiKey implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        final HttpUrl url = chain.request()
                .url()
                .newBuilder()
                .addQueryParameter("api_key", BuildConfig.THE_MOVIE_DB_API)
                .build();
        final Request request = chain.request().newBuilder().url(url).build();
        return chain.proceed(request);
    }
}

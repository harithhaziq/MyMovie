package com.example.mymovieapplication.RestAPI;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {
    private static RetrofitBuilder instance = null;
    public static final String BASE_URL = "https://api.themoviedb.org/3/";
//    static String AUTHORIZATION_HEADER_KEY = "Authorization";
//    public String AUTHORIZATION_HEADER_VALUE = "Bearer 89a571acaf96541bdee2b19060fc9980";
    private static MovieService movieService;

    public static synchronized MovieService getMovieService(){
        if(movieService == null){
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            headers.put("Accept", "application/json");

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request().newBuilder()
                                    .headers(Headers.of(headers))
                                    .addHeader(MovieService.AUTHORIZATION_HEADER_KEY, MovieService.AUTHORIZATION_HEADER_VALUE)
                                    .build();
                            return chain.proceed(request);
                        }
                    })
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(RetrofitBuilder.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            movieService = retrofit.create(MovieService.class);
        }
        return movieService;

    }
}

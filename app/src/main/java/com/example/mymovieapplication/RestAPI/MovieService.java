package com.example.mymovieapplication.RestAPI;

import com.example.mymovieapplication.Model.Movie.JsonResponse;
import com.example.mymovieapplication.Model.Movie.Result;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Query;

public interface MovieService {

    static String AUTHORIZATION_HEADER_KEY = "Authorization";
    public String AUTHORIZATION_HEADER_VALUE = "89a571acaf96541bdee2b19060fc9980";

    @GET("movie/now_playing")
    Call<JsonResponse> getMovieList(
            @HeaderMap Map<String, String> headers,
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );
}

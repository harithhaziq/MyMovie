package com.example.mymovieapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ScrollView;

import com.example.mymovieapplication.Adapter.MovieListAdapter;
import com.example.mymovieapplication.Model.Movie.JsonResponse;
import com.example.mymovieapplication.Model.Movie.Result;
import com.example.mymovieapplication.RestAPI.RetrofitBuilder;
import com.example.mymovieapplication.RestAPI.MovieService;
import com.example.mymovieapplication.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NestedScrollView nestedScrollView;
    private ScrollView scrollView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Result> movieList = new ArrayList<>();
    private MovieListAdapter movieListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private RetrofitBuilder retrofitBuilderService;
    private int currentPageNumber = 1 ; // By default is one
    private int maxPageNo = 10;
    int totalItem = 99;
    boolean isScrolling = false;
    boolean isRunning = false;
    private MovieService movieService = RetrofitBuilder.getMovieService();
    private String TAG = "MovieListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        recyclerView = findViewById(R.id.rv_movie_list);
//        nestedScrollView = findViewById(R.id.nsv_movie_scroll_mla);
        swipeRefreshLayout = findViewById(R.id.srl_swipeRefreshLayout_mla);
//        scrollView = findViewById(R.id.sv_movie_mla);

        setTitle("Test Title Movie List");

        linearLayoutManager = new LinearLayoutManager(this);
        loadMovieData(getApplicationContext(), currentPageNumber);

        reloadView();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                    // End of list reached, call API to fetch more data
                    currentPageNumber++; // increment page number
                    loadMovieData(MovieListActivity.this, currentPageNumber);
                    Log.d(TAG, "onScrolled: " + visibleItemCount + " firstvisibleitemposition " + firstVisibleItemPosition);
                }
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true;
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                movieList.clear();
                refreshMovieData(MovieListActivity.this, currentPageNumber);
            }
        });
    }

    private void loadMovieData(Context context, int pageNo) {
        isRunning = true;
        String language = "en-US";

        Call<JsonResponse> call = movieService.getMovieList(Utils.getHeadersMap(), MovieService.AUTHORIZATION_HEADER_VALUE, language, pageNo);
        call.enqueue(new Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                List<Result> downloadedMovie = response.body().getResults();
                int totalItemSize = response.body().getTotalResults();

                for(int i=0; i < downloadedMovie.size(); i++){
                    movieList.add(downloadMovieItem(downloadedMovie.get(i)));
                }

                reloadView();
                isRunning = false;
//                if(movieList.size() != totalItemSize){
//                    pageNo++;
//                    loadMovieData(context, );
//                    Log.d(TAG, "onResponse: succeed movie list size == total item size " + totalItemSize);
//                }
                Log.d(TAG, "onResponse: onresponse " + totalItemSize + "\ntotalMovieList: " + movieList.size());

            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: fail movie list size != total item size " + t.getMessage());
                isRunning = false;
            }
        });

    }

    private void refreshMovieData(Context context, int pageNo){
        isRunning = true;
        String language = "en-US";

        Call<JsonResponse> call = movieService.getMovieList(Utils.getHeadersMap(), MovieService.AUTHORIZATION_HEADER_VALUE, language, pageNo);
        call.enqueue(new Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                List<Result> downloadedMovie = response.body().getResults();
                int totalItemSize = response.body().getTotalResults();

                for(int i=0; i < downloadedMovie.size(); i++){
                    movieList.add(downloadMovieItem(downloadedMovie.get(i)));
                }

                swipeRefreshLayout.setRefreshing(false);
                reloadView();
                isRunning = false;
//                if(movieList.size() != totalItemSize){
//                    pageNo++;
//                    loadMovieData(context, );
//                    Log.d(TAG, "onResponse: succeed movie list size == total item size " + totalItemSize);
//                }
                Log.d(TAG, "onResponse: onresponse " + totalItemSize + "\ntotalMovieList: " + movieList.size());

            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: fail movie list size != total item size " + t.getMessage());
                isRunning = false;
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    private void reloadView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MovieListAdapter(this, movieList));
    }

    private Result downloadMovieItem(Result downloadedMovie){
            Result insertMovieData = new Result();

            insertMovieData.setAdult(downloadedMovie.isAdult());
            insertMovieData.setOriginalTitle(downloadedMovie.getOriginalTitle());
            insertMovieData.setPosterPath(downloadedMovie.getPosterPath());

        return insertMovieData;
    }
}
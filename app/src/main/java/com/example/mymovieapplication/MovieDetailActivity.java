package com.example.mymovieapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.mymovieapplication.Model.Movie.Result;

public class MovieDetailActivity extends AppCompatActivity {

    private Result movieItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        getIntents();
        
        loadView();
    }

    private void loadView() {
    }

    private void getIntents() {
        
    }
}
package com.example.mymovieapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymovieapplication.Model.Movie.Result;
import com.example.mymovieapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;



public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {

    private Context context;
    private List<Result> movieList;
    private String baseUrlPoster = "https://image.tmdb.org/t/p/w200";


    public MovieListAdapter(Context context, List<Result> movieList){
        this.movieList = movieList;
    }


    @NonNull
    @Override
    public MovieListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_movie, viewGroup,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListAdapter.ViewHolder holder, int i) {
        final Result movieItem = movieList.get(i);
        String imageUrl = baseUrlPoster + movieItem.getPosterPath();

        Picasso.get()
                .load(imageUrl)
                .into(holder.ivPoster);

        holder.tvIsAdult.setText(String.valueOf(movieItem.isAdult()));
        holder.tvTitle.setText(movieItem.getTitle());
        holder.tvDesc.setText(movieItem.getOverview());
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvTitle, tvDesc, tvIsAdult;
        public ImageView ivPoster;

        public ViewHolder(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_movie_title_mla);
            tvDesc = itemView.findViewById(R.id.tv_movie_desc_mla);
            tvIsAdult = itemView.findViewById(R.id.tv_movie_adult_mla);
            ivPoster = itemView.findViewById(R.id.iv_movie_poster_mla);
        }
    }
}

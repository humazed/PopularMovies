package com.example.huma.popularmovies.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.huma.popularmovies.R;
import com.example.huma.popularmovies.model.Movie;

import java.util.List;

/**
 * User: YourPc
 * Date: 3/29/2017
 */

public class MoviesAdapter extends BaseQuickAdapter<Movie, BaseViewHolder> {
    private static final String TAG = MoviesAdapter.class.getSimpleName();

    public MoviesAdapter(List<Movie> movies) {
        super(R.layout.row_movie, movies);
    }


    @Override
    protected void convert(BaseViewHolder viewHolder, Movie movie) {
        viewHolder.setText(R.id.movie_title, movie.getTitle());

        Glide.with(mContext)
                .load("http://image.tmdb.org/t/p/w185/" + movie.getPosterPath())
                .crossFade()
                .into((ImageView) viewHolder.getView(R.id.movie_image));

    }
}


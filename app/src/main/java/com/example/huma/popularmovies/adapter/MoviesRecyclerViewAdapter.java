package com.example.huma.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.huma.popularmovies.R;
import com.example.huma.popularmovies.model.Movie;
import com.example.huma.popularmovies.ui.MovieDetailActivity;
import com.example.huma.popularmovies.ui.MovieDetailFragment;
import com.example.huma.popularmovies.ui.MovieListActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by huma on 12/25/2015.
 */
public class MoviesRecyclerViewAdapter extends RecyclerView.Adapter<MoviesRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = MoviesRecyclerViewAdapter.class.getSimpleName();

    private MovieListActivity mMovieListActivity;
    private final List<Movie> mMovies;

    public MoviesRecyclerViewAdapter(MovieListActivity MovieListActivity, List<Movie> movies) {
        mMovieListActivity = MovieListActivity;
        mMovies = movies;
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mMovie = mMovies.get(position);

        holder.mItemTitle.setText(mMovies.get(position).getOriginalTitle());
//        Log.d(TAG, "onBindViewHolder " + "http://image.tmdb.org/t/p/w185/" + holder.mMovie.getPosterPath());

        Glide.with(mMovieListActivity)
//                .load("http://image.tmdb.org/t/p/w185//fYzpM9GmpBlIC893fNjoWCwE24H.jpg")
                .load("http://image.tmdb.org/t/p/w185/" + holder.mMovie.getPosterPath())
                .centerCrop()
                .placeholder(R.color.colorPrimary)
                .crossFade()
                .into(holder.mItemImage);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMovieListActivity.mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putParcelable(MovieDetailFragment.KEY_MOVIE, holder.mMovie);
                    MovieDetailFragment fragment = new MovieDetailFragment();
                    fragment.setArguments(arguments);
                    mMovieListActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.movie_detail_container, fragment)
                            .commit();
                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, MovieDetailActivity.class);
                    intent.putExtra(MovieDetailFragment.KEY_MOVIE, holder.mMovie);

                    context.startActivity(intent);
                }
            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public Movie mMovie;
        public View mView;
        @Bind(R.id.item_image) ImageView mItemImage;
        @Bind(R.id.item_title) TextView mItemTitle;


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mView = view;
        }

    }
}

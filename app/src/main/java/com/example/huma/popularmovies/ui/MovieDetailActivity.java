package com.example.huma.popularmovies.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.huma.popularmovies.R;
import com.example.huma.popularmovies.model.Movie;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * An activity representing a single movie detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link MovieListActivity}.
 */
public class MovieDetailActivity extends AppCompatActivity {
    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    public static final String FAV_BUTTON_STATE = "favButtonState";

    @Bind(R.id.backdrop_path_image_view) ImageView mBackdropPathImageView;
    @Bind(R.id.detail_toolbar) Toolbar mDetailToolbar;
    @Bind(R.id.favourite_fab) FloatingActionButton mFavouriteFab;

    private boolean isSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        setSupportActionBar(mDetailToolbar);

        if (savedInstanceState != null) {
            isSelected = savedInstanceState.getBoolean(FAV_BUTTON_STATE);
            mFavouriteFab.setSelected(isSelected);
            Log.d(TAG, "onCreate savedInstanceState" + isSelected);
        } else Log.d(TAG, "onCreate savedInstanceState" + "null " + isSelected);

        //get selected Movie MovieListActivity.
        Movie mMovie = getIntent().getParcelableExtra(MovieDetailFragment.KEY_MOVIE);


        mFavouriteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 1/27/2016 star the movie
                if (!isSelected) {
                    isSelected = true;
                    mFavouriteFab.setSelected(true);
                } else {
                    isSelected = false;
                    mFavouriteFab.setSelected(false);
                }
            }
        });

        mBackdropPathImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Glide.with(this)
                .load("http://image.tmdb.org/t/p/w500/" + mMovie.getBackdropPath())
                .centerCrop()
                .placeholder(R.color.colorPrimary)
                .crossFade()
                .into(mBackdropPathImageView);


        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Log.d(TAG, "onCreate " + "outSide");
        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putParcelable(MovieDetailFragment.KEY_MOVIE, mMovie);
            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();
            Log.d(TAG, "onCreate " + "inside");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FAV_BUTTON_STATE, isSelected);
        Log.d(TAG, "onSaveInstanceState " + isSelected);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, MovieListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

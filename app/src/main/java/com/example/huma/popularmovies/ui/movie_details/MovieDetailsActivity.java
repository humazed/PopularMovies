package com.example.huma.popularmovies.ui.movie_details;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.huma.popularmovies.R;
import com.example.huma.popularmovies.db.MoviesDBProviderUtils;
import com.example.huma.popularmovies.model.Movie;
import com.example.huma.popularmovies.ui.MovieDetailFragment;
import com.example.huma.popularmovies.ui.MovieListActivity;

import at.blogc.android.views.ExpandableTextView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An activity representing a single movie detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link MovieListActivity}.
 */
public class MovieDetailsActivity extends AppCompatActivity {
    private static final String TAG = MovieDetailsActivity.class.getSimpleName();
    public static final String FAV_BUTTON_STATE = "favButtonState";

    @BindView(R.id.backdrop_imageView) ImageView mBackdropImageView;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.collapsible_toolbar) CollapsingToolbarLayout mCollapsibleToolbar;
    @BindView(R.id.app_bar) AppBarLayout mAppBar;
    @BindView(R.id.follow_button) Button mFollowButton;
    @BindView(R.id.tv_show_poster) ImageView mTvShowPoster;
    @BindView(R.id.title_textView) TextView mTitleTextView;
    @BindView(R.id.state_textView) TextView mStateTextView;
    @BindView(R.id.description_textView) ExpandableTextView mDescriptionTextView;
    @BindView(R.id.read_more_textView) TextView mReadMoreTextView;
    @BindView(R.id.tv_show_description_container) LinearLayout mTvShowDescriptionContainer;
    @BindView(R.id.info_bar_textView) TextView mInfoBarTextView;
    @BindView(R.id.episode_tracker_title_textView) TextView mEpisodeTrackerTitleTextView;
    @BindView(R.id.episode_tracker_recyclerView) RecyclerView mEpisodeTrackerRecyclerView;
    @BindView(R.id.tv_show_progress_bar) ProgressBar mTvShowProgressBar;
    @BindView(R.id.scroll_view) NestedScrollView mScrollView;
    @BindView(R.id.coordinator) CoordinatorLayout mCoordinator;

//    @BindView(R.id.backdrop_path_image_view) ImageView mBackdropPathImageView;
//    @BindView(R.id.detail_toolbar) Toolbar mDetailToolbar;
//    @BindView(R.id.favourite_fab) FloatingActionButton mFavouriteFab;

    private boolean isSelected;
    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        if (savedInstanceState != null) {
            isSelected = savedInstanceState.getBoolean(FAV_BUTTON_STATE);
            mFollowButton.setSelected(isSelected);
        }

        //get selected Movie MovieListActivity.
        mMovie = getIntent().getParcelableExtra(MovieDetailFragment.KEY_MOVIE);

        final MoviesDBProviderUtils provider = new MoviesDBProviderUtils(MovieDetailsActivity.this);

        getSupportActionBar().setTitle(mMovie.getTitle());

        mFollowButton.setSelected(provider.isFav(mMovie));
        mFollowButton.setOnClickListener(view -> {
            if (mFollowButton.isSelected()) {
                mFollowButton.setSelected(false);
                mFollowButton.setText(R.string.btn_follow);
                provider.deleteMovie(mMovie);
            } else {
                mFollowButton.setSelected(true);
                mFollowButton.setText(R.string.btn_unfollow);
                provider.addMovie(mMovie);
            }
        });

        mBackdropImageView.setOnClickListener(v -> {

        });

        Glide.with(this)
                .load("http://image.tmdb.org/t/p/w500/" + mMovie.getBackdropPath())
                .centerCrop()
                .placeholder(R.color.colorPrimary)
                .crossFade()
                .into(mBackdropImageView);


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

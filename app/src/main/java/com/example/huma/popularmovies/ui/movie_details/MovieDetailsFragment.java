package com.example.huma.popularmovies.ui.movie_details;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.huma.popularmovies.R;
import com.example.huma.popularmovies.adapter.ReviewsAdapter;
import com.example.huma.popularmovies.adapter.TrailersAdapter;
import com.example.huma.popularmovies.api.TheMovieDbAPI;
import com.example.huma.popularmovies.db.MoviesDBProviderUtils;
import com.example.huma.popularmovies.model.Movie;
import com.example.huma.popularmovies.model.Reviews;
import com.example.huma.popularmovies.model.Trailers;

import at.blogc.android.views.ExpandableTextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDetailsFragment extends Fragment {
    private static final String TAG = MovieDetailsFragment.class.getSimpleName();
    private static final String ARG_MOVIE = "movie";
    public static final String FAV_BUTTON_STATE = "favButtonState";
    public static final String KEY_MOVIE = "move";

    @BindView(R.id.backdrop_imageView) ImageView mBackdropImageView;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.collapsible_toolbar) CollapsingToolbarLayout mCollapsibleToolbar;
    @BindView(R.id.app_bar) AppBarLayout mAppBar;
    @BindView(R.id.follow_button) Button mFollowButton;
    @BindView(R.id.poster_imageView) ImageView mPosterImageView;
    @BindView(R.id.title_textView) TextView mTitleTextView;
    @BindView(R.id.state_textView) TextView mStateTextView;
    @BindView(R.id.content_textView) ExpandableTextView mDescriptionTextView;
    @BindView(R.id.read_more_textView) TextView mReadMoreTextView;
    @BindView(R.id.tv_show_description_container) LinearLayout mTvShowDescriptionContainer;
    @BindView(R.id.info_bar_textView) TextView mInfoBarTextView;
    @BindView(R.id.tv_show_progress_bar) ProgressBar mTvShowProgressBar;
    @BindView(R.id.scroll_view) NestedScrollView mScrollView;
    @BindView(R.id.coordinator) CoordinatorLayout mCoordinator;
    @BindView(R.id.trailers_title_textView) TextView mTrailersTitleTextView;
    @BindView(R.id.trailers_recyclerView) RecyclerView mTrailersRecyclerView;
    @BindView(R.id.reviews_title_textView) TextView mReviewsTitleTextView;
    @BindView(R.id.reviews_recyclerView) RecyclerView mReviewsRecyclerView;
    Unbinder unbinder;

    private Movie mMovie;
    private MoviesDBProviderUtils mProvider;


    private OnFragmentInteractionListener mListener;

    public MovieDetailsFragment() { /*Required empty public constructor*/ }

    public static MovieDetailsFragment newInstance(Movie movie) {
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_MOVIE, movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMovie = getArguments().getParcelable(ARG_MOVIE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);
        unbinder = ButterKnife.bind(this, view);
        mProvider = new MoviesDBProviderUtils(getActivity());

        if (savedInstanceState != null) {
            boolean isSelected = savedInstanceState.getBoolean(FAV_BUTTON_STATE);
            mFollowButton.setSelected(isSelected);
            mFollowButton.setText(isSelected ? R.string.btn_unfollow : R.string.btn_follow);
        }

        fillUI(mMovie);

        mFollowButton.setOnClickListener(view1 -> {
            if (mFollowButton.isSelected()) {
                mFollowButton.setSelected(false);
                mFollowButton.setText(R.string.btn_follow);
                mProvider.deleteMovie(mMovie);
            } else {
                mFollowButton.setSelected(true);
                mFollowButton.setText(R.string.btn_unfollow);
                mProvider.addMovie(mMovie);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(mMovie.getTitle());

        // Show the Up button in the action bar.
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null && !getResources().getBoolean(R.bool.isTablet)) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void fillUI(Movie movie) {
        mTitleTextView.setText(movie.getTitle());
        mStateTextView.setText(movie.getReleaseDate());
        mInfoBarTextView.setText("Rated " + movie.getVoteAverage() + "   ·   By " + movie.getVoteCount() + " member");

        if (mProvider.isFav(movie)){
            mFollowButton.setSelected(true);
            mFollowButton.setText(R.string.btn_unfollow);
        }

        mDescriptionTextView.setText(movie.getOverview());
        // toggle the ExpandableTextView
        mDescriptionTextView.setAnimationDuration(200L);
        mTvShowDescriptionContainer.setOnClickListener(v -> {
            mDescriptionTextView.toggle();
            mReadMoreTextView.setText(mDescriptionTextView.isExpanded() ? R.string.read_less : R.string.read_more);
        });

        Glide.with(getActivity())
                .load("http://image.tmdb.org/t/p/w500/" + movie.getBackdropPath())
                .centerCrop()
                .placeholder(R.color.colorPrimary)
                .crossFade()
                .into(mBackdropImageView);
        Glide.with(getActivity())
                .load("http://image.tmdb.org/t/p/w500/" + movie.getPosterPath())
                .centerCrop()
                .placeholder(R.color.colorPrimary)
                .crossFade()
                .into(mPosterImageView);

        loadTrails(movie);
        loadReviews(movie);
    }

    private void loadTrails(Movie movie) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TheMovieDbAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TheMovieDbAPI dbAPI = retrofit.create(TheMovieDbAPI.class);
        Call<Trailers> trailersCall = dbAPI.getTrailers(movie.getId());
        trailersCall.enqueue(new Callback<Trailers>() {
            @Override
            public void onResponse(Call<Trailers> call, Response<Trailers> response) {
                if (response.body() != null) {
                    Log.d(TAG, "response.body() = " + response.body());
                    mTrailersRecyclerView.setLayoutManager(
                            new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                    mTrailersRecyclerView.setAdapter(new TrailersAdapter(response.body().getResults()));
                }
            }

            @Override
            public void onFailure(Call<Trailers> call, Throwable t) {
            }
        });

    }

    private void loadReviews(Movie movie) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TheMovieDbAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TheMovieDbAPI dbAPI = retrofit.create(TheMovieDbAPI.class);
        Call<Reviews> trailersCall = dbAPI.getReviews(movie.getId());
        trailersCall.enqueue(new Callback<Reviews>() {
            @Override
            public void onResponse(Call<Reviews> call, Response<Reviews> response) {
                if (response.body() != null) {
                    Log.d(TAG, "response.body() = " + response.body());
                    mReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    mReviewsRecyclerView.setAdapter(new ReviewsAdapter(response.body().getResults()));
                }
            }

            @Override
            public void onFailure(Call<Reviews> call, Throwable t) {
            }
        });

    }

    void share() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mMovie.getTitle());
        startActivity(shareIntent);
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

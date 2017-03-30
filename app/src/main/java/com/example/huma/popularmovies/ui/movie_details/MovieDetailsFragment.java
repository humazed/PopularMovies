package com.example.huma.popularmovies.ui.movie_details;

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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.example.huma.popularmovies.db.MoviesDBProviderUtils;
import com.example.huma.popularmovies.model.Movie;

import at.blogc.android.views.ExpandableTextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MovieDetailsFragment extends Fragment {
    private static final String TAG = MovieDetailsFragment.class.getSimpleName();
    private static final String ARG_MOVIE = "movie";
    public static final String FAV_BUTTON_STATE = "favButtonState";

    @BindView(R.id.backdrop_imageView) ImageView mBackdropImageView;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.collapsible_toolbar) CollapsingToolbarLayout mCollapsibleToolbar;
    @BindView(R.id.app_bar) AppBarLayout mAppBar;
    @BindView(R.id.follow_button) Button mFollowButton;
    @BindView(R.id.poster_imageView) ImageView mPosterImageView;
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


        if (savedInstanceState != null) {
            boolean isSelected = savedInstanceState.getBoolean(FAV_BUTTON_STATE);
            mFollowButton.setSelected(isSelected);
            mFollowButton.setText(isSelected ? R.string.btn_unfollow : R.string.btn_follow);
        }

        mProvider = new MoviesDBProviderUtils(getActivity());

        fillUI(mMovie);

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

        mFollowButton.setSelected(mProvider.isFav(movie));
        mFollowButton.setOnClickListener(view -> {
            if (mFollowButton.isSelected()) {
                mFollowButton.setSelected(false);
                mFollowButton.setText(R.string.btn_follow);
                mProvider.deleteMovie(movie);
            } else {
                mFollowButton.setSelected(true);
                mFollowButton.setText(R.string.btn_unfollow);
                mProvider.addMovie(movie);
            }
        });

        mDescriptionTextView.setText(movie.getOverview());
        // toggle the ExpandableTextView
        mDescriptionTextView.setAnimationDuration(200L);
        mReadMoreTextView.setOnClickListener(v -> {
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

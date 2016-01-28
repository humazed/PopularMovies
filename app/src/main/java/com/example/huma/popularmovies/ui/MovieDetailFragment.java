package com.example.huma.popularmovies.ui;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.huma.popularmovies.R;
import com.example.huma.popularmovies.model.Movie;
import com.example.huma.popularmovies.utils.UiUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A fragment representing a single movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends Fragment {
    private static final String TAG = MovieDetailFragment.class.getSimpleName();

    @Bind(R.id.movie_poster) ImageView mMoviePoster;
    @Bind(R.id.movie_title) TextView mMovieTitle;
    @Bind(R.id.movie_release_date) TextView mMovieReleaseDate;
    @Bind(R.id.movie_average_rating) TextView mMovieAverageRating;
    @Bind(R.id.movie_overview) TextView mMovieOverview;

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String KEY_MOVIE = "move";

    /**
     * The content this fragment is presenting.
     */
    private Movie mMovie;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(KEY_MOVIE)) {
            // Load the content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mMovie = getArguments().getParcelable(KEY_MOVIE);

            //set the title of appBar according to film's originalTitle
            CollapsingToolbarLayout appBarLayout =
                    (CollapsingToolbarLayout) getActivity().findViewById(R.id.toolbar_layout);
            if (mMovie != null) {
                if (appBarLayout != null) {
                    appBarLayout.setTitle(mMovie.getOriginalTitle());
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_detail, container, false);
        ButterKnife.bind(this, rootView);

        // Show movie title as text in a TextView.
        if (mMovie != null)
            loadMovie(mMovie);

        return rootView;
    }

    private void loadMovie(Movie movie) {
        mMovie = movie;


        mMovieTitle.setText(movie.getTitle());
        mMovieAverageRating.setText(getString(R.string.movie_details_rating, movie.getVoteAverage()));
        mMovieReleaseDate.setText(UiUtils.getDisplayReleaseDate(movie.getReleaseDate()));
        mMovieOverview.setText(movie.getOverview());
//        mFavoriteButton.setSelected(movie.isFavored());

        // Poster image
        Glide.with(this)
                .load("http://image.tmdb.org/t/p/w185/" + mMovie.getPosterPath())
                .centerCrop()
                .placeholder(R.color.colorPrimary)
                .crossFade()
                .into(mMoviePoster);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}

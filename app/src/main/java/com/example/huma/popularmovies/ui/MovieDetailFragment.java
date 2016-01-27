package com.example.huma.popularmovies.ui;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.huma.popularmovies.R;
import com.example.huma.popularmovies.model.Movie;

/**
 * A fragment representing a single movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends Fragment {
    private static final String TAG = MovieDetailFragment.class.getSimpleName();

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

        // Show the dummy content as text in a TextView.
        if (mMovie != null) {
            ((TextView) rootView.findViewById(R.id.movie_detail)).setText(mMovie.getOriginalTitle());
        }

        return rootView;
    }
}

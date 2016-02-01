package com.example.huma.popularmovies.ui;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.huma.popularmovies.R;
import com.example.huma.popularmovies.api.TheMovieDbAPI;
import com.example.huma.popularmovies.model.Movie;
import com.example.huma.popularmovies.model.Review;
import com.example.huma.popularmovies.model.Reviews;
import com.example.huma.popularmovies.model.Trailer;
import com.example.huma.popularmovies.model.Trailers;
import com.example.huma.popularmovies.utils.UiUtils;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

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
    public static final String KEY_TWO_PANE = "TwoPane";
    public static final String FAV_BUTTON_STATE = "favButtonState";

    @Bind(R.id.movie_poster) ImageView mMoviePoster;
    @Bind(R.id.movie_title) TextView mMovieTitle;
    @Bind(R.id.movie_release_date) TextView mMovieReleaseDate;
    @Bind(R.id.movie_average_rating) TextView mMovieAverageRating;
    @Bind(R.id.movie_overview) TextView mMovieOverview;
    @Bind(R.id.trails_list_view) ListView mTrailsListView;
    @Bind(R.id.movie_detail) RelativeLayout mMovieDetail;
    @Bind(R.id.movie_videos_container) LinearLayout mMovieVideosContainer;
    @Bind(R.id.review_list_view) ListView mReviewListView;
    @Bind(R.id.movie_reviews_container) LinearLayout mMovieReviewsContainer;
    @Bind(R.id.movie_favorite_button) ImageButton mMovieFavoriteButton;


    /**
     * The content this fragment is presenting.
     */
    private Movie mMovie;

    private boolean mTwoPane;

    boolean isSelected;


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
            mTwoPane = getArguments().getBoolean(KEY_TWO_PANE);

            //set the title of appBar according to film's originalTitle
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) getActivity().findViewById(R.id.toolbar_layout);
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

        if (savedInstanceState != null) {
            isSelected = savedInstanceState.getBoolean(FAV_BUTTON_STATE, false);
            mMovieFavoriteButton.setSelected(isSelected);
            Log.d(TAG, "onCreateView savedInstanceState" + isSelected);
        } else Log.d(TAG, "onCreateView savedInstanceState " + "null");


        //show mMovieFavoriteButton when in TwoPane as the floating button is hidden.
        if (mTwoPane) mMovieFavoriteButton.setVisibility(View.VISIBLE);
        else mMovieFavoriteButton.setVisibility(View.INVISIBLE);

        mMovieFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSelected) {
                    isSelected = true;
                    mMovieFavoriteButton.setSelected(true);
                } else {
                    isSelected = false;
                    mMovieFavoriteButton.setSelected(false);
                }
            }
        });


        // Show movie title as text in a TextView.
        if (mMovie != null) {
            loadMovie(mMovie);
            loadTrails(mMovie);
            loadReviews(mMovie);
        }


        return rootView;
    }

    public void setMovieFavored(Movie movie, boolean favored) {
        movie.setFavored(favored);
        if (favored) {
            UiUtils.addToFavorites(getActivity(), movie.getId());
        } else {
            UiUtils.removeFromFavorites(getActivity(), movie.getId());
        }
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

    private void loadTrails(Movie movie) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TheMovieDbAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TheMovieDbAPI dbAPI = retrofit.create(TheMovieDbAPI.class);
        Call<Trailers> trailersCall = dbAPI.getTrailers(movie.getId());
        trailersCall.enqueue(new Callback<Trailers>() {
            @Override//videoNameView.setText(video.getSite() + ": " + video.getName());
            public void onResponse(Response<Trailers> response, Retrofit retrofit) {
                List<Trailer> trailers = null;
                if (response.body() != null) {
                    trailers = response.body().getResults();
                }
                if (trailers != null) {
                    Log.d(TAG, "onResponse size" + trailers.size());
                    String[] s = new String[trailers.size()];
                    for (int i = 0; i < trailers.size(); i++) {
                        Trailer trailer = trailers.get(i);
                        s[i] = trailer.getSite() + ": " + trailer.getName();
                    }
                    Log.d(TAG, "onResponse() returned: loadTrails" + Arrays.toString(s));

                    UiUtils.setListViewHeightBasedOnItems(mTrailsListView);

                    if (getActivity() != null)
                        mTrailsListView.setAdapter(new ArrayAdapter<>(getActivity(),
                                R.layout.item_video, R.id.video_name, s));


                    Log.d(TAG, "onResponse " + response.body().toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {

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
            public void onResponse(Response<Reviews> response, Retrofit retrofit) {
                List<Review> reviews = null;
                if (response.body() != null) {
                    reviews = response.body().getResults();
                }
                if (reviews != null) {
                    String[] s = new String[reviews.size()];
                    for (int i = 0; i < s.length; i++) {
                        s[i] = reviews.get(i).getContent();
                    }

                    UiUtils.setListViewHeightBasedOnItems(mReviewListView);

                    if (getActivity() != null)
                        mReviewListView.setAdapter(new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_list_item_1, s));
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FAV_BUTTON_STATE, isSelected);
        Log.d(TAG, "onSaveInstanceState " + isSelected);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}

package com.example.huma.popularmovies.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.example.huma.popularmovies.db.MoviesDBProviderUtils;
import com.example.huma.popularmovies.model.Movie;
import com.example.huma.popularmovies.model.Review;
import com.example.huma.popularmovies.model.Reviews;
import com.example.huma.popularmovies.model.Trailer;
import com.example.huma.popularmovies.model.Trailers;
import com.example.huma.popularmovies.utils.UiUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

    @BindView(R.id.movie_poster) ImageView mMoviePoster;
    @BindView(R.id.movie_title) TextView mMovieTitle;
    @BindView(R.id.movie_release_date) TextView mMovieReleaseDate;
    @BindView(R.id.movie_average_rating) TextView mMovieAverageRating;
    @BindView(R.id.movie_overview) TextView mMovieOverview;
    @BindView(R.id.trails_list_view) ListView mTrailsListView;
    @BindView(R.id.movie_detail) RelativeLayout mMovieDetail;
    @BindView(R.id.movie_videos_container) LinearLayout mMovieVideosContainer;
    @BindView(R.id.review_list_view) ListView mReviewListView;
    @BindView(R.id.movie_reviews_container) LinearLayout mMovieReviewsContainer;
    @BindView(R.id.movie_favorite_button) ImageButton mMovieFavoriteButton;


    /**
     * The content this fragment is presenting.
     */
    private Movie mMovie;

    private List<Trailer> mTrailers;
    private List<Review> mReviews;

    private boolean mTwoPane;

    private MoviesDBProviderUtils mMoviesDBProviderUtils;

    private Unbinder unbinder;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMoviesDBProviderUtils = new MoviesDBProviderUtils(getActivity());

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
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        mMovieFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mMovieFavoriteButton.isSelected()) {
                    mMovieFavoriteButton.setSelected(true);
                    mMoviesDBProviderUtils.addMovie(mMovie);
                } else {
                    mMovieFavoriteButton.setSelected(false);
                    mMoviesDBProviderUtils.deleteMovie(mMovie);
                }
            }
        });

        mTrailsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                playTrailer(mTrailers.get(position));
            }
        });

        //show mMovieFavoriteButton when in TwoPane as the floating button is hidden.
        if (mTwoPane) mMovieFavoriteButton.setVisibility(View.VISIBLE);
        else mMovieFavoriteButton.setVisibility(View.INVISIBLE);

        // Show movie title as text in a TextView.
        if (mMovie != null) {
            loadMovie(mMovie);
            loadTrails(mMovie);
            loadReviews(mMovie);
        }

        return rootView;
    }

    public void playTrailer(Trailer trailer) {
        if (trailer.getSite().equals(Trailer.SITE_YOUTUBE))
            getActivity().startActivity(new Intent(
                    Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey())));
        Log.d(TAG, "playTrailer " + Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey()));
    }

    private void loadMovie(Movie movie) {
        mMovie = movie;

        mMovieTitle.setText(movie.getTitle());
        mMovieAverageRating.setText(getString(R.string.movie_details_rating, movie.getVoteAverage()));
        mMovieReleaseDate.setText(UiUtils.getDisplayReleaseDate(movie.getReleaseDate()));
        mMovieOverview.setText(movie.getOverview());
        mMovieFavoriteButton.setSelected(mMoviesDBProviderUtils.isFav(movie));

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
            @Override
            public void onResponse(Call<Trailers> call, Response<Trailers> response) {
                if (response.body() != null) {
                    mTrailers = response.body().getResults();
                }
                if (mTrailers != null) {
                    Log.d(TAG, "onResponse size" + mTrailers.size());
                    String[] s = new String[mTrailers.size()];
                    for (int i = 0; i < mTrailers.size(); i++) {
                        Trailer trailer = mTrailers.get(i);
                        s[i] = trailer.getSite() + ": " + trailer.getName();
                    }

                    UiUtils.setListViewHeightBasedOnItems(mTrailsListView);

                    if (getActivity() != null)
//                        mTrailsListView.setAdapter(new ArrayAdapter<>(getActivity(),
//                                R.layout.row_trailer, R.id.video_name, s));


                    Log.d(TAG, "onResponse " + response.body().toString());
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
                    mReviews = response.body().getResults();
                }
                if (mReviews != null) {
                    String[] s = new String[mReviews.size()];
                    for (int i = 0; i < s.length; i++) {
                        s[i] = mReviews.get(i).getContent();
                    }

                    UiUtils.setListViewHeightBasedOnItems(mReviewListView);

                    if (getActivity() != null)
                        mReviewListView.setAdapter(new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_list_item_1, s));
                }
            }

            @Override
            public void onFailure(Call<Reviews> call, Throwable t) {
            }
        });

    }

    @OnClick(R.id.share_button)
    void share() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mMovie.getTitle() +
                "\nTrailer: " + "http://www.youtube.com/watch?v=" + mTrailers.get(0).getKey());
        startActivity(shareIntent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

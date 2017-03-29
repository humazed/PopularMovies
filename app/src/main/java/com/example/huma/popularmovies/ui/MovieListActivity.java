package com.example.huma.popularmovies.ui;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.huma.popularmovies.R;
import com.example.huma.popularmovies.adapter.AutoFitRecyclerView;
import com.example.huma.popularmovies.adapter.MoviesRecyclerViewAdapter;
import com.example.huma.popularmovies.api.TheMovieDbAPI;
import com.example.huma.popularmovies.db.MoviesDBProviderUtils;
import com.example.huma.popularmovies.model.Movie;
import com.example.huma.popularmovies.model.Movies;
import com.example.huma.popularmovies.utils.UiUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * An activity representing a list of movies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MovieListActivity extends AppCompatActivity {
    private static final String TAG = MovieListActivity.class.getSimpleName();

    @BindView(R.id.sort_spinner) Spinner mSortSpinner;
    @BindView(R.id.movie_list) RecyclerView mMovieList;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet device.
     */
    public boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Setup spinner
        mSortSpinner.setAdapter(new MyAdapter(
                toolbar.getContext(),
                new String[]{
                        "most popular",
                        "highest rated",
                        "favorites",
                }));

        mSortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        update(TheMovieDbAPI.POPULARITY_DESC);
                        break;
                    case 1:
                        update(TheMovieDbAPI.RATED_DESC);
                        break;
                    case 2:
                        updateFromFav();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    private void updateFromFav() {
        MoviesDBProviderUtils provider = new MoviesDBProviderUtils(this);
        setupRecyclerView(mMovieList, provider.getMovies());
    }

    //fetch data form internet and display it.
    private void update(@TheMovieDbAPI.SortingOrder String sortBy) {
        //http://api.themoviedb.org/3/discover/movie?api_key=397b65dc1146c99252660a80e3d34c6d
        // &sort_by=popularity.desc
        Log.d(TAG, "update " + "star");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TheMovieDbAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TheMovieDbAPI dbAPI = retrofit.create(TheMovieDbAPI.class);
        Call<Movies> moviesCall = dbAPI.getMovies(sortBy);
        moviesCall.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {
                List<Movie> movies = response.body().getResults();
                setupRecyclerView(mMovieList, movies);
            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {
            }
        });
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, List<Movie> movies) {
        recyclerView.setAdapter(new MoviesRecyclerViewAdapter(this, movies));
//        recyclerView.setLayoutManager(new GridLayoutManager(this, mTwoPane ? 2 : 3));

        recyclerView.setLayoutManager(new AutoFitRecyclerView(this, (int) UiUtils.pxFromDp(this, 100f)));
    }

    ///////////////////////////////////////////////////////////////////////////
    // MyAdapter
    ///////////////////////////////////////////////////////////////////////////
    private static class MyAdapter extends ArrayAdapter<String> implements ThemedSpinnerAdapter {
        private final Helper mDropDownHelper;

        public MyAdapter(Context context, String[] objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
            mDropDownHelper = new Helper(context);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                // Inflate the drop down using the helper's LayoutInflater
                LayoutInflater inflater = mDropDownHelper.getDropDownViewInflater();
                view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            } else {
                view = convertView;
            }

            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(getItem(position));

            return view;
        }

        @Override
        public Resources.Theme getDropDownViewTheme() {
            return mDropDownHelper.getDropDownViewTheme();
        }

        @Override
        public void setDropDownViewTheme(Resources.Theme theme) {
            mDropDownHelper.setDropDownViewTheme(theme);
        }
    }
}

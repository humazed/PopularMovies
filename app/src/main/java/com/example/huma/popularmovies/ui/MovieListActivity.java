package com.example.huma.popularmovies.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.huma.popularmovies.R;
import com.example.huma.popularmovies.adapter.MoviesRecyclerViewAdapter;
import com.example.huma.popularmovies.api.TheMovieDbAPI;
import com.example.huma.popularmovies.model.Movie;
import com.example.huma.popularmovies.model.Movies;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

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

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    public boolean mTwoPane;

    private RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.movie_list);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        update();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MovieListActivity.this, "hE", Toast.LENGTH_LONG).show();
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

    private void update() {
        //http://api.themoviedb.org/3/discover/movie?api_key=397b65dc1146c99252660a80e3d34c6d
        // &sort_by=popularity.desc

        final String BASE_URL = "http://api.themoviedb.org/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TheMovieDbAPI dbAPI = retrofit.create(TheMovieDbAPI.class);
        // TODO: 1/27/2016 add spinner to change the sort
        Call<Movies> moviesCall = dbAPI.getMovies(TheMovieDbAPI.POPULARITY_DESC);
        moviesCall.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(Response<Movies> response, Retrofit retrofit) {

                List<Movie> movies = response.body().getMovies();
                setupRecyclerView(mRecyclerView, movies);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, List<Movie> movies) {
        recyclerView.setAdapter(new MoviesRecyclerViewAdapter(this, movies));
        recyclerView.setLayoutManager(new GridLayoutManager(this, mTwoPane ? 2 : 3));
    }

}

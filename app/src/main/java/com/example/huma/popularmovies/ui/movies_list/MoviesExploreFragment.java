package com.example.huma.popularmovies.ui.movies_list;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.huma.popularmovies.R;
import com.example.huma.popularmovies.adapter.MoviesAdapter;
import com.example.huma.popularmovies.api.TheMovieDbAPI;
import com.example.huma.popularmovies.model.Movie;
import com.example.huma.popularmovies.model.Movies;
import com.example.huma.popularmovies.ui.movie_details.MovieDetailsActivity;
import com.example.huma.popularmovies.ui.movie_details.MovieDetailsFragment;

import java.util.List;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesExploreFragment extends Fragment {
    private static final String TAG = MoviesExploreFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "param1";

    @BindView(R.id.explore_recyclerView) RecyclerView mExploreRecyclerView;
    @BindBool(R.bool.isTablet) boolean isTablet;
    Unbinder unbinder;

    private String mParam1;

    private OnFragmentInteractionListener mListener;
    private SharedPreferences mPreferences;
    private SharedPreferences.OnSharedPreferenceChangeListener mOnSharedPreferenceChangeListener;

    public MoviesExploreFragment() { /*Required empty public constructor*/ }

    public static MoviesExploreFragment newInstance(String param1) {
        MoviesExploreFragment fragment = new MoviesExploreFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies_explore, container, false);
        unbinder = ButterKnife.bind(this, view);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mOnSharedPreferenceChangeListener = (sharedPreferences, key) -> {
            Log.d(TAG, "key = " + key);
            if (key.equals(getString(R.string.key_sort_by))) {
                @TheMovieDbAPI.SortingOrder
                String sortBy = mPreferences.getString(getString(R.string.key_sort_by), TheMovieDbAPI.POPULAR);
                Log.d(TAG, "registerOnSharedPreferenceChangeListener " + "val: " + sortBy);

                update(sortBy);
            }
        };
        mPreferences.registerOnSharedPreferenceChangeListener(mOnSharedPreferenceChangeListener);

        @TheMovieDbAPI.SortingOrder
        String sortBy = mPreferences.getString(getString(R.string.key_sort_by), TheMovieDbAPI.POPULAR);
        update(sortBy);

        return view;
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
                setupRecyclerView(movies);
            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private void setupRecyclerView(List<Movie> movies) {
        MoviesAdapter adapter = new MoviesAdapter(movies);

        adapter.setOnItemClickListener((adapter1, view, position) -> {
            Movie movie = movies.get(position);
            if (isTablet) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container,
                                MovieDetailsFragment.newInstance(movie))
                        .commit();
            } else {
                Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
                intent.putExtra(MovieDetailsFragment.KEY_MOVIE, movie);
                getActivity().startActivity(intent);
            }
        });

        mExploreRecyclerView.setAdapter(adapter);
        mExploreRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
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
        mPreferences.unregisterOnSharedPreferenceChangeListener(mOnSharedPreferenceChangeListener);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

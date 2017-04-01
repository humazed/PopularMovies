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
import android.widget.ProgressBar;
import android.widget.Toast;

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

import static com.chad.library.adapter.base.BaseQuickAdapter.SCALEIN;
import static com.example.huma.popularmovies.utils.NetworkUtil.getCaCheOkHttpClient;

public class MoviesExploreFragment extends Fragment {
    private static final String TAG = MoviesExploreFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "param1";

    @BindView(R.id.explore_recyclerView) RecyclerView mExploreRecyclerView;
    @BindBool(R.bool.isTablet) boolean isTablet;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;
    Unbinder unbinder;

    private String mParam1;

    private OnFragmentInteractionListener mListener;
    private SharedPreferences mPreferences;
    private SharedPreferences.OnSharedPreferenceChangeListener mOnSharedPreferenceChangeListener;
    private TheMovieDbAPI mAPI;
    private int mPage;
    private
    String mSortBy;

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

        mPage = 1;

        mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mOnSharedPreferenceChangeListener = (sharedPreferences, key) -> {
            Log.d(TAG, "key = " + key);
            if (key.equals(getString(R.string.key_sort_by))) {
                mSortBy = mPreferences.getString(getString(R.string.key_sort_by), TheMovieDbAPI.POPULAR);
                Log.d(TAG, "registerOnSharedPreferenceChangeListener " + "val: " + mSortBy);

                update(mSortBy, mPage);
            }
        };
        mPreferences.registerOnSharedPreferenceChangeListener(mOnSharedPreferenceChangeListener);

        mSortBy = mPreferences.getString(getString(R.string.key_sort_by), TheMovieDbAPI.POPULAR);
        update(mSortBy, mPage);

        return view;
    }

    //fetch data form internet and display it.
    private void update(String sortBy, int page) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TheMovieDbAPI.BASE_URL)
                .client(getCaCheOkHttpClient(getActivity()))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mAPI = retrofit.create(TheMovieDbAPI.class);
        mAPI.getMovies(sortBy, page).enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {
                mProgressBar.setVisibility(View.GONE);
                List<Movie> movies = response.body().getResults();
                setupRecyclerView(movies);
            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                Log.e(TAG, "onFailure: ", t);
                Toast.makeText(getActivity(), "Loading Failed", Toast.LENGTH_LONG).show();
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

        //minimum number of unseen items before start loading more
        adapter.setAutoLoadMoreSize(20);
        adapter.setOnLoadMoreListener(() -> {
            Log.d(TAG, "setupRecyclerView " + "setOnLoadMoreListener");

            mAPI.getMovies(mSortBy, ++mPage).enqueue(new Callback<Movies>() {
                @Override
                public void onResponse(Call<Movies> call, Response<Movies> response) {
                    if (response.body() != null) {
                        List<Movie> movies = response.body().getResults();
                        adapter.addData(movies);
                        adapter.loadMoreComplete();
                    } else adapter.loadMoreFail();
                }

                @Override
                public void onFailure(Call<Movies> call, Throwable t) {
                    Log.e(TAG, "onFailure: ", t);
                    adapter.loadMoreFail();
                }
            });
        }, mExploreRecyclerView);

        adapter.openLoadAnimation(SCALEIN);

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

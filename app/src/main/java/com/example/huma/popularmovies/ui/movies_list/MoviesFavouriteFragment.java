package com.example.huma.popularmovies.ui.movies_list;

import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.huma.popularmovies.R;
import com.example.huma.popularmovies.adapter.MoviesAdapter;
import com.example.huma.popularmovies.db.MoviesDBProviderUtils;
import com.example.huma.popularmovies.model.Movie;
import com.example.huma.popularmovies.ui.movie_details.MovieDetailsActivity;
import com.example.huma.popularmovies.ui.movie_details.MovieDetailsFragment;

import java.util.List;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.huma.popularmovies.db.MoveContract.MovieEntry.CONTENT_URI;

public class MoviesFavouriteFragment extends Fragment {
    private static final String TAG = MoviesFavouriteFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "param1";

    @BindView(R.id.favourite_recyclerView) RecyclerView mFavouriteRecyclerView;
    @BindBool(R.bool.isTablet) boolean isTablet;
    Unbinder unbinder;

    private String mParam1;

    private OnFragmentInteractionListener mListener;
    private MoviesDBProviderUtils mProvider;
    private ContentObserver mObserver;

    public MoviesFavouriteFragment() { /*Required empty public constructor*/ }

    public static MoviesFavouriteFragment newInstance(String param1) {
        MoviesFavouriteFragment fragment = new MoviesFavouriteFragment();
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
        View view = inflater.inflate(R.layout.fragment_movies_favourite, container, false);
        unbinder = ButterKnife.bind(this, view);

        mProvider = new MoviesDBProviderUtils(getActivity());

        setupRecyclerView(mProvider.getFavMovies());

        mObserver = new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                setupRecyclerView(mProvider.getFavMovies());
            }
        };


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getContentResolver().registerContentObserver(CONTENT_URI, true, mObserver);
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

        mFavouriteRecyclerView.setAdapter(adapter);
        mFavouriteRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
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
        getActivity().getContentResolver().unregisterContentObserver(mObserver);
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

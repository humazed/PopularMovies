package com.example.huma.popularmovies.api;

import android.support.annotation.StringDef;

import com.example.huma.popularmovies.model.Movies;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

public interface TheMovieDbAPI {
//    String  SORT_BY_PARAM = "sort_by",
//            API_KEY_PARAM = "api_key";


    String POPULARITY_ASC = "popularity.asc",
            POPULARITY_DESC = "popularity.desc",
            RATED_ASC = "vote_count.asc",
            RATED_DESC = "vote_count.desc";

    @GET("3/discover/movie?api_key=397b65dc1146c99252660a80e3d34c6d")
    Call<Movies> getMovies(
            @Query("sort_by") @SortingOrder String sortBy
    );


    @StringDef({
            POPULARITY_DESC, POPULARITY_ASC, RATED_ASC, RATED_DESC
    })
    @interface SortingOrder {}
}

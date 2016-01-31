package com.example.huma.popularmovies.api;

import android.support.annotation.StringDef;

import com.example.huma.popularmovies.model.Movies;
import com.example.huma.popularmovies.model.Reviews;
import com.example.huma.popularmovies.model.Trailers;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface TheMovieDbAPI {
    String API_KEY = "397b65dc1146c99252660a80e3d34c6d";

//    String  SORT_BY_PARAM = "sort_by",
//            API_KEY_PARAM = "api_key";


    String POPULARITY_ASC = "popularity.asc",
            POPULARITY_DESC = "popularity.desc",
            RATED_ASC = "vote_count.asc",
            RATED_DESC = "vote_count.desc";
    String BASE_URL = "http://api.themoviedb.org/3/";

    //http://api.themoviedb.org/3/discover/movie?api_key=397b65dc1146c99252660a80e3d34c6d&sort_by=popularity.desc
    @GET("discover/movie?api_key=" + API_KEY)
    Call<Movies> getMovies(
            @Query("sort_by") @SortingOrder String sortBy
    );

    //http://api.themoviedb.org/3/movie/281957/videos?api_key=397b65dc1146c99252660a80e3d34c6d
    @GET("movie/{id}/videos?api_key=" + API_KEY)
    Call<Trailers> getTrailers(
            @Path("id") int id
    );

    //                                  140607
    //http://api.themoviedb.org/3/movie/281957/reviews?api_key=397b65dc1146c99252660a80e3d34c6d
    @GET("movie/{id}/reviews?api_key=" + API_KEY)
    Call<Reviews> getReviews(
            @Path("id") int id
    );


    @StringDef({
            POPULARITY_DESC, POPULARITY_ASC, RATED_ASC, RATED_DESC
    })
    @interface SortingOrder {}
}

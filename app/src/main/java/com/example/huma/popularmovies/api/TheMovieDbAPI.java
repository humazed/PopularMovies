package com.example.huma.popularmovies.api;

import android.support.annotation.StringDef;

import com.example.huma.popularmovies.model.Movies;
import com.example.huma.popularmovies.model.Reviews;
import com.example.huma.popularmovies.model.Trailers;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TheMovieDbAPI {
    String API_KEY = "397b65dc1146c99252660a80e3d34c6d";
    String BASE_URL = "http://api.themoviedb.org/3/";

    String TOP_RATED = "top_rated", POPULAR = "popular";

    //http://api.themoviedb.org/3/movie/top_rated?api_key=397b65dc1146c99252660a80e3d34c6d
    @GET("movie/{sort_by}?api_key=" + API_KEY)
    Call<Movies> getMovies(@Path("sort_by") @SortingOrder String sortBy);

    //http://api.themoviedb.org/3/movie/281957/videos?api_key=397b65dc1146c99252660a80e3d34c6d
    @GET("movie/{id}/videos?api_key=" + API_KEY)
    Call<Trailers> getTrailers(@Path("id") int id);

    //                                  140607
    //http://api.themoviedb.org/3/movie/281957/reviews?api_key=397b65dc1146c99252660a80e3d34c6d
    @GET("movie/{id}/reviews?api_key=" + API_KEY)
    Call<Reviews> getReviews(@Path("id") int id);


    @StringDef({
            TOP_RATED, POPULAR
    })
    @interface SortingOrder {}
}

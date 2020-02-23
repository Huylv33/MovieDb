package com.project.mobile.movie_db_training.network;

import com.project.mobile.movie_db_training.data.model.CreditResponse;
import com.project.mobile.movie_db_training.data.model.CreditsResponse;
import com.project.mobile.movie_db_training.data.model.GenresResponse;
import com.project.mobile.movie_db_training.data.model.Movie;
import com.project.mobile.movie_db_training.data.model.MovieResponse;
import com.project.mobile.movie_db_training.data.model.Person;
import com.project.mobile.movie_db_training.data.model.ReviewResponse;
import com.project.mobile.movie_db_training.data.model.VideoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TMDbService {
    @GET("genre/movie/list?language=en-US")
    Call<GenresResponse> getGenreList(@Query("api_key") String key);

    @GET("movie/{listType}?language=en-US")
    Call<MovieResponse> getMovieList(@Path("listType") String listType,
                                     @Query("api_key") String key,
                                     @Query("page") int page);

    @GET("movie/{movie_id}/videos?language=en-US")
    Call<VideoResponse> getVideos(@Path("movie_id") String id, @Query("api_key") String key);

    @GET("movie/{movie_id}/reviews")
    Call<ReviewResponse> getReviews(@Path("movie_id") String id,
                                    @Query("api_key") String key,
                                    @Query("page") int page);

    @GET("movie/{movie_id}/credits")
    Call<CreditsResponse> getCredits(@Path("movie_id") String id,
                                     @Query("api_key") String key);

    @GET("discover/movie")
    Call<MovieResponse> getMoviesByGenre(@Query("api_key") String key,
                                         @Query("with_genres") String genreId,
                                         @Query("page") int page);

    @GET("movie/latest?language=en-US")
    Call<Movie> getLatestMovie(@Query("api_key") String key);

    @GET("search/movie?language=en-US&page=1")
    Call<MovieResponse> searchMovies(@Query("query") String searchQuery, @Query("api_key") String key);

    @GET("person/{person_id}?language=en-US&append_to_response=movie_credits")
    Call<Person> getPersonDetail(@Path("person_id") String id, @Query("api_key") String key);

    @GET("person/{person_id}/movie_credits")
    Call<CreditResponse> getMovieCredits(@Path("person_id") String id, @Query("api_key") String key);

    @GET("movie/{movie_id}")
    Call<Movie> getMovieDetail(@Path("movie_id") String id, @Query("api_key") String key);
}

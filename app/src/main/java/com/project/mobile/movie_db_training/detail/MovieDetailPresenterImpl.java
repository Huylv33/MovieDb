package com.project.mobile.movie_db_training.detail;

import android.content.Context;

import androidx.annotation.NonNull;

import com.project.mobile.movie_db_training.BuildConfig;
import com.project.mobile.movie_db_training.data.local.MovieDatabase;
import com.project.mobile.movie_db_training.data.model.CreditsResponse;
import com.project.mobile.movie_db_training.data.model.FavoriteEntity;
import com.project.mobile.movie_db_training.data.model.Movie;
import com.project.mobile.movie_db_training.data.model.Review;
import com.project.mobile.movie_db_training.data.model.ReviewResponse;
import com.project.mobile.movie_db_training.data.model.Video;
import com.project.mobile.movie_db_training.data.model.VideoResponse;
import com.project.mobile.movie_db_training.network.NetworkModule;
import com.project.mobile.movie_db_training.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailPresenterImpl implements MovieDetailContract.Presenter {
    private MovieDetailContract.View mView;
    private boolean mIsLoading;
    private int mReviewPage = 1;
    private int mTotalReviewPage;
    private MovieDatabase mMovieDatabase;
    private boolean mIsFavorite;

    public MovieDetailPresenterImpl(Context context) {
        mMovieDatabase = MovieDatabase.getInstance(context);
    }

    @Override
    public void fetchLatestMovie() {
        NetworkModule.getTMDbService().getLatestMovie(BuildConfig.TMDB_API_KEY)
                .enqueue(new Callback<Movie>() {
                    @Override
                    public void onResponse(Call<Movie> call, Response<Movie> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            onFetchLatestMovieSuccess(response.body());
                        } else {
                            onFetchFail(Constants.RESPONSE_ERROR);
                        }
                    }

                    @Override
                    public void onFailure(Call<Movie> call, Throwable t) {
                        onFetchFail(t.getMessage());
                    }
                });
    }

    private void onFetchLatestMovieSuccess(@NonNull Movie movie) {
        mView.showInfo(movie);
        showFavorite(movie.getId());
        fetchVideos(movie.getId());
        fetchReviews(movie.getId());
    }

    @Override
    public void fetchVideos(String movieId) {
        NetworkModule.getTMDbService().getVideos(movieId, BuildConfig.TMDB_API_KEY)
                .enqueue(new Callback<VideoResponse>() {
                    @Override
                    public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            onFetchVideosSuccess(response.body().getVideos());
                        } else {
                            onFetchFail(Constants.RESPONSE_ERROR);
                        }
                    }

                    @Override
                    public void onFailure(Call<VideoResponse> call, Throwable t) {
                        onFetchFail(t.getMessage());
                    }
                });
    }

    private void onFetchVideosSuccess(@NonNull List<Video> videos) {
        mView.showVideos(videos);
    }

    @Override
    public void fetchReviews(String movieId) {
        if (mIsLoading) return;
        mView.showLoading(Constants.LOADING_START);
        mIsLoading = false;
        NetworkModule.getTMDbService().getReviews(movieId, BuildConfig.TMDB_API_KEY, 1)
                .enqueue(new Callback<ReviewResponse>() {
                    @Override
                    public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            onFetchReviewsSuccess(response.body());
                        } else {
                            onFetchFail(Constants.RESPONSE_ERROR);
                        }
                    }

                    @Override
                    public void onFailure(Call<ReviewResponse> call, Throwable t) {
                        onFetchFail(t.getMessage());
                    }
                });
    }

    @Override
    public void loadMoreReviews(String movieId) {
        if (!mIsLoading && mReviewPage < mTotalReviewPage) {
            mReviewPage++;
            fetchReviews(movieId);
        }
    }

    private void onFetchReviewsSuccess(@NonNull ReviewResponse reviewResponse) {
        List<Review> reviews = reviewResponse.getReviews();
        mView.showReviews(reviews);
        mTotalReviewPage = reviewResponse.getTotalPages();
    }

    private void onFetchFail(String message) {
        mView.showLoading(message);
    }

    private boolean isFavorite(String movieId) {
        return mMovieDatabase.favoritesDao().getById(movieId) != null;
    }

    @Override
    public void onFabFavoriteClick(Movie movie) {
        FavoriteEntity favoriteEntity = new FavoriteEntity(movie);
        if (!isFavorite(movie.getId())) {
            mMovieDatabase.favoritesDao().insert(favoriteEntity);
            mView.showFavoriteButton(Constants.FAVORITE_ACTIVE);
        } else {
            mMovieDatabase.favoritesDao().deleteById(favoriteEntity.getId());
            mView.showFavoriteButton(Constants.FAVORITE_NON_ACTIVE);
        }
    }

    @Override
    public void showFavorite(String movieId) {
        if (isFavorite(movieId)) {
            mView.showFavoriteButton(Constants.FAVORITE_ACTIVE);
        } else {
            mView.showFavoriteButton(Constants.FAVORITE_NON_ACTIVE);
        }
    }

    @Override
    public void fetchCast(String movieId) {
        NetworkModule.getTMDbService().getCredits(movieId, BuildConfig.TMDB_API_KEY)
                .enqueue(new Callback<CreditsResponse>() {
                    @Override
                    public void onResponse(Call<CreditsResponse> call, Response<CreditsResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            mView.showCast(response.body().getCast());
                        }
                    }

                    @Override
                    public void onFailure(Call<CreditsResponse> call, Throwable t) {

                    }
                });
    }

    @Override
    public void setView(MovieDetailContract.View view) {
        mView = view;
    }

    @Override
    public void destroy() {
        mView = null;
    }
}

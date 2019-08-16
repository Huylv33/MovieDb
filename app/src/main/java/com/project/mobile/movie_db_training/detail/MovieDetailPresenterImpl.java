package com.project.mobile.movie_db_training.detail;

import androidx.annotation.NonNull;

import com.project.mobile.movie_db_training.BuildConfig;
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

    @Override
    public void fetchVideos(String movieId) {
        NetworkModule.getTMDbService().getVideos(movieId, BuildConfig.TMDB_API_KEY)
                .enqueue(new Callback<VideoResponse>() {
                    @Override
                    public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            onFetchVideosSuccess(response.body().getVideos());
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

    @Override
    public void setView(MovieDetailContract.View view) {
        mView = view;
    }

    @Override
    public void destroy() {
        mView = null;
    }
}

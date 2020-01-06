package com.project.mobile.movie_db_training.main;

import android.util.Log;

import androidx.annotation.NonNull;

import com.project.mobile.movie_db_training.BuildConfig;
import com.project.mobile.movie_db_training.data.model.MovieResponse;
import com.project.mobile.movie_db_training.network.NetworkModule;
import com.project.mobile.movie_db_training.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPresenterImpl implements MainContract.Presenter {
    private String TAG = MainPresenterImpl.class.getSimpleName();
    private boolean mIsLoading = false;
    private int mPage = 1;
    private MainContract.View mView;

    @Override
    public void fetchMovies(String listType) {
        if (mIsLoading) return;
        mIsLoading = true;
        NetworkModule.getTMDbService()
                .getMovieList(listType, BuildConfig.TMDB_API_KEY, mPage)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            onFetchSuccess(response.body(), listType);
                        } else {
                            onFetchFail(Constants.RESPONSE_ERROR);
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {
                        onFetchFail(t.getMessage());
                    }
                });
    }

    @Override
    public void setView(MainContract.View view) {
        mView = view;
    }

    @Override
    public void destroy() {
        mView = null;
    }

    private void onFetchSuccess(@NonNull MovieResponse movieResponse, String listType) {
        mIsLoading = false;
        mView.showMovies(movieResponse.getMovies(), listType);
    }

    private void onFetchFail(String message) {
        Log.d(TAG,"FAIL");
    }
}

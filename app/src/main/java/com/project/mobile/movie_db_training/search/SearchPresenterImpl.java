package com.project.mobile.movie_db_training.search;

import android.util.Log;

import com.project.mobile.movie_db_training.BuildConfig;
import com.project.mobile.movie_db_training.data.model.Movie;
import com.project.mobile.movie_db_training.data.model.MovieResponse;
import com.project.mobile.movie_db_training.network.NetworkModule;
import com.project.mobile.movie_db_training.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchPresenterImpl implements SearchContract.Presenter {
    private SearchContract.View mView;
    private List<Movie> mMovies = new ArrayList<>();

    @Override
    public void searchMovies(String query) {
        NetworkModule.getTMDbService()
                .searchMovies(query, BuildConfig.TMDB_API_KEY)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            onFetchSuccess(response.body());
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
    public void loadMore() {
        //Todo
    }

    @Override
    public void setView(SearchContract.View view) {
        this.mView = view;
    }

    @Override
    public void destroy() {
        mView = null;
    }

    private void onFetchSuccess(MovieResponse movieResponse) {
        mMovies = movieResponse.getMovies();
        mView.showMovies(mMovies);
    }

    private void onFetchFail(String message) {

    }
}

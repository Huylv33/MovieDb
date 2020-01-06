package com.project.mobile.movie_db_training.main;

import com.project.mobile.movie_db_training.data.model.Movie;

import java.util.List;

public interface MainContract {
    interface View {
        void showMovies(List<Movie> movies, String listType);

        void showLoading(String message);
    }

    interface Presenter {

        void fetchMovies(String option);

        void setView(MainContract.View view);

        void destroy();
    }
}

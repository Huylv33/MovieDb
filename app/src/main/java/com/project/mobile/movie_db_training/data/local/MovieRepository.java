package com.project.mobile.movie_db_training.data.local;

import android.content.Context;

import com.project.mobile.movie_db_training.data.model.FavoriteEntity;

import java.util.List;

public class MovieRepository {
    private MovieDatabase mMovieDatabase;

    public MovieRepository(Context context) {
        mMovieDatabase = MovieDatabase.getInstance(context);
    }

    public void insertMovie(FavoriteEntity favoriteEntity, Callback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mMovieDatabase.favoritesDao().insert(favoriteEntity);
                callback.update();
            }
        }).start();
    }

    public void deleteMovie(String id, Callback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mMovieDatabase.favoritesDao().deleteById(id);
                callback.update();
            }
        }).start();
    }

    public void getMovieById(String id, FavoriteCallBack callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FavoriteEntity favoriteEntity = mMovieDatabase.favoritesDao().getById(id);
                if (favoriteEntity != null) {
                    callback.setFavorite(true);
                } else {
                    callback.setFavorite(false);
                }
            }
        }).start();
    }
    public void getAllMovie(GetAllMovieCallBack callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<FavoriteEntity> favoriteEntities = mMovieDatabase.favoritesDao().getAll();
                callback.update(favoriteEntities);
            }
        }).start();
    }
    public interface Callback {
        void update();
    }

    public interface FavoriteCallBack {
        void setFavorite(boolean favorite);
    }
    public interface GetAllMovieCallBack {
        void update(List<FavoriteEntity> favoriteEntities);
    }
}

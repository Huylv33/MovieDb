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
                new MainThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.update();
                    }
                });
            }
        }).start();
    }

    public void deleteMovie(String id, Callback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mMovieDatabase.favoritesDao().deleteById(id);
                new MainThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.update();
                    }
                });
            }
        }).start();
    }

    public void getMovieById(String id, Callback2<Boolean> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FavoriteEntity favoriteEntity = mMovieDatabase.favoritesDao().getById(id);
                new MainThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.update(favoriteEntity != null);
                    }
                });
            }
        }).start();
    }

    public void getAllMovie(Callback2<List<FavoriteEntity>> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<FavoriteEntity> favoriteEntities = mMovieDatabase.favoritesDao().getAll();
                new MainThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.update(favoriteEntities);
                    }
                });
            }
        }).start();
    }

    public interface Callback {
        void update();
    }

    public interface Callback2<T> {
        void update(T obj);
    }
}

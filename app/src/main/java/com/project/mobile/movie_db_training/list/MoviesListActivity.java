package com.project.mobile.movie_db_training.list;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.project.mobile.movie_db_training.R;
import com.project.mobile.movie_db_training.data.model.Genre;
import com.project.mobile.movie_db_training.data.model.Movie;
import com.project.mobile.movie_db_training.detail.MovieDetailActivity;
import com.project.mobile.movie_db_training.utils.Constants;
import com.project.mobile.movie_db_training.utils.GettingTitle;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesListActivity extends AppCompatActivity implements MoviesListFragment.Callback {

    @BindView(R.id.toolbar)
    Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent == null) return;
        Bundle extras = intent.getExtras();
        MoviesListFragment moviesListFragment;
        if (extras != null) {
            if (extras.containsKey(Constants.LIST_TYPE)) {
                String listType = extras.getString(Constants.LIST_TYPE);
                initToolbar(GettingTitle.getTitleFromListType(listType));
                moviesListFragment = MoviesListFragment.newInstance(listType);
            } else {
                Genre genre = extras.getParcelable(Constants.GENRE);
                initToolbar(genre.getName());
                moviesListFragment = MoviesListFragment.newInstance(genre);
            }
        } else {
            // hien thi local
            initToolbar(Constants.FAVORITE_TITLE);
            moviesListFragment = new MoviesListFragment();
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_movies_list, moviesListFragment)
                .commit();
    }

    private void initToolbar(String title) {
        setSupportActionBar(mToolBar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle(title);
        }
        mToolBar.setNavigationOnClickListener(view -> {
            finish();
        });
    }

    @Override
    public void onMovieClick(Movie movie) {
        startMovieDetail(movie);
    }

    public void startMovieDetail(Movie movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        Bundle extras = new Bundle();
        extras.putParcelable(Constants.MOVIE_KEY, movie);
        intent.putExtras(extras);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list, menu);
        return super.onCreateOptionsMenu(menu);
    }
}

package com.project.mobile.movie_db_training.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.project.mobile.movie_db_training.R;
import com.project.mobile.movie_db_training.about.AboutActivity;
import com.project.mobile.movie_db_training.data.model.Movie;
import com.project.mobile.movie_db_training.detail.MovieDetailActivity;
import com.project.mobile.movie_db_training.genre.GenresListActivity;
import com.project.mobile.movie_db_training.list.MoviesListFragment;
import com.project.mobile.movie_db_training.search.SearchActivity;
import com.project.mobile.movie_db_training.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MoviesListFragment.Callback {
    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        ButterKnife.bind(this);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_now_playing, MoviesFragment.newInstance("now_playing")).commit();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_up_coming, MoviesFragment.newInstance("upcoming")).commit();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_popular, MoviesFragment.newInstance("popular")).commit();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_top_rated, MoviesFragment.newInstance("top_rated")).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.nav_home:
                mDrawerLayout.closeDrawers();
                break;
            case R.id.nav_discover:
                startActivity(new Intent(this, GenresListActivity.class));
                break;
            case R.id.nav_share:
                shareAppInfo();
                break;
            case R.id.nav_feedback:
                sendFeedback();
                break;
            case R.id.nav_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }
        return false;
    }

    private void initView() {
        DrawerLayout mDrawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
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
    private void shareAppInfo() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "AppUrl.com");
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }
    private void sendFeedback() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"huyit12a5@gmail.com"});
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

}

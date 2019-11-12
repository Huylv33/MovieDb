package com.project.mobile.movie_db_training.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.navigation.NavigationView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.project.mobile.movie_db_training.R;
import com.project.mobile.movie_db_training.data.model.Movie;
import com.project.mobile.movie_db_training.detail.MovieDetailActivity;
import com.project.mobile.movie_db_training.genre.GenresListActivity;
import com.project.mobile.movie_db_training.list.MoviesListActivity;
import com.project.mobile.movie_db_training.list.MoviesListFragment;
import com.project.mobile.movie_db_training.search.SearchActivity;
import com.project.mobile.movie_db_training.utils.Constants;
import com.takusemba.multisnaprecyclerview.MultiSnapRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainContract.View,
        MoviesListFragment.Callback, View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;
    @BindView(R.id.rv_now_playing)
    MultiSnapRecyclerView mNowPlayingRv;
    @BindView(R.id.rv_upcoming)
    MultiSnapRecyclerView mUpComingRv;
    @BindView(R.id.all_now_playing)
    TextView mSeeAllNowPlaying;
    @BindView(R.id.all_upcoming)
    TextView mSeeAllUpcoming;
    private MainContract.Presenter mPresenter;
    private MovieAdapter mNowPlayingAdapter;
    private MovieAdapter mUpcomingAdapter;
    private List<Movie> mNowPlayingMovies = new ArrayList<>();
    private List<Movie> mUpcomingMovies = new ArrayList<>();
    private List<Movie> mPopularMovies = new ArrayList<>();
    private List<Movie> mTopRatedMovies     = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setupYoutubeView();
        ButterKnife.bind(this);
        setListener();
        mNowPlayingAdapter = new MovieAdapter(mNowPlayingMovies, this);
        mUpcomingAdapter = new MovieAdapter(mUpcomingMovies, this);
        setupRecyclerView(mNowPlayingRv, mNowPlayingAdapter, mNowPlayingMovies);
        setupRecyclerView(mUpComingRv, mUpcomingAdapter, mUpcomingMovies);
        mPresenter = new MainPresenterImpl();
        mPresenter.setView(this);
        mPresenter.fetchMovies(Constants.NOW_PLAYING);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search :
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
            case R.id.nav_discover:
                startActivity(new Intent(this, GenresListActivity.class));
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

    private void setupYoutubeView() {
        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);
    }

    private void setListener() {
        mSeeAllNowPlaying.setOnClickListener(this);
        mSeeAllUpcoming.setOnClickListener(this);
    }

    private void setupRecyclerView(MultiSnapRecyclerView view, MovieAdapter adapter, List<Movie> movies) {
        view.setAdapter(adapter);
        view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public void showMovies(List<Movie> movies, String listType) {
        switch (listType) {
            case Constants.UPCOMING:
                this.mUpcomingMovies.addAll(movies);
                mUpcomingAdapter.notifyDataSetChanged();
                break;
            case Constants.NOW_PLAYING:
                this.mNowPlayingMovies.addAll(movies);
                mNowPlayingAdapter.notifyDataSetChanged();
                mPresenter.fetchMovies(Constants.UPCOMING);
                break;
        }
    }

    @Override
    public void showLoading(String message) {

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

    public void startMovieList(String listType) {
        Intent intent = new Intent(this, MoviesListActivity.class);
        intent.putExtra(Constants.LIST_TYPE, listType);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.all_now_playing:
                startMovieList(Constants.NOW_PLAYING);
                break;
            case R.id.all_upcoming:
                startMovieList(Constants.UPCOMING);
                break;
        }
    }

}

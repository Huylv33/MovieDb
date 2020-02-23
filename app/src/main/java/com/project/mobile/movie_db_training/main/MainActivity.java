package com.project.mobile.movie_db_training.main;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.project.mobile.movie_db_training.R;
import com.project.mobile.movie_db_training.about.AboutActivity;
import com.project.mobile.movie_db_training.data.model.Movie;
import com.project.mobile.movie_db_training.detail.MovieDetailActivity;
import com.project.mobile.movie_db_training.genre.GenresListActivity;
import com.project.mobile.movie_db_training.list.MoviesListActivity;
import com.project.mobile.movie_db_training.list.MoviesListFragment;
import com.project.mobile.movie_db_training.search.SearchActivity;
import com.project.mobile.movie_db_training.setting.SettingsActivity;
import com.project.mobile.movie_db_training.utils.Constants;
import com.project.mobile.movie_db_training.utils.NetworkChecking;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MoviesListFragment.Callback {
    private static final String TAG = MainActivity.class.getSimpleName();
    //    @BindView(R.id.nav_view)
    NavigationView mNavigationView;
    //    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    ConnectivityManager connectivityManager;
    ConnectivityManager.NetworkCallback mNetworkCallback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(@NonNull Network network) {
            super.onAvailable(network);
        }

        @Override
        public void onLost(@NonNull Network network) {
            super.onLost(network);
            showNetworkAlert();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
        if (savedInstanceState != null) return;
        loadMovies();
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        connectivityManager.registerNetworkCallback(builder.build(), mNetworkCallback);
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
            case R.id.refresh:
                loadMovies();
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
            case R.id.nav_favorite:
                startActivity(new Intent(this, MoviesListActivity.class));
                break;
            case R.id.nav_setting:
                startActivity(new Intent(this, SettingsActivity.class));
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
        sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"huyit12a5@gmail.com"});
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    private void loadMovies() {
        if (!NetworkChecking.checkInternetAvailability(this)) {
            showNetworkAlert();
            return;
        }

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction
//                .replace(R.id.fragment_now_playing, MoviesFragment.newInstance("now_playing"));
//        fragmentTransaction
//                .replace(R.id.fragment_up_coming, MoviesFragment.newInstance("upcoming"));
//        fragmentTransaction
//                .replace(R.id.fragment_popular, MoviesFragment.newInstance("popular"));
//        fragmentTransaction
//                .replace(R.id.fragment_top_rated, MoviesFragment.newInstance("top_rated"));
//        fragmentTransaction.commit();
        addMoviesFragment(MoviesFragment.newInstance("now_playing"), R.id.fragment_now_playing);
        addMoviesFragment(MoviesFragment.newInstance("upcoming"), R.id.fragment_up_coming);
        addMoviesFragment(MoviesFragment.newInstance("popular"), R.id.fragment_popular);
        addMoviesFragment(MoviesFragment.newInstance("top_rated"), R.id.fragment_top_rated);
    }

    private void addMoviesFragment(MoviesFragment moviesFragment, int id) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction
                .replace(id, moviesFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void showNetworkAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.network_title)
                .setMessage(R.string.network_message);
        builder.setPositiveButton(R.string.retry, (dialog, id) -> {
            dialog.dismiss();
            loadMovies();
        });
        builder.setNegativeButton(R.string.cancel, (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
        connectivityManager.unregisterNetworkCallback(mNetworkCallback);
    }
}

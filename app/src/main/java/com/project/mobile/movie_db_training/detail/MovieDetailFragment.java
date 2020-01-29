package com.project.mobile.movie_db_training.detail;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.project.mobile.movie_db_training.R;
import com.project.mobile.movie_db_training.data.model.Cast;
import com.project.mobile.movie_db_training.data.model.Movie;
import com.project.mobile.movie_db_training.data.model.Review;
import com.project.mobile.movie_db_training.data.model.Video;
import com.project.mobile.movie_db_training.list.MoviesListFragment;
import com.project.mobile.movie_db_training.utils.Constants;
import com.squareup.picasso.Picasso;
import com.takusemba.multisnaprecyclerview.MultiSnapRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment implements MovieDetailContract.View {

    @BindView(R.id.text_name)
    TextView mName;
    @BindView(R.id.text_overview)
    TextView mOverview;
    @BindView(R.id.image_backdrop)
    ImageView mBackDrop;
    @BindView(R.id.text_rating)
    TextView mRating;
    @BindView(R.id.text_release_date)
    TextView mReleaseDate;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.review_title)
    TextView mReviewTitle;
    @BindView(R.id.rv_reviews)
    RecyclerView mReviewsRv;
    @BindView(R.id.rv_cast)
    MultiSnapRecyclerView mCastRv;
    @BindView(R.id.fab_favorite)
    FloatingActionButton mFabFavorite;
    private ReviewsListAdapter mReviewsAdapter;
    private CastAdapter mCastAdapter;
    private Unbinder mUnbinder;
    private Movie mMovie;
    private MovieDetailContract.Presenter mPresenter;
    private List<Review> mReviews = new ArrayList<>();
    private List<Cast> mCasts = new ArrayList<>();
    private Callback mCallback;
    public MovieDetailFragment() {
        // Required empty public constructor
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MovieDetailFragment.Callback) {
            mCallback = (MovieDetailFragment.Callback) context;
        }
    }
    public static MovieDetailFragment newInstance(@NonNull Movie movie) {
        Bundle args = new Bundle();
        args.putParcelable(Constants.MOVIE_KEY, movie);
        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
        movieDetailFragment.setArguments(args);
        return movieDetailFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        setToolbar();
//        setFloatButtonListener();
        initCastLayout();
        initReviewsLayout();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getContext() == null) return;
        mPresenter = new MovieDetailPresenterImpl(getContext());
        mPresenter.setView(this);
        if (getArguments() != null) {
            Movie movie = getArguments().getParcelable(Constants.MOVIE_KEY);
            if (movie != null) {
                mMovie = movie;
                showInfo(mMovie);
//                mPresenter.showFavorite(mMovie.getId());
                mPresenter.fetchCast(mMovie.getId());
                mPresenter.fetchVideos(mMovie.getId());
                mPresenter.fetchReviews(mMovie.getId());
            }
        } else {
            // get latest movie
            mPresenter.fetchLatestMovie();
        }
    }

    private void setToolbar() {
        if (getActivity() == null) return;
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(view -> {
            getActivity().finish();
        });
    }

    private void initReviewsLayout() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mReviewsRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = linearLayoutManager.getItemCount();
                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (totalItemCount == lastVisibleItem + 1) {
                    mPresenter.loadMoreReviews(mMovie.getId());
                }
            }
        });
        mReviewsRv.setLayoutManager(linearLayoutManager);
        mReviewsAdapter = new ReviewsListAdapter(mReviews);
        mReviewsRv.setAdapter(mReviewsAdapter);
    }
    private void initCastLayout() {
        mCastRv.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL,false));
        mCastAdapter = new CastAdapter(mCasts,mCallback);
        mCastRv.setAdapter(mCastAdapter);
    }
    private void setFloatButtonListener() {
        mFabFavorite.setOnClickListener(view1 -> {
            onFabFavoriteClick();
        });
    }

    @Override
    public void showInfo(Movie movie) {
        this.mMovie = movie;
        Picasso.get().load(Constants.IMAGE_BASE_URL + Constants.LARGE_IMAGE_WITDH_PATH +
                movie.getBackdropPath())
                .into(mBackDrop);
        mName.setText(movie.getTitle());
        mOverview.setText(movie.getOverview());
        mRating.setText(String.format(getString(R.string.rating), movie.getVoteAverage()));
        mReleaseDate.setText(movie.getReleaseDate());
    }

    @Override
    public void showVideos(List<Video> videos) {
        if (getActivity() == null || videos.size() == 0) return;
        HorizontalScrollView horizontalTrailers = getActivity().findViewById(R.id.horizontal_trailers);
        ViewGroup trailers = horizontalTrailers.findViewById(R.id.trailers);
        horizontalTrailers.setVisibility(View.VISIBLE);
        LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (vi != null) {
            for (int i = 0; i < videos.size(); i++) {
                View view = vi.inflate(R.layout.video, trailers, false);
                ImageView imageView = view.findViewById(R.id.image_thumb);
                Picasso.get().load(Constants.BASE_IMAGE_THUMB_URL +
                        videos.get(i).getKey() + Constants.IMAGE_THUMB_PATH).into(imageView);
                imageView.setTag(videos.get(i).getKey());
                imageView.setOnClickListener(view1 -> {
                    onImageThumbClick(imageView);
                });
                trailers.addView(view);
            }
        }
    }

    @Override
    public void showReviews(@NonNull List<Review> reviews) {
        mReviewTitle.setText(String.format(getString(R.string.review), reviews.size()));
        if (reviews.size() > 0) {
            mReviews.addAll(reviews);
            mReviewsRv.setVisibility(View.VISIBLE);
            mReviewsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showCast(List<Cast> casts) {
        this.mCasts.clear();
        this.mCasts.addAll(casts);
        mCastAdapter.notifyDataSetChanged();
    }

    private void onImageThumbClick(View view) {
        String videoUrl = Constants.BASE_YOUTUBE_URL + view.getTag();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
        if (getContext() != null) {
            try {
                getContext().startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getContext(), Constants.PLAY_VIDEO_ERROR, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void onFabFavoriteClick() {
        mPresenter.onFabFavoriteClick(mMovie);
    }

    @Override
    public void showLoading(String message) {
        if (getView() != null) Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showFavoriteButton(int status) {
        mFabFavorite.setImageDrawable(getResources().getDrawable(
                status == Constants.FAVORITE_ACTIVE ?
                        R.drawable.ic_favorite_active_24dp : R.drawable.ic_favorite_black_24dp
        ));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.destroy();
        mUnbinder.unbind();
    }

    public interface Callback {
        void onCastClick(String id);
    }
}

package com.project.mobile.movie_db_training.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.mobile.movie_db_training.R;
import com.project.mobile.movie_db_training.data.model.Movie;
import com.project.mobile.movie_db_training.list.MoviesListActivity;
import com.project.mobile.movie_db_training.list.MoviesListFragment;
import com.project.mobile.movie_db_training.utils.Constants;
import com.project.mobile.movie_db_training.utils.GettingTitle;
import com.takusemba.multisnaprecyclerview.MultiSnapRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MoviesFragment extends Fragment implements MainContract.View{
    @BindView(R.id.tv_list_type)
    TextView mListTypeTv;
    @BindView(R.id.rv_movie_list)
    MultiSnapRecyclerView mMovieListRv;
    @BindView(R.id.tv_see_all)
    TextView mSeeAllTv;
    private Unbinder mUnbinder;
    private MainPresenterImpl mPresenter;
    private List<Movie> mMovies = new ArrayList<>();
    private MovieAdapter mAdapter;
    private String mListType;
    private MoviesListFragment.Callback mCallback ;
    public MoviesFragment() {
        // Required empty public constructor
    }
    public static MoviesFragment newInstance(String listType) {
        Bundle args = new Bundle();
        args.putString(Constants.LIST_TYPE, listType);
        MoviesFragment fragment = new MoviesFragment();
        fragment.setArguments(args);
        return fragment;
}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_list,container,false);
        mUnbinder = ButterKnife.bind(this,rootView);
        setupRecyclerView(mMovieListRv,mAdapter);
        if (getArguments() != null && getArguments().containsKey(Constants.LIST_TYPE)) {
            mListType = getArguments().getString(Constants.LIST_TYPE);
        }
        if (mListType != null) {
            mListTypeTv.setText(GettingTitle.getTitleFromListType(mListType));
            mSeeAllTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), MoviesListActivity.class);
                    intent.putExtra(Constants.LIST_TYPE, mListType);
                    startActivity(intent);
                }
            });
        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = new MainPresenterImpl();
        mPresenter.setView(this);
        mPresenter.fetchMovies(mListType);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MoviesListFragment.Callback) {
            mCallback = (MoviesListFragment.Callback) context;
        }
    }
    private void setupRecyclerView(MultiSnapRecyclerView view, MovieAdapter adapter) {
        mAdapter = new MovieAdapter(mMovies,mCallback);
        view.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        view.setAdapter(mAdapter);
    }

    @Override
    public void showMovies(List<Movie> movies, String listType) {
        mMovies.addAll(movies);
        mMovieListRv.setVisibility(View.VISIBLE);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLoading(String message) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}


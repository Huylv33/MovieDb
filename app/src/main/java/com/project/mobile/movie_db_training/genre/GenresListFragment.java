package com.project.mobile.movie_db_training.genre;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.project.mobile.movie_db_training.R;
import com.project.mobile.movie_db_training.data.model.Genre;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class GenresListFragment extends Fragment implements GenresListContract.View {
    @BindView(R.id.rv_genres_list)
    RecyclerView mGenresListRv;
    private RecyclerView.Adapter mAdapter;
    private List<Genre> mGenreList = new ArrayList<>();
    private GenresListContract.Presenter mGenreListPresenter;
    private Unbinder mUnbinder;
    private Callback mCallback;

    public GenresListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Callback) {
            mCallback = (Callback) context;
        }
    }

    @Override
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container,
                                          Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        android.view.View view = inflater.inflate(R.layout.fragment_genres_list, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        initLayout();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull android.view.View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGenreListPresenter = new GenresListPresenterImpl();
        mGenreListPresenter.setView(this);
        mGenreListPresenter.fetchGenres();
    }

    private void initLayout() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mGenresListRv.setLayoutManager(layoutManager);
        mAdapter = new GenresListAdapter(mGenreList, mCallback);
        mGenresListRv.setAdapter(mAdapter);
    }

    @Override
    public void showGenres(List<Genre> genresList) {
        this.mGenreList.clear();
        this.mGenreList.addAll(genresList);
        mGenresListRv.setVisibility(android.view.View.VISIBLE);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLoading(String message) {
        Snackbar.make(mGenresListRv, message, Snackbar.LENGTH_LONG);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        mGenreListPresenter.destroy();
    }

    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }

    interface Callback {
        void onGenreClick(Genre genre);
    }
}

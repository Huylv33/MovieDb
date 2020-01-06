package com.project.mobile.movie_db_training.main;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mobile.movie_db_training.R;
import com.project.mobile.movie_db_training.data.model.Movie;
import com.project.mobile.movie_db_training.list.MoviesListFragment;
import com.project.mobile.movie_db_training.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private List<Movie> mMovies;
    private MoviesListFragment.Callback mCallback;

    public MovieAdapter(List<Movie> movies, MoviesListFragment.Callback callback) {
        mMovies = movies;
        mCallback = callback;
    }

    @NonNull
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item_2, parent, false);
        return new MovieAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.ViewHolder holder, int position) {
        holder.bind(mMovies.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onMovieClick(mMovies.get(position));
            }
    });
    }

    @Override
    public int getItemCount() {
        return mMovies != null ? mMovies.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.movie_poster)
        ImageView poster;
        @BindView(R.id.movie_name)
        TextView name;
        @BindView(R.id.movie_rating)
        TextView rating;
        public Movie movie;

        public ViewHolder(View root) {
            super(root);
            ButterKnife.bind(this, root);
        }

        public void bind(Movie movie) {
            name.setText(movie.getTitle());
            rating.setText(movie.getVoteAverage());
            Log.d("MovieAdapter", movie.getTitle() + " " + movie.getVoteAverage());
            Picasso.get().load(Constants.POSTER_BASE_URL
                    + movie.getPosterPath()).into(poster);
        }
    }
}

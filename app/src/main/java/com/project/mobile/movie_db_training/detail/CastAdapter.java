package com.project.mobile.movie_db_training.detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mobile.movie_db_training.R;
import com.project.mobile.movie_db_training.data.model.Cast;
import com.project.mobile.movie_db_training.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastHolder> {
    private List<Cast> mCasts = new ArrayList<>();
    private MovieDetailFragment.Callback mCallback;

    public CastAdapter(List<Cast> casts, MovieDetailFragment.Callback callback) {
        mCasts = casts;
        mCallback = callback;
    }

    @NonNull
    @Override
    public CastHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cast_item, parent, false);
        return new CastHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CastHolder holder, int position) {
        holder.bind(mCasts.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onCastClick(mCasts.get(position).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCasts != null ? mCasts.size() : 0;
    }

    public class CastHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_cast_profile)
        ImageView mCastProfile;
        @BindView(R.id.text_cast_name)
        TextView mCastName;

        public CastHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Cast cast) {
            mCastName.setText(cast.getName());
            Picasso.get().load(Constants.IMAGE_BASE_URL + "w185/" +
                    cast.getProfilePath())
                    .placeholder(R.drawable.fade_image)
                    .error(R.drawable.image_error)
                    .into(mCastProfile);
        }
    }
}

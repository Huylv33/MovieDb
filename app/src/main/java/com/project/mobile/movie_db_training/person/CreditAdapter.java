package com.project.mobile.movie_db_training.person;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mobile.movie_db_training.R;
import com.project.mobile.movie_db_training.data.model.Credit;
import com.project.mobile.movie_db_training.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreditAdapter extends RecyclerView.Adapter<CreditAdapter.CreditHolder> {
    private List<Credit> mCredits = new ArrayList<>();

    public CreditAdapter(List<Credit> credits) {
        mCredits = credits;
    }

    @NonNull
    @Override
    public CreditHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.credit_item, parent, false);
        return new CreditHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CreditHolder holder, int position) {
        holder.bind(mCredits.get(position));
    }

    @Override
    public int getItemCount() {
        return mCredits.size();
    }

    public class CreditHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.movie_poster)
        ImageView mPoster;
        @BindView(R.id.movie_name)
        TextView mMovieName;
        @BindView(R.id.cast_name)
        TextView mCastName;

        public CreditHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Credit credit) {
            Picasso.get().load(Constants.POSTER_BASE_URL + credit.getPosterPath())
                    .placeholder(R.drawable.fade_image)
                    .error(R.drawable.image_error)
                    .into(mPoster);
            mMovieName.setText(credit.getTitle());
            mCastName.setText(credit.getCharacter());
        }
    }
}

package com.project.mobile.movie_db_training.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "watchlist", indices = {@Index(value = {"id"}, unique = true)})
public class WatchlistEntity {
    @ColumnInfo(name = "id")
    @PrimaryKey
    @NonNull
    private String mId;
    @ColumnInfo(name = "title")
    private String mTitle;
    @ColumnInfo(name = "vote_average")
    private String mVoteAverage;
    @ColumnInfo(name = "backdrop_path")
    private String mBackdropPath;
    @ColumnInfo(name = "release_date")
    private String mReleaseDate;
    @ColumnInfo(name = "overview")
    private String mOverview;

    public WatchlistEntity() {
    }

    public WatchlistEntity(Movie movie) {
        this.mId = movie.getId();
        this.mTitle = movie.getTitle();
        this.mVoteAverage = movie.getVoteAverage();
        this.mBackdropPath = movie.getBackdropPath();
        this.mReleaseDate = movie.getReleaseDate();
        this.mOverview = movie.getOverview();
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getVoteAverage() {
        return mVoteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        mVoteAverage = voteAverage;
    }

    public String getBackdropPath() {
        return mBackdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        mBackdropPath = backdropPath;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String overview) {
        mOverview = overview;
    }

}

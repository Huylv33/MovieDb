package com.project.mobile.movie_db_training.utils;

public class Utils {
    public static String getTitleFromListType(String listType) {
        switch (listType) {
            case Constants.NOW_PLAYING:
                return Constants.NOW_PLAYING_TITLE_BAR;
            case Constants.POPULAR:
                return Constants.POPULAR_TITLE_BAR;
            case Constants.UPCOMING:
                return Constants.UPCOMING_TITLE_BAR;
            case Constants.TOP_RATED:
                return Constants.TOP_RATED_TITLE_BAR;
            default:
                break;
        }
        return null;
    }

}

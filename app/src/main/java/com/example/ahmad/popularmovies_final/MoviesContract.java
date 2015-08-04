package com.example.ahmad.popularmovies_final;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Ahmad on 7/23/2015.
 */
public class MoviesContract {

    /*Content Authority*/
    public static final String CONTENT_AUTHORITY = "com.example.ahmad.popularmovies_final";

    //BASE URI CONTENT
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    //Name of the movie table
    public static final String PATH_MOVIE = "movie";

    //Name of the review table
    public static final String PATH_REVIEW = "review";


    public static final int MOVIES = 100;
    public static final int MOVIES_STAGE_SORT = 101;
    public static final int MOVIE_DETAIL = 102;
    public static final int REVIEWS = 300;



    public static final class MoviesEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        //table name
        public static final String TABLE_NAME = "movie";

        //columns names
        public static final String MOV_COL_ID = "movie_id";
        public static final String MOV_COL_TITLE = "movie_title";
        public static final String MOV_COL_OVERVIEW = "movie_overview";
        public static final String MOV_COL_FAVORITE = "movies_favorite";
        public static final String MOV_COL_POPULARITY = "movie_popularity";
        public static final String MOV_COL_VOTE_COUNTS = "movie_vote_counts";
        public static final String MOV_COL_ORIGINAL_TITLE = "movie_original_title";
        public static final String MOV_COL_POSTER = "movie_poster";
        public static final String MOV_COL_BACKDROP = "movie_backdrop";
        public static final String MOV_COL_RELEASE_DATE = "movie_release_date";
        public static final String MOV_COL_VOTE_AVE = "movie_vote_ave";

        //Build URI FOR GET MOVIE WITH ID.
        public static Uri buildMovieWithIdUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        //BUILD URI for fetching movies with spcifie sorting type
        public  static Uri buildMovieWithSortUri(String sort_type)
        {
            return CONTENT_URI.buildUpon().appendPath(sort_type).build();
        }
    }

    public static final class ReviewsEntry implements  BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEW).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;

        //Table Name
        public static final String TABLE_NAME = "review";

        //tables columns name
        public static final String REV_COL_AUTHOR = "review_author";
        public static final String REV_COL_CONTENT = "review_content";
        public static final String MOVIE_ID = "movie_id";

        public static Uri buildReviewUri(long id)
        {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildReviewWithMovieId(long movie_id)
        {
            return ContentUris.withAppendedId(CONTENT_URI, movie_id);
        }
    }
}


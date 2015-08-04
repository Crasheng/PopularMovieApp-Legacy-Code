package com.example.ahmad.popularmovies_final;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by Ahmad on 7/26/2015.
 */
public class MovieProvider extends ContentProvider {


    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    public static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority =  MoviesContract.CONTENT_AUTHORITY;


        //the order of that adding the Uris is very important, and i have to check first whether
        //it is an id "Number", and else if it is string segment.
        //According to the questions on StackOverFlow!
        /*http://stackoverflow.com/questions/5030094/problems-with-androids-urimatcher/15015687#15015687*/

        /*Hint
        * Try to flip the 2nd and 3rd statement around and the test fails
        * */
        matcher.addURI(authority, "/"+MoviesContract.PATH_MOVIE, MoviesContract.MOVIES);
        matcher.addURI(authority, "/"+MoviesContract.PATH_MOVIE + "/#", MoviesContract.MOVIE_DETAIL);
        matcher.addURI(authority, "/"+MoviesContract.PATH_MOVIE + "/*", MoviesContract.MOVIES_STAGE_SORT);
        matcher.addURI(authority, "/"+MoviesContract.PATH_REVIEW, MoviesContract.REVIEWS);
        return matcher;


    }
}

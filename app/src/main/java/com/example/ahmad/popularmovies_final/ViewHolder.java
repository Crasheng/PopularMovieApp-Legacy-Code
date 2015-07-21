package com.example.ahmad.popularmovies_final;

import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by Ahmad on 7/21/2015.
 * This is a Class to Implement the ViewHolder Design Pattern
 * Simply, It Contains all what you need to populate on a screen activity
 */
public class ViewHolder {

    //Making the Variable of the class public, may destroy the encapsulation but, more important it works fine
    //and you can just make them private , and create setters and getters
    public TextView movie_rating_ratio;
    public RatingBar movie_rating;
    public TextView movie_overview;
    public TextView movie_release_date;
    public TextView movie_original_title;
    public ImageView movie_backdrop;
    public ImageView movie_poster;
}

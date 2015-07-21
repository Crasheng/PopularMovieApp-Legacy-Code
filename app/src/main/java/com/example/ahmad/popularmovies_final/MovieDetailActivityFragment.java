package com.example.ahmad.popularmovies_final;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {

    private final String POSTERBASEURL = "http://image.tmdb.org/t/p/w185";
    private final String BACKDROPBASEURL = "http://image.tmdb.org/t/p/w500";
    ViewHolder holder = null;

    public MovieDetailActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        holder = new ViewHolder();
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        if (savedInstanceState == null) {
            holder.movie_rating = (RatingBar) view.findViewById(R.id.movie_rating_stars);
            holder.movie_rating_ratio = (TextView) view.findViewById(R.id.vote_ratio);
            holder.movie_poster = (ImageView) view.findViewById(R.id.poster_image);
            holder.movie_overview = (TextView) view.findViewById(R.id.movie_overview);
            holder.movie_backdrop = (ImageView) view.findViewById(R.id.backdrop_image);
            holder.movie_release_date = (TextView) view.findViewById(R.id.movie_release_date);
            holder.movie_original_title = (TextView) view.findViewById(R.id.movie_original_name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Intent extra_data = getActivity().getIntent();
        ArrayList<MovieData> clicked_item_list = extra_data.getParcelableArrayListExtra("Single_Movie_Data");
        MovieData clicked_movie = clicked_item_list.get(0);


        //get the relative path to each movies' data
        String poster = clicked_movie.getMoviePoster();
        String backdrop = clicked_movie.getMovieBackdrop();


        //Applying all Required Data to the View
        Picasso.with(getActivity()).load(POSTERBASEURL + poster).noFade().into(holder.movie_poster);
        Picasso.with(getActivity()).load(BACKDROPBASEURL + backdrop).placeholder(R.drawable.spinner).noFade().into(holder.movie_backdrop);

        holder.movie_rating.setRating(clicked_movie.getMovieRating());
        holder.movie_rating_ratio.setText(" (" + String.valueOf(clicked_movie.getMovieRating()) + ")");
        holder.movie_release_date.setText("Release Date: " + clicked_movie.getMovieReleaseDate());
        holder.movie_overview.setText(clicked_movie.getMovieOverview());
        holder.movie_original_title.setText(clicked_movie.getMovieOriginalTitle());
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}

package com.example.ahmad.popularmovies_final;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ahmad on 7/9/2015.
 * This Class to Load the Data into the MovieData Object
 */
public class ParseMoviesData {

    private static int length;
    private static JSONArray results;
    private static MovieData[] movie_data = null;
    private static ArrayList<MovieData> movies_datas = new ArrayList<>();


    public static int getNumerOfMovies(String recieved_json) {
        try {
            results = new JSONObject(recieved_json).getJSONArray("results");
            length = results.length();

        } catch (JSONException e) {
            length = 0;
            e.printStackTrace();
        }
        return length;
    }

    public static void fillEachMovieData(ArrayList<MovieData> movies_data) {
        JSONObject mJson;
        for (int i = 0; i < length; i++) {
            try {

                //Get JSON Object to refer to each movie data easily.
                mJson = results.getJSONObject(i);
                movies_data.add(i, new MovieData().set_id(mJson.getInt("id"))
                        .originaltitle(mJson.getString("original_title"))
                        .title(mJson.getString("title")).averagevote((float) mJson.getDouble("vote_average"))
                        .backdropPath(mJson.getString("backdrop_path"))
                        .overview(mJson.getString("overview")).posterPath(mJson.getString("poster_path"))
                        .releaseDate(mJson.getString("release_date")));


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static ArrayList<MovieData> getSweatMovieData() {
        return movies_datas;
    }
}

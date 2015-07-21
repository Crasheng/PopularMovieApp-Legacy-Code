package com.example.ahmad.popularmovies_final;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final String API_KEY = "";
    private final String MOVIES_PARC_KEY = "MOVIES_DATA";
    CardAdapter movie_adapter_data;
    FetchMoviesData fetch_task;
    //Variable to hold what returned from fetched data
    ArrayList<MovieData> movies_data = new ArrayList<>();
    private String arrangement_flag = "popularity.desc";

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        movie_adapter_data = new CardAdapter(getActivity(), R.layout.movie_card);
        GridView gv = (GridView) v.findViewById(R.id.grid_list);
        gv.setAdapter(movie_adapter_data);

        //check if this created activity created first time or not
        //if not first time
        if (savedInstanceState != null) {
            //retrive the data from the saved instance state
            movies_data = savedInstanceState.getParcelableArrayList(MOVIES_PARC_KEY);
            //clear any previous data into the adapter
            movie_adapter_data.notifyDataSetChanged();
            //add the data to the adapter
            movie_adapter_data.addAll(movies_data);
        } else
            //In case the activity being created for first time
            //Excute a new Fetch thread, and adapterdata via OnPostExceute
            new FetchMoviesData().execute(arrangement_flag);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        GridView gv = (GridView) getActivity().findViewById(R.id.grid_list);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Array List of Parcable objects to be passed to new Activity
                ArrayList<MovieData> clicked_item_list = new ArrayList<>();

                //add the clicked item.
                clicked_item_list.add((MovieData) (parent.getItemAtPosition(position)));

                //make a new intent to open anew activity
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class);

                //Put Array List of Parcable objects into Extra
                intent.putParcelableArrayListExtra("Single_Movie_Data", clicked_item_list);
                //Start the Activity.
                startActivity(intent);
            }
        });
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(MOVIES_PARC_KEY, movies_data);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        menu.setGroupCheckable(R.id.menu_group, true, true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pop_movies:
                item.setChecked(true);
                arrangement_flag = "popularity.desc";
                movies_data = null;
                movies_data = new ArrayList<>();
                new FetchMoviesData().execute(arrangement_flag);
                return true;
            case R.id.most_rated:
                item.setChecked(true);
                arrangement_flag = "vote_count.desc";
                movies_data = null;
                movies_data = new ArrayList<>();
                new FetchMoviesData().execute(arrangement_flag);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    /*
    * Overriding Asynctask class to craete threads for fetching data from internet
    * */
    class FetchMoviesData extends AsyncTask<String, Void, String> {
        int length;
        ArrayList<MovieData> movie_data;

        @Override
        protected void onPostExecute(String s) {


            //Prepare the required data.
            length = ParseMoviesData.getNumerOfMovies(s);
            ParseMoviesData.fillEachMovieData(movies_data);
            movie_adapter_data.clear();
            movie_adapter_data.addAll(movies_data);

            Toast.makeText(getActivity(), "Fetching Movies", Toast.LENGTH_LONG).show();


        }

        @Override
        protected String doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String fetched_movies = null;

            try {
                // Construct the URL for the MoviesData query
                final String MOVIEDB_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
                final String api_key_param = "api_key";
                final String sort_param = "sort_by";


                Uri builderUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                        .appendQueryParameter(sort_param, params[0])
                        .appendQueryParameter(api_key_param, API_KEY)
                        .build();

                URL url = new URL(builderUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    fetched_movies = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    fetched_movies = null;
                }
                fetched_movies = buffer.toString();
                Log.v("Movies DATA FETCHED", "Fetched_movies" + fetched_movies);
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                fetched_movies = null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            Log.d("fetcheee", fetched_movies);
            return fetched_movies;
        }
    }
}

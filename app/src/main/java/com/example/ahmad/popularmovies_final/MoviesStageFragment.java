package com.example.ahmad.popularmovies_final;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.ahmad.popularmovies_final.Data.MoviesContract;
import com.example.ahmad.popularmovies_final.Data.MoviesContract.MoviesEntry;

import javax.security.auth.callback.Callback;


/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesStageFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, Callback, FetchDataInternet.FetchedDataReady {

    //LOG AN EVENT.
    public static final String TAG = "check_populating";

    private static boolean INTERNET_STATUE_OPENED = true;

    //FLAG TO KNOW WHICH DEVICE WORKING RIGHT NOW.
//    private static boolean TABLET_FLAG = false;


    //Custom Adapter for movies data
    MovieCardAdapter movie_adapter_data;

    //Cursor identifier.
    static final int CUR_LOADER_ID = 0 ;

    //Column that is in need.
    private static String[] projections ={
            MoviesEntry._ID,
            MoviesEntry.MOV_COL_ID,
            MoviesEntry.MOV_COL_POSTER
    };



    private static String arrangement_flag = "popularity.DESC";


    @Override
    public void RequestedDataReady(ContentValues[] fetched_movies, int mode) {

        if (fetched_movies != null && mode == UtilityMovieData.REQUEST_MOVIES) {
            getActivity().getContentResolver().bulkInsert(MoviesContract.MoviesEntry.CONTENT_URI, fetched_movies);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (isNetworkAvailable(activity)) {
            INTERNET_STATUE_OPENED = false;
        }
    }

    //The MoviesStageActivity will implement this interface.
    public interface onMovieClick{
        void movieHasBeenClicked(Uri clicked_movie);
    }


    public MoviesStageFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        setRetainInstance(true);
        if (savedInstanceState == null && INTERNET_STATUE_OPENED) {
            //CREATE a new instance and start execute it.
            new FetchDataInternet(this, UtilityMovieData.REQUEST_MOVIES).execute(arrangement_flag);
        }
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onResume() {
            super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        //Pass the cursor null, because it does not exist yet,
        movie_adapter_data = new MovieCardAdapter(getActivity(),null);

        GridView gridview = (GridView) view.findViewById(R.id.grid_list);
        gridview.setAdapter(movie_adapter_data);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {


        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        getLoaderManager().initLoader(CUR_LOADER_ID, null, this);


        //Reference to the grid view.
        GridView gridview = (GridView) getActivity().findViewById(R.id.grid_list);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                Uri movie_detail_uri = null;
                if (cursor != null) {

                    //getting the clicked movie id.
                    int id_col_index = cursor.getColumnIndex(projections[1]);
                    int umovie_id = cursor.getInt(id_col_index);

                    //get the new URI constructed pointing to the clicked movie row.
                    movie_detail_uri = MoviesEntry.buildMovieWithUniIdUri(umovie_id);
                }
                //register that event happened.
                ((onMovieClick) getActivity()).movieHasBeenClicked(movie_detail_uri);
            }
        });
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        //must be called after these items added to the menu.
        menu.setGroupCheckable(R.id.menu_group, true, true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pop_movies:
                item.setChecked(true);
                arrangement_flag = MoviesEntry.MOV_COL_POPULARITY+"."+MoviesEntry.SORT_ORDER.trim();
                new FetchDataInternet(this, UtilityMovieData.REQUEST_MOVIES).execute(arrangement_flag);
                getLoaderManager().restartLoader(CUR_LOADER_ID, null, this);
                return true;
            case R.id.most_rated:
                item.setChecked(true);
                arrangement_flag = MoviesEntry.MOV_COL_VOTE_COUNTS+"."+MoviesEntry.SORT_ORDER.trim();
                new FetchDataInternet(this, UtilityMovieData.REQUEST_MOVIES).execute(arrangement_flag);
                getLoaderManager().restartLoader(CUR_LOADER_ID, null, this);
                return true;
            case R.id.refresh:
                INTERNET_STATUE_OPENED = isNetworkAvailable(getActivity());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Uri populate_stage_uri;
        if (arrangement_flag.equals("popularity.DESC")) {
            populate_stage_uri = MoviesEntry.buildMovieWithSortUri(MoviesEntry.MOV_COL_POPULARITY + MoviesEntry.SORT_ORDER);
        }
        else {
            populate_stage_uri = MoviesEntry.buildMovieWithSortUri(MoviesEntry.MOV_COL_VOTE_COUNTS + MoviesEntry.SORT_ORDER);
        }

        return new CursorLoader(getActivity(),populate_stage_uri,projections,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        movie_adapter_data.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        movie_adapter_data.swapCursor(null);
    }
    private boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

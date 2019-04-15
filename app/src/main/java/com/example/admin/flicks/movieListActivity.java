package com.example.admin.flicks;

import android.app.DownloadManager;
import android.graphics.Bitmap;
import android.graphics.Movie;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Adapter;
import android.widget.Toast;

import com.example.admin.flicks.models.Config;
import com.example.admin.flicks.models.movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public abstract class movieListActivity extends AppCompatActivity {

    //constants
    //the base URL for the API
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";
    //the parameter name for the API key
    public final static String API_KEY_PARAM = "api_key";
    // tag for logging from this activity
    public final static String TAG = "MovieListActivity";
    //instance fields
    AsyncHttpClient client;
    // the list of currently playing movies
    ArrayList<Movie> movies;
    // the recycler view
    RecyclerView rvMovies;
    // the adapter wired to the recycler view
    MovieAdapter Adapter;
    //image config
    Config config;
    // initialize the adapter  -- movies array cannot be reinitialized after this point
    Adapter = new MovieAdapter(movies);
    // resolve the recycler view and connect a layout manager and the adapter
    rvMovies = (RecyclerView) findViewById(R.id.rvMvies);
    rvMovies.setLayoutManager (new LinearLayoutManager(this));
    rvMovies.setAdapter(Adapter);


    private Object Movie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        // initialize the client
        client = new AsyncHttpClient();
        // initialize the List of movies
        movies = new ArrayList<>();
        //get the configuration on app creation
        getConfiguration();


        }
        // get the List of currently playing movies from the API
        private void getNowPlaying(){
        //create the url
            String url = API_BASE_URL +"/config/movie/now_playing";
            // set the request parameters
            RequestParams params = new RequestParams();
            params.put(API_KEY_PARAM, getString(R.string.api_key));// API key, always.required
            // execute a GET request expecting a JSON object response
            RequestHandle results = client.get(url, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    //load the results into movies list
                    try {
                        JSONArray results = response.getJSONArray("results");
                        // Interate through result set and create Movie objects
                        for (int i = 0; i < results.length(); i++) {
                            movie movie = new movie(results.getJSONObject(i));
                            movies.add(Movie );
                            // notify adapter that a row was added
                            Adapter.notifyItemInserted( movie.size() -1);
                        }
                        Log.i(TAG,String.format("Loaded s movies", results.length()));
                    } catch (JSONException e) {
                        logError("Failed to parse now playing movies", e, true);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    logError("Failed to get data from now_Playing endpoint", throwable, true);
                }
            });

        }

    //get the configuration from the API
    private void  getConfiguration(){
        //create the url
        String url = API_BASE_URL +"/configuration";
        // set the request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));// API key, always.required
        // execute a GET request expecting a JSON object response
        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                   config = new Config( response );
                    Log.i(TAG,
                            String.format("Loaded configuration with imageBasaUrl % and posterSize %",
                                    config.getImageBaseUrl(),
                                    config.getPosterSize()));
                    // pass config to adapter
                    Adapter.setConfig(config);
                    // get the now playing movie list
                    getNowPlaying();
                } catch (JSONException e) {
                   logError("Failed parsing configuration", e,true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
               logError("Failed getting configuration", throwable, true);
            }
        });
    }

    //handle errors Log and alert user
    private void logError(String message,Throwable error, boolean alertUser){
        // always log the error
        Log.e(TAG,message,error);
        // alert the user to avoid silent errors
        if (alertUser){
            //show a long toast with the error message
            Toast.makeText(getApplicationContext(),message ,Toast.LENGTH_LONG).show();
        }
    }
}

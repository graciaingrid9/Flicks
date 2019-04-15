package com.example.admin.flicks.models;

import org.json.JSONException;
import org.json.JSONObject;

public class movie{
        //values from API {
    private String title;
    private String Overview;
    private String posterPath; // only the path

    // initialize from JSON data
    public movie(JSONObject object) throws JSONException{
        title = object.getString("title");
        Overview = object.getString("overview");
        posterPath = object.getString("poster_path");

    }

        public String getTitle() {
            return title;
        }

        public String getOverview() {
            return Overview;
        }


        public String getPosterPath() {
            return posterPath;
        }
}


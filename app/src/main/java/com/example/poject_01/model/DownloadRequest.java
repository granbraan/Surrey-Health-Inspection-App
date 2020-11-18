package com.example.poject_01.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class DownloadRequest {
    private RequestQueue mQueue;
    private String url;
    private Context rContext;
    private String fileName;
    private SharedPreferences prefs;


    public DownloadRequest(String url, Context rContext, String fileName) {
        this.url = url;
        this.rContext = rContext;
        this.fileName = fileName;
        this.prefs = rContext.getSharedPreferences("startup_logic", MODE_PRIVATE);
    }


    private void downloadData(String downloadURL) {
        DownloadDataAsyncTask task = new DownloadDataAsyncTask(rContext, fileName);
        task.execute(downloadURL);

        // updating preferences used to control flow of app
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("initial_update", true);
        Date currentDate = new Date(System.currentTimeMillis());
        editor.putLong("last_update", currentDate.getTime() );
        editor.commit();


    }

    public void getURL() {
        mQueue = Volley.newRequestQueue(rContext);

        JsonObjectRequest restaurantsRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject object1 = response.getJSONObject("result");
                    JSONArray array1 = object1.getJSONArray("resources");
                    JSONObject data = array1.getJSONObject(0);
                    String dataURL  =  data.getString("url");
                    Log.d("Get URL - URL", "" + dataURL);

                    // check if last_modified has been updated
                    String surrey_last_modified = data.getString("last_modified");
                    String restaurant_last_modified = prefs.getString("restaurant_last_modified", "");
                    String inspections_last_modified = prefs.getString("inspections_last_modified", "");
                    Log.d("surrey_last_modified", ""+ surrey_last_modified);
                    Log.d("restaurant_last_modified", ""+ restaurant_last_modified);
                    Log.d("inspection_last_modified", ""+ inspections_last_modified);

                    if (!Objects.equals(surrey_last_modified, restaurant_last_modified) ){
                        // TODO: get user input for download (alert dialog)
                        Toast.makeText(rContext, "Do you wanna update?", Toast.LENGTH_LONG).show();
                        downloadData(dataURL);
                        }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(restaurantsRequest);

    }
}

package com.example.poject_01.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

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

    private static final int RESTAURANT_URL_CHECK = 0;
    private static final int INSPECTION_URL_CHECK = 1;
    private RequestQueue mQueue;
    private String url;
    private Context rContext;
    private String fileName;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private boolean urlModified;
    private String surreyLastModified;
    private String csvDataURL;
    private int dataSetCheck;



    public DownloadRequest(String url, Context rContext, String fileName, int check ) {
        this.url = url;
        this.rContext = rContext;
        this.fileName = fileName;
        this.prefs = rContext.getSharedPreferences("startup_logic", MODE_PRIVATE);
        this.editor = prefs.edit();
        this.dataSetCheck = check;
    }



    public boolean dataModified(){
        return urlModified;
    }



    public void downloadData() {
        DownloadDataAsyncTask task = new DownloadDataAsyncTask(rContext, fileName);
        // calls DownloadDataAsyncTask.doInBackground()

        if (dataSetCheck == INSPECTION_URL_CHECK) {
            if(urlModified){
                editor.putString("inspections_last_modified" , surreyLastModified);
                task.execute(csvDataURL, "1");
            }
        }
        if(dataSetCheck == RESTAURANT_URL_CHECK){
            if(urlModified){
                editor.putString("restaurants_last_modified", surreyLastModified);
                task.execute(csvDataURL, "0");
            }
        }

        // updating preferences used to control flow of app
        editor.putBoolean("initial_update", true);
        Date currentDate = new Date(System.currentTimeMillis());
        editor.putLong("last_update", currentDate.getTime() );
        editor.commit();

    }

    public void getURL( final VolleyCallBack callBack) {
        mQueue = Volley.newRequestQueue(rContext);

        JsonObjectRequest restaurantsRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject object1 = response.getJSONObject("result");
                    JSONArray array1 = object1.getJSONArray("resources");
                    JSONObject data = array1.getJSONObject(0);
                    String dataURL  =  data.getString("url");
                    Log.d("getURL - URL", "" + dataURL);

                    // check if last_modified has been updated
                    String surrey_last_modified = data.getString("last_modified");
                    String restaurant_last_modified = prefs.getString("restaurant_last_modified", "");
                    String inspections_last_modified = prefs.getString("inspections_last_modified", "");
                    Log.d("surrey_last_modified", ""+ surrey_last_modified);
                    Log.d("restaurant_last_modified", ""+ restaurant_last_modified);
                    Log.d("inspection_last_modified", ""+ inspections_last_modified);

                    if (!Objects.equals(surrey_last_modified, restaurant_last_modified) ){
                        // TODO: get user input for download (alert dialog)
                            DownloadOption();

                            downloadData(dataURL);
                            //Toast.makeText(rContext, "Do you wanna update?", Toast.LENGTH_LONG).show();
                            //downloadData(dataURL);
                        }
                        else{
                            urlModified = false;
                        }
                        Log.d("surrey_last_modified", ""+ surreyLastModified);
                        Log.d("restaurant_last_modified", "none "+ restaurant_last_modified);
                    }
                    else if ( dataSetCheck == INSPECTION_URL_CHECK) {

                        String inspections_last_modified = prefs.getString("inspections_last_modified", "");
                        if (!Objects.equals(surreyLastModified, inspections_last_modified)){
                            urlModified = true;

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


    public interface VolleyCallBack {
        void onSuccess();
    }




}

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
    private boolean urlModified;
    private String surreyLastModified;
    private String csvDataURL;




    public DownloadRequest(String url, Context rContext, String fileName) {
        this.url = url;
        this.rContext = rContext;
        this.fileName = fileName;
        this.prefs = rContext.getSharedPreferences("startup_logic", MODE_PRIVATE);
    }

    public String getCsvUrl(){
        return csvDataURL;
    }

    public boolean dataModified(){
        return urlModified;
    }

    public void downloadData(String downloadURL) {
        DownloadDataAsyncTask task = new DownloadDataAsyncTask(rContext, fileName);
        task.execute(downloadURL);

        // updating preferences used to control flow of app
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("initial_update", true);
        Date currentDate = new Date(System.currentTimeMillis());
        editor.putLong("last_update", currentDate.getTime() );

        if(urlModified){
            editor.putString("inspections_last_modified" , surreyLastModified);
        }
        if(urlModified){
            editor.putString("restaurants_last_modified", surreyLastModified);
        }

        editor.commit();


    }

    public void getURL(int urlCheck, final VolleyCallBack callBack) {
        mQueue = Volley.newRequestQueue(rContext);

        JsonObjectRequest restaurantsRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject urlResult = response.getJSONObject("result");
                    JSONArray dataType = urlResult.getJSONArray("resources");
                    JSONObject csvObject = dataType.getJSONObject(0);
                     csvDataURL  =  csvObject.getString("url");
                    Log.d("getURL - URL", "" + csvDataURL);

                    surreyLastModified = csvObject.getString("last_modified");
                    if (urlCheck == RESTAURANT_URL_CHECK) {
                        String restaurant_last_modified = prefs.getString("restaurant_last_modified", "");
                        if (!Objects.equals(surreyLastModified, restaurant_last_modified)){
                            urlModified = true;

                        }
                        else{
                            urlModified = false;
                        }
                        Log.d("surrey_last_modified", ""+ surreyLastModified);
                        Log.d("restaurant_last_modified", "none "+ restaurant_last_modified);
                    }
                    else if ( urlCheck == INSPECTION_URL_CHECK) {
                        String inspections_last_modified = prefs.getString("inspections_last_modified", "");
                        if (!Objects.equals(surreyLastModified, inspections_last_modified)){
                            urlModified = true;

                        }
                        else{
                            urlModified = false;
                        }
                        Log.d("surrey_last_modified", ""+ surreyLastModified);
                        Log.d("inspection_last_modified", "none "+ inspections_last_modified);
                    }
                    else{
                        Log.d("getURL - onResponse", "failed to validate url - Checking for website dat changes not preformed ");
                    }
                    //downloadData(csvDataURL);
                    callBack.onSuccess();


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

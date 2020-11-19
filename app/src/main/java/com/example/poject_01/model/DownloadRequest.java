package com.example.poject_01.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.poject_01.DownloadFragment;
import com.example.poject_01.UI.MainActivity;
import com.example.poject_01.UI.MapsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class DownloadRequest {

    private RequestQueue mQueue;
    private String url;
    private Context rContext;
    private String fileName;
    private SharedPreferences prefs;
    private boolean urlModified;


    public DownloadRequest(String url, Context rContext, String fileName) {
        this.url = url;
        this.rContext = rContext;
        this.fileName = fileName;
        this.prefs = rContext.getSharedPreferences("startup_logic", MODE_PRIVATE);


    }


    public boolean wasModified(){
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
                    String csvDataURL  =  csvObject.getString("url");
                    Log.d("getURL - URL", "" + csvDataURL);

                    String surrey_last_modified = csvObject.getString("last_modified");

                    // restaurant url = 0
                    if (urlCheck == 0) {
                        String restaurant_last_modified = prefs.getString("restaurant_last_modified", "");
                        if (!Objects.equals(surrey_last_modified, restaurant_last_modified)){
                            urlModified = true;

                        }
                        else{
                            urlModified = false;
                        }
                        Log.d("surrey_last_modified", ""+ surrey_last_modified);
                        Log.d("restaurant_last_modified", "none "+ restaurant_last_modified);
                    }
                    else {
                        String inspections_last_modified = prefs.getString("inspections_last_modified", "");
                        if (!Objects.equals(surrey_last_modified, inspections_last_modified)){
                            urlModified = true;
                        }
                        else{
                            urlModified = false;
                        }
                        Log.d("surrey_last_modified", ""+ surrey_last_modified);
                        Log.d("inspection_last_modified", "none "+ inspections_last_modified);
                    }

                    callBack.onSuccess();
                        //downloadData(dataURL);

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



    public void updateInspections() {
        try {
            String fileName = rContext.getFilesDir() + "/"+ "inspections.csv" + "/" + "inspections.csv";
            InputStream fis = new FileInputStream(new File(fileName));
            BufferedReader inspectionReader = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8));
            Data inspectionDataUpdate = new Data( inspectionReader);
            inspectionDataUpdate.readUpdatedInspectionData();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void updateRestaurants() {
        try {
            String fileName = rContext.getFilesDir() + "/"+ "restaurants.csv" + "/" + "restaurants.csv";
            InputStream fis = new FileInputStream(new File(fileName));
            BufferedReader restaurantReader = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8));
            Data restaurantDataUpdate = new Data(restaurantReader);
            restaurantDataUpdate.readUpdatedRestaurantData();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

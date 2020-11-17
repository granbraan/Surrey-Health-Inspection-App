package com.example.poject_01.model;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;

public class DownloadRequest {
    private RequestQueue mQueue;
    private String url;
    private Context rContext;
    private String fileName;

    public DownloadRequest(String url, Context rContext, String fileName) {
        this.url = url;
        this.rContext = rContext;
        this.fileName = fileName;
    }


    private void downloadData(String downloadURL) {
        DownloadDataAsyncTask task = new DownloadDataAsyncTask(rContext, fileName);
        task.execute(downloadURL);

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
                    String testURL  =  data.getString("url");
                    String LAST_MODIFIED = data.getString("last_modified");
                    Log.d("Main Activity","URL:" + testURL);


                    // check if last_modified has been updated


                    LocalDateTime date = LocalDateTime.parse(LAST_MODIFIED);
                    LocalDateTime currentDate = LocalDateTime.now();



                    Log.d("Date Time", ""+currentDate);



                    downloadData(testURL);

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

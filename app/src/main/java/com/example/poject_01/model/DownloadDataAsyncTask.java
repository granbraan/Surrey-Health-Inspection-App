package com.example.poject_01.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.poject_01.UI.MainActivity;
import com.example.poject_01.UI.MapsActivity;
import com.example.poject_01.UI.WelcomeActivity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * This class downloads the data on a separate thread than the Main thread
 *
 */
public class DownloadDataAsyncTask extends AsyncTask<String, Integer, String> {

    private Context mContext;
    private String fileName;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;


    public DownloadDataAsyncTask(Context context, String fileName) {
        mContext = context;
        this.fileName = fileName;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(String... f_url) {
        int count;
        try {
            URL url = new URL(f_url[0]);
            URLConnection connection = url.openConnection();
            connection.connect();

            // input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            // created directory to store data
            File dir = new File(mContext.getFilesDir(), "restaurantData");
            if(!dir.exists()){
                dir.mkdir();
            }

            File dataFile = new File(dir, fileName);
            // Output stream to write file
            OutputStream output = new FileOutputStream(dataFile);

            Log.d( "The file path = ", dataFile.getAbsolutePath());
            byte[] data = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;

                // writing data to file
                output.write(data, 0, count);
            }
            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }
        return f_url[1];
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String dataSetCheck) {

        super.onPostExecute(dataSetCheck);
        // restaurant download
        prefs = mContext.getSharedPreferences("startup_logic" , Context.MODE_PRIVATE);
        editor = prefs.edit();
        int count = prefs.getInt("url_count", 0);
        if (count == 1 ) {
            if (Objects.equals(dataSetCheck, "0")) {
                updateRestaurants();
            }
            if (Objects.equals(dataSetCheck, "1")) {
                updateInspections();
            }
            editor.putInt("url_count", 0);
            editor.commit();
            Intent intent = MapsActivity.getIntent(WelcomeActivity.getContext());
            mContext.startActivity(intent);
            ((Activity)mContext).finish();

        }
        else if ( count == 2) {
            if (Objects.equals(dataSetCheck, "0")) {
                updateRestaurants();
            }
            if (Objects.equals(dataSetCheck, "1")) {
                updateInspections();
                editor.putInt("url_count", 0);
                editor.commit();
                Intent intent = MapsActivity.getIntent(WelcomeActivity.getContext());
                mContext.startActivity(intent);
                ((Activity)mContext).finish();

            }
        }

    }
    public void updateInspections() {
        try {
            String fileName = mContext.getFilesDir() + "/"+ "restaurantData" + "/" + "inspections.csv";
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
            String fileName = mContext.getFilesDir() + "/"+ "restaurantData" + "/" + "restaurants.csv";
            InputStream fis = new FileInputStream(new File(fileName));
            BufferedReader restaurantReader = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8));
            Data restaurantDataUpdate = new Data(restaurantReader);
            restaurantDataUpdate.readUpdatedRestaurantData();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

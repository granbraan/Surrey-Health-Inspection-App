package com.example.poject_01.model;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.poject_01.UI.MapsActivity;

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
            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
                //publishProgress(""+(int)((total*100)/lenghtOfFile));

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
        Log.d( "DownLoadAsyncTask", "Download from " + f_url[0] + " COMPLETE");
        return f_url[1];
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        Log.d("ptg", "onProgressUpdate: " + values[0]);

    }

    @Override
    protected void onPostExecute(String dataSetCheck) {
        super.onPostExecute(dataSetCheck);
        // restaurant download

        if (Objects.equals(dataSetCheck, "0")){
            updateRestaurants();
        }
        if (Objects.equals(dataSetCheck, "1")){
            updateInspections();
        }
    }
    public void updateInspections() {
        try {
            String fileName = MapsActivity.getContext().getFilesDir() + "/"+ "restaurantData" + "/" + "inspections.csv";
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
            String fileName = MapsActivity.getContext().getFilesDir() + "/"+ "restaurantData" + "/" + "restaurants.csv";
            InputStream fis = new FileInputStream(new File(fileName));
            BufferedReader restaurantReader = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8));
            Data restaurantDataUpdate = new Data(restaurantReader);
            restaurantDataUpdate.readUpdatedRestaurantData();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


}

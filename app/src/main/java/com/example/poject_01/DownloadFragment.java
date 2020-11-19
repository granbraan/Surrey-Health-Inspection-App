package com.example.poject_01;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.poject_01.UI.MapsActivity;
import com.example.poject_01.model.Data;
import com.example.poject_01.model.DownloadDataAsyncTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Date;


public class DownloadFragment extends AppCompatDialogFragment {
    private String restaurantsURL = "https://data.surrey.ca/api/3/action/package_show?id=restaurants";
    private String inspectionsURL = "https://data.surrey.ca/api/3/action/package_show?id=fraser-health-restaurant-inspection-reports";
    private SharedPreferences prefs;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //create the view
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.download_alert,null);

        //create button listener
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        Log.i("DownloadFragment Activity", "User clicked 'accept' button");
                        // TODO: download and update data
                        downloadData(restaurantsURL,"restaurants.csv");
                        downloadData(inspectionsURL,"inspections.csv");
                        updateInspections();
                        updateRestaurants();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        Log.i("DownloadFragment Activity", "User clicked 'decline' button");
                        //Do nothing - User chose not to download
                        break;
                }


            }
        };

        // TODO: extract strings
        //build alert dialog
        return new AlertDialog.Builder(getActivity())
                .setTitle("Update Available")
                .setView(v)
                .setPositiveButton("Yes", listener)
                .setNegativeButton("No", listener)
                .create();



    }
    public void downloadData(String downloadURL, String fileName) {
        DownloadDataAsyncTask task = new DownloadDataAsyncTask(MapsActivity.getContext(), fileName);
        task.execute(downloadURL);

        // updating preferences used to control flow of app
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("initial_update", true);
        Date currentDate = new Date(System.currentTimeMillis());
        editor.putLong("last_update", currentDate.getTime() );
        editor.commit();

    }

    public void updateInspections() {
        try {
            String fileName = MapsActivity.getContext().getFilesDir() + "/"+ "inspections.csv" + "/" + "inspections.csv";
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
            String fileName = MapsActivity.getContext().getFilesDir() + "/"+ "restaurants.csv" + "/" + "restaurants.csv";
            InputStream fis = new FileInputStream(new File(fileName));
            BufferedReader restaurantReader = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8));
            Data restaurantDataUpdate = new Data(restaurantReader);
            restaurantDataUpdate.readUpdatedRestaurantData();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

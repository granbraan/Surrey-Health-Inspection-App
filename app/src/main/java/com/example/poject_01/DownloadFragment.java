package com.example.poject_01;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.poject_01.UI.MainActivity;
import com.example.poject_01.UI.MapsActivity;
import com.example.poject_01.model.DownloadRequest;

import static android.content.Context.MODE_PRIVATE;


public class DownloadFragment extends AppCompatDialogFragment {
    AlertDialog.Builder builder;
    private SharedPreferences prefs;
    private DownloadRequest restaurantsDownload;
    private DownloadRequest inspectionsDownload;

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
                        //prefs =  MapsActivity.getContext().getSharedPreferences("startup_logic"   ,  MODE_PRIVATE);
                        //Toast.makeText(MapsActivity.getContext(), "User clicked download", Toast.LENGTH_LONG);
                        restaurantsDownload = ((MapsActivity)getActivity()).getRestaurantsRequest();
                        inspectionsDownload =((MapsActivity)getActivity()).getInspectionsRequest();

                        if (restaurantsDownload.dataModified()){
                            restaurantsDownload.downloadData();
                        }
                        if (inspectionsDownload.dataModified()){
                            inspectionsDownload.downloadData();
                        }
                        Log.d("DownloadFragment Activity", "User clicked 'accept' button");
                        Log.d("DownloadFragment Activity", "Restaurants Modified = " + restaurantsDownload.dataModified());
                        Log.d("DownloadFragment Activity", "Inspections Modified = " + inspectionsDownload.dataModified());
                        // TODO: download and update data


                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        Log.d("DownloadFragment Activity", "User clicked 'decline' button");
                        //Do nothing - User chose not to download
                        break;
                }


            }
        };


        //build alert dialog
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.update_alert_title)
                .setView(v)
                .setPositiveButton(R.string.update_alert_yes, listener)
                .setNegativeButton(R.string.update_alert_no, listener)
                .create();
    }


    private Dialog showUpdateDialog() {
        //create view to show
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.loading_dialog, null);
        builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("");
        builder.setCancelable(false);
        builder.setView(v);
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_NEGATIVE:
                        //Cancel Download
                        refreshActivity();
                        break;
                }
            }
        });
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.create();
        dialog.show();
        return dialog;
    }

    private void refreshActivity() {
        //Intent intent = new Intent(getContext(), MapsActivity.class);
        //startActivity(intent);
    }

}

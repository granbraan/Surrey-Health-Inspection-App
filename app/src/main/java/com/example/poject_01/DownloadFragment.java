package com.example.poject_01;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.poject_01.UI.MapsActivity;
import com.example.poject_01.model.DownloadRequest;

import java.util.Objects;

public class DownloadFragment extends AppCompatDialogFragment {


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //return super.onCreateDialog(savedInstanceState);

        //create the view
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.download_alert,null);

        //create button listener
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:

                        //call method
                        //DownloadRequest request = new DownloadRequest (String, Context, String);
                        //request.downloadData(URL);

                        //((MapsActivity) Objects.requireNonNull(getActivity())."download?? "());

                        break;
                    case DialogInterface.BUTTON_NEGATIVE:

                        //Do nothing
                        break;
                }
                //Log message
                Log.i("TAG", "You clicked");
            }
        };


        //build alert dialog
        return new AlertDialog.Builder(getActivity())
                .setTitle("Found Update")
                .setView(v)
                .setPositiveButton(android.R.string.ok, listener)
                .setNegativeButton(android.R.string.cancel, listener)
                .create();
    }




}

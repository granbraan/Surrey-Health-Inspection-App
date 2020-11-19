package com.example.poject_01;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;



public class DownloadFragment extends AppCompatDialogFragment {



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
}

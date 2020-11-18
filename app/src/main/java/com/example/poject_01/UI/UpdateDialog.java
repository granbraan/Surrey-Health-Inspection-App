package com.example.poject_01.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.poject_01.R;

public class UpdateDialog extends AppCompatDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //create view to show
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.loading_dialog,null);

        //create button listener
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("TAG", "CLICKED ON CANCEL");
                //TODO: load previous data
            }
        };
        //build alert dialog
        return new AlertDialog.Builder(getActivity()).setTitle(getString(R.string.updating))
                .setView(v)
                .setNegativeButton(android.R.string.no, listener)
                .setCancelable(false)
                .create();

    }
}

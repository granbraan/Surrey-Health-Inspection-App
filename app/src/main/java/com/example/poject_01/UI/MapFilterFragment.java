package com.example.poject_01.UI;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.poject_01.R;

public class MapFilterFragment extends AppCompatDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //create view 2 show
        @SuppressLint("InflateParams") View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.map_filter_layout, null);
        //button listener
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                    case DialogInterface.BUTTON_NEUTRAL:
                        resetUserInput();
                        break;

                }
            }
        };
        //build dialog
        AlertDialog alert = new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton(android.R.string.ok, listener)
                .setNegativeButton(android.R.string.cancel, listener)
                .setNeutralButton("Reset",listener)
                .create();
        alert.setCanceledOnTouchOutside(false);
        return alert;
    }

    private void getUserInput() {
        EditText name = getDialog().findViewById(R.id.filterName2);
        String name2 = name.getText().toString();

        EditText violations = getDialog().findViewById(R.id.filterViolations2);
        String violations2 = name.getText().toString();

        EditText hazard = getDialog().findViewById(R.id.filter_hazard_lvl2);
        String hazard2 = name.getText().toString();

    }

    private void resetUserInput() {
        EditText name = getDialog().findViewById(R.id.filterName2);
        name.setText("");

        EditText violations = getDialog().findViewById(R.id.filterViolations2);
        violations.setText("");


        EditText hazard = getDialog().findViewById(R.id.filter_hazard_lvl2);
        hazard.setText("");
    }
}

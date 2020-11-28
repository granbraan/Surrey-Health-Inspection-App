package com.example.poject_01.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.poject_01.R;

import static android.content.Context.MODE_PRIVATE;

public class ListFilterFragment extends AppCompatDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //create the view
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.filter_fragment_layout,null);

        //create button listener
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        getUserInput();

                        break;
                    case DialogInterface.BUTTON_NEGATIVE:

                        //Do nothing - User to cancel
                        break;
                    case DialogInterface.BUTTON_NEUTRAL:
                        ((MainActivity)getActivity()).restaurantAdapter.getFilter().filter("");
                        break;

                }
            }

        };


        //build alert dialog
        AlertDialog alertD =  new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton("Save", listener)
                .setNegativeButton("Cancel", listener)
                .setNeutralButton("Reset", listener)
                .create();
        alertD.setCanceledOnTouchOutside(false);
        return alertD;

    }

    private void getUserInput() {
        EditText editName = getDialog().findViewById(R.id.filterName);
        EditText editViolations = getActivity().findViewById(R.id.filterViolations);
        EditText editHazardLvl = getActivity().findViewById(R.id.filter_hazard_lvl);

        String inputName = editName.getText().toString();
        //String inputViolations =  editViolations.getText().toString();
        //String inputHazardLvl = editHazardLvl.getText().toString();

        ((MainActivity)getActivity()).restaurantAdapter.getFilter().filter(inputName);

    }

}

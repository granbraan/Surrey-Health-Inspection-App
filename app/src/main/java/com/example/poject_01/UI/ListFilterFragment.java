package com.example.poject_01.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.poject_01.R;

public class ListFilterFragment extends AppCompatDialogFragment {
    private Spinner spinner;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // create the view
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.filter_fragment_layout,null);

        // setup dropdown
        setupDropDown(v);

        // create button listener
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


        // build alert dialog
        AlertDialog alertD =  new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton("Save", listener)
                .setNegativeButton("Cancel", listener)
                .setNeutralButton("Reset", listener)
                .create();
        alertD.setCanceledOnTouchOutside(false);
        return alertD;

    }

    private void setupDropDown(View v) {
        spinner = v.findViewById(R.id.violationsDropdown);
        String[] items = new String[]{"Less", "More"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(v.getContext(), R.layout.filter_dropdown, items);
        spinner.setAdapter(adapter);
    }

    private void getUserInput() {
        EditText editHazardLvl = getDialog().findViewById(R.id.filter_hazard_level);
        EditText editViolations = getDialog().findViewById(R.id.filterViolations);
        EditText editName = getDialog().findViewById(R.id.filterName);

        String inputName = editName.getText().toString();
        String inputViolations =  editViolations.getText().toString();
        String inputHazardLvl = editHazardLvl.getText().toString();
        String inputBoolean = spinner.getSelectedItem().toString();

        String filterLump = inputName + "|" + inputHazardLvl +"|"+ inputViolations  +"|"+ inputBoolean;
        Log.d("filter fragment", "filter lump - " + filterLump);

        ((MainActivity)getActivity()).restaurantAdapter.getFilter().filter(filterLump);

    }

}

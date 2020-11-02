package com.example.poject_01.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.airbnb.lottie.animation.content.Content;
import com.example.poject_01.R;
import com.example.poject_01.model.Inspection;
import com.example.poject_01.model.InspectionList;
import com.example.poject_01.model.MyAdapter;

import java.util.ArrayList;
import java.util.List;

public class InspectionDetails extends AppCompatActivity {
    private static final String EXTRA_INDEX = "inspectionIndex";
    private Inspection curInspection;
    private int index;
    private static InspectionList instance = InspectionList.getInstance();

    public static Intent makeLaunchIntent(Context c, int index) {
        Intent intent = new Intent(c, InspectionDetails.class);
        intent.putExtra(EXTRA_INDEX, index);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection_details);

        populateListView();

    }

    private void populateListView() {
        //get list
        curInspection = instance.getInspectionIndex(index);


        //build adapter
        ArrayAdapter<Inspection> adapter = new ArrayAdapter<Inspection>(this,
                R.layout.inspection_content,
                instance.getInspections());
        //config list view
        ListView list = findViewById(R.id.inspectionListView);
        list.setAdapter(adapter);

    }

    private void extractDataFromIntent() {
        Intent intent = new Intent();
        index = intent.getIntExtra("EXTRA_INDEX", 0);
    }

}
package com.example.poject_01.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.animation.content.Content;
import com.example.poject_01.R;
import com.example.poject_01.model.Inspection;
import com.example.poject_01.model.InspectionList;
import com.example.poject_01.model.MyAdapter;
import com.example.poject_01.model.Restaurant;
import com.example.poject_01.model.RestaurantList;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InspectionDetails extends AppCompatActivity {
    private Inspection curInspection;
    private int index;
    private RestaurantList restaurantList = RestaurantList.getInstance();
    private int restaurantIndex;
    private static InspectionList instance = InspectionList.getInstance();

    public static Intent makeLaunchIntent(Context c, int index) {
        Intent intent =  new Intent(c,InspectionDetails.class);
        intent.putExtra("EXTRA_INDEX",index);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection_details);
        extractDataFromIntent();
        setText();
        //List Violations
        populateListView();
        registerClickCallBack();

    }

    private void populateListView() {
        curInspection = restaurantList.getRestaurantIndex(restaurantIndex).getInspections().getInspectionIndex(index);
        //build adapter
        ArrayAdapter<String> adapter = new myListAdapter();
        ListView list =  findViewById(R.id.inspectionListView);
        list.setAdapter(adapter);

    }
    private class myListAdapter extends ArrayAdapter<String> {
        //get list
        public myListAdapter() {
            super(InspectionDetails.this, R.layout.inspection_content, curInspection.getViolationList());
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            if(itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.inspection_content, parent, false);
            }
            //find violation to work with
            String curViolation = curInspection.getViolationList().get(position);
            String[] violations = curViolation.split(",");
            //fill view
            ImageView imageView = itemView.findViewById(R.id.violationItem);
            //Violation Types: Food, Equipment, pest/animal, other
            if(violations[2].toLowerCase().contains("sani")) {
                imageView.setImageResource(R.drawable.sanitary);
            }
            else if(violations[2].toLowerCase().contains("equipment")) {
                imageView.setImageResource(R.drawable.equipment);
            }
            else if(violations[2].toLowerCase().contains("food")) {
                imageView.setImageResource(R.drawable.food);
            }
            else if(violations[2].toLowerCase().contains("pest")) {
                imageView.setImageResource(R.drawable.pest);
            }
            else {
                imageView.setImageResource(R.drawable.clipboard);
            }
            // Violation Number
            TextView vioNumText = itemView.findViewById(R.id.violationNumber);
            vioNumText.setText(violations[0]);

            // Violation Type
            TextView criticalText = itemView.findViewById(R.id.violationType);
            criticalText.setText(violations[1]);

            // Icon for Critical/Non critical
            ImageView criticalIcon = itemView.findViewById(R.id.criticalImage);
            if(violations[1].toLowerCase().contains("not")) {
                criticalIcon.setImageResource(R.drawable.greenhazardicon);
            }
            else {
                criticalIcon.setImageResource(R.drawable.redhazardicon);
            }

            return itemView;
        }
    }
    private void registerClickCallBack() {
        ListView list = findViewById(R.id.inspectionListView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                String curViolation = curInspection.getViolationList().get(position);
                String[] violations = curViolation.split(",");
                String message = violations[2];
                Toast.makeText(InspectionDetails.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }



    private void extractDataFromIntent() {
        Intent intent = getIntent();
        index = intent.getIntExtra("EXTRA_INDEX", 0);
        restaurantIndex = intent.getIntExtra("REST_INDEX", 0);
    }

    private String refactorDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate inspectionDate = LocalDate.parse(date, formatter);
        return (inspectionDate.getMonth() + " " + inspectionDate.getDayOfMonth() + " " + inspectionDate.getYear());
    }

    private void setText() {
        curInspection = restaurantList.getRestaurantIndex(restaurantIndex).getInspections().getInspectionIndex(index);
        String date = refactorDate(String.valueOf(curInspection.getInspectionDate()));
        String numCritical = "Critical " +  curInspection.getNumCritical();
        String numNonCritical = "Non Critical " + curInspection.getNumNonCritical();
        String inspectionType = curInspection.getInspectionType();

        TextView textDate = (TextView)findViewById(R.id.inspectionDate);
        textDate.setText(date);

        TextView textCritical =(TextView) findViewById(R.id.inspectionCritical);
        textCritical.setText(numCritical);

        TextView textNonCritical =(TextView)findViewById(R.id.inspectionNonCritical);
        textNonCritical.setText(numNonCritical);

        TextView textInspectionType = (TextView) findViewById(R.id.inspectionType);
        textInspectionType.setText(inspectionType);

    }

}
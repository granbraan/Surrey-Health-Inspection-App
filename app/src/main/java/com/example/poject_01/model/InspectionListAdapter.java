package com.example.poject_01.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poject_01.R;
import com.example.poject_01.UI.InspectionDetailsActivity;
import com.example.poject_01.UI.MainActivity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;
/**
 * Adapter class to set the contents of recycler view to display each inspection of restaurant
 */
public class InspectionListAdapter extends RecyclerView.Adapter <InspectionListAdapter.MyViewHolder>{
    InspectionList inspectionList; //stores the inspection list of particular restaurant
    Context context;
    int restaurantIndex;
    private Restaurant restaurant;
    private List<Restaurant> restaurantList;


    //Constructor of the class
    public InspectionListAdapter(Context ct, InspectionList listOfInspection, int restIndex) {
        context = ct;
        inspectionList = listOfInspection;
        restaurantIndex = restIndex;
        restaurantList = MainActivity.getInstance().getFilteredList();
        restaurant = restaurantList.get(restIndex);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //creates the contents in the recycler view
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row_in_recycler_view,parent,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//instantiate the contents od recycler view to be displayed on UI
        Inspection inspection = inspectionList.getInspectionIndex(position);
        //date
        String d = "" + inspection.getInspectionDate();
        String date = refactorDate(d);
        holder.dateT.setText(String.format("Date - %s", date));

        //critical issues
        holder.critT.setText(context.getString(R.string.num_critical_restaurant)+ " " +inspection.getNumCritical());

        //non critical issues
        holder.nonCritT.setText(context.getString(R.string.num_non_critical_restaurant)+" " + inspection.getNumNonCritical());

        //hazard level
        holder.hazardT.setText(context.getString(R.string.hazard_lvl_restaurant)+ " " +inspection.getHazardRating());


        //image
        if(inspection.getHazardRating().equals("Low"))
        {
            holder.imageT.setImageResource(R.drawable.low_risk);
            holder.hazardT.setTextColor(Color.GREEN);
        }
        else if(inspection.getHazardRating().equals("Moderate"))
        {
            holder.imageT.setImageResource(R.drawable.medium_risk);
            holder.hazardT.setTextColor(Color.parseColor("#FF8800"));
        }
        else
        {
            holder.imageT.setImageResource(R.drawable.high_risk);
            holder.hazardT.setTextColor(Color.RED);
        }
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(restaurant.getInspections().getInspectionIndex(position).getNumNonCritical() + restaurant.getInspections().getInspectionIndex(position).getNumCritical() > 0) {
                    Intent intent = new Intent(context, InspectionDetailsActivity.class);
                    intent.putExtra("EXTRA_INDEX",position);
                    intent.putExtra("REST_INDEX",restaurantIndex);
                    context.startActivity(intent);
                }
            }
        });
    }

    //returns the total number of inspections
    @Override
    public int getItemCount() {
        return inspectionList.getNumInspections();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dateT, critT, nonCritT, hazardT;
        ImageView imageT;
        ConstraintLayout mainLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            dateT = itemView.findViewById(R.id.date_inspection);
            critT = itemView.findViewById(R.id.num_crit);
            nonCritT = itemView.findViewById(R.id.num_non_crit);
            hazardT = itemView.findViewById(R.id.hzrd_lvl);
            imageT = itemView.findViewById(R.id.imageView2);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }

    //reorganise the date in expected output
    private String refactorDate(String d) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate currentDate = LocalDate.now();
        LocalDate inspectionDate = LocalDate.parse(d, formatter);
        long difference = DAYS.between(inspectionDate, currentDate);

        Log.d("MainActivity", "current date - "+ currentDate);
        Log.d("MainActivity", "inspection date - "+ inspectionDate);
        Log.d("MainActivity", "difference = "+ difference);
        if (difference <= 30){
            return( difference + " days ago");
        }
        else if(difference <= 365){
            return("" + inspectionDate.getMonth() + " " + inspectionDate.getDayOfMonth() );
        }
        else {
            return("" + inspectionDate.getYear() + " "+ inspectionDate.getMonth() );
        }
    }
}

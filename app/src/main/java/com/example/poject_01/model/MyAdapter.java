package com.example.poject_01.model;

import android.content.Context;
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
import com.example.poject_01.UI.InspectionDetails;

import java.util.List;

public class MyAdapter  extends RecyclerView.Adapter <MyAdapter.MyViewHolder>{

    InspectionList inspection_list;
    Context context;
    public MyAdapter(Context ct, InspectionList list_of_inspection) {
        context = ct;
        inspection_list = list_of_inspection;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row_in_recycler_view,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.date_t.setText("Date - "+inspection_list.getInspectionIndex(position).getInspectionType());
        holder.crit_t.setText("Number of critical issues = "+ inspection_list.getInspectionIndex(position).getNumCritical());
        holder.non_crit_t.setText("Number of non critical issues = "+ inspection_list.getInspectionIndex(position).getNumNonCritical());
        holder.hazard_t.setText("Hazard level = "+inspection_list.getInspectionIndex(position).getHazardRating());

        if(inspection_list.getInspectionIndex(position).getHazardRating()=="Low")
        {
            holder.image_t.setImageResource(R.drawable.low_risk);
        }
        else if(inspection_list.getInspectionIndex(position).getHazardRating()=="Moderate")
        {
            holder.image_t.setImageResource(R.drawable.medium_risk);
        }
        else
        {
            holder.image_t.setImageResource(R.drawable.high_risk);
        }
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = InspectionDetails.makeLaunchIntent(MyAdapter.this, position);
                //startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return inspection_list.getNum_inspection();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView date_t, crit_t,non_crit_t,hazard_t;
        ImageView image_t;
        ConstraintLayout mainLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            date_t= itemView.findViewById(R.id.date);
            crit_t = itemView.findViewById(R.id.num_crit);
            non_crit_t = itemView.findViewById(R.id.num_non_crit);
            hazard_t = itemView.findViewById(R.id.hzrd_lvl);
            image_t = itemView.findViewById(R.id.imageView2);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}

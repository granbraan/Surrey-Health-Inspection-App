package com.example.poject_01.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.poject_01.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

public class RestaurantClusterRenderer extends DefaultClusterRenderer<RestaurantCluster> {
    private Context context;
    private IconGenerator generator;
    private ImageView imageView;
    GoogleMap googleMap;
    private float zoomLevel;
    private int markerImageId;

    public RestaurantClusterRenderer(Context context, GoogleMap map,
                                     ClusterManager<RestaurantCluster> clusterManager,
                                     float zoomLevel) {
        super(context, map, clusterManager);
        this.googleMap = googleMap;
        this.zoomLevel = zoomLevel;
        generator = new IconGenerator(context.getApplicationContext());
        imageView = new ImageView(context.getApplicationContext());
        int width =100;
        int height =100;
        imageView.setLayoutParams(new ViewGroup.LayoutParams(width,height));
        generator.setContentView(imageView);
    }


    @Override
    protected void onBeforeClusterItemRendered(@NonNull RestaurantCluster item, @NonNull MarkerOptions markerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions);
//        if(item.numInspections() > 0)
//            {
//                if(item.getLatestInspection().getHazardRating().equals("High"))
//                {
//                    markerImageId = R.drawable.red;
//                }
//                else if(item.getLatestInspection().getHazardRating().equals("Moderate"))
//                {
//                    markerImageId = R.drawable.yellow;
//                }
//                else {
//                    markerImageId = R.drawable.green;
//                }
//
//            }
//            else {
//                markerImageId = R.drawable.green;
//            }
            imageView.setImageResource(R.drawable.green);
        Bitmap icon = generator.makeIcon();
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
    }
}

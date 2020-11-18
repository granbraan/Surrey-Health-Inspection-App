package com.example.poject_01.UI;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.example.poject_01.R;
import com.example.poject_01.model.Data;
import com.example.poject_01.model.DownloadRequest;
import com.example.poject_01.model.Restaurant;
import com.example.poject_01.model.RestaurantCluster;
import com.example.poject_01.model.RestaurantList;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.clustering.ClusterManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private final RestaurantList restaurantList = RestaurantList.getInstance();
    Switch aSwitch;
    private FusedLocationProviderClient currentLocation;
    boolean permissionGranted = false;
    LocationRequest locationRequest;
    private Toolbar toolbar;
    private double lat;
    private double lng;
    private String restaurantTrack;
    private ClusterManager<RestaurantCluster> clusterManager;
    private String restaurantsURL = "https://data.surrey.ca/api/3/action/package_show?id=restaurants";
    private String inspectionsURL = "https://data.surrey.ca/api/3/action/package_show?id=fraser-health-restaurant-inspection-reports";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        toolbar = (Toolbar) findViewById(R.id.toolbar_map);
        toolbar.inflateMenu(R.menu.toggle_button);
        toolbar.setTitle("Maps");
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = MainActivity.makeIntent(MapsActivity.this);
                if(item.getItemId()==R.id.switch_list) {
                    startActivity(intent);
                    return  true;
                }
                else
                    return false;
            }
        });
        if(getIntent().getBooleanExtra("EXIT",false)) {
            finish();
        }
        SharedPreferences prefs = this.getSharedPreferences("startup_logic"   ,  MODE_PRIVATE);
        boolean initial_update = prefs.getBoolean("initial_update", false);
        if (!initial_update){
            readWriteInitialData();

        }
        else{
            updateRestaurants();
            updateInspections();
        }
        // comparing current time to last_update time
        Date currentDate = new Date(System.currentTimeMillis());
        Date last_update = new Date( prefs.getLong("last_update", 0));
        long diffInMillies = currentDate.getTime() - last_update.getTime();
        long diffInHours =  TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        Log.d("Date - Last Update",""+ last_update);
        Log.d("Date - Current",""+ currentDate);
        Log.d("Difference in Hours:", "" +diffInHours);
        // 20 hours since last update
        if (diffInHours >= 20) {
            DownloadRequest restaurants = new DownloadRequest(restaurantsURL, MapsActivity.this, "restaurants.csv");
            DownloadRequest inspections = new DownloadRequest(inspectionsURL, MapsActivity.this, "inspections.csv");
            restaurants.getURL();
            inspections.getURL();

            //TODO: if user chooses to download data, update restaurants and inspections.

            //updateRestaurants();
            //updateInspections();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        currentLocation = LocationServices.getFusedLocationProviderClient(this);
        createLocationRequest();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            checkSettingAndStartLocationUpdates();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 12345);
        }

    }

    public void updateInspections() {
        try {
            String fileName = this.getFilesDir() + "/"+ "inspections.csv" + "/" + "inspections.csv";
            InputStream fis = new FileInputStream(new File(fileName));
            BufferedReader inspectionReader = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8));
            Data inspectionDataUpdate = new Data( inspectionReader);
            inspectionDataUpdate.readUpdatedInspectionData();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void updateRestaurants() {
        try {
            String fileName = this.getFilesDir() + "/"+ "restaurants.csv" + "/" + "restaurants.csv";
            InputStream fis = new FileInputStream(new File(fileName));
            BufferedReader restaurantReader = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8));
            Data restaurantDataUpdate = new Data(restaurantReader);
            restaurantDataUpdate.readUpdatedRestaurantData();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        setUpCluster();
//        for (int i = 0; i < restaurantList.getRestaurantListSize(); i++) {
//            double latitude = restaurantList.getRestaurantIndex(i).getLatitude();
//            double longitude = restaurantList.getRestaurantIndex(i).getLongitude();
//            LatLng location = new LatLng(latitude, longitude);
//            int markerImageId;
//            String hazardRating = "Low";
//            if(restaurantList.getRestaurantIndex(i).numInspections() > 0)
//            {
//                if(restaurantList.getRestaurantIndex(i).getLatestInspection().getHazardRating().equals("High"))
//                {
//                    markerImageId = R.drawable.red;
//                    hazardRating = "High";
//                }
//                else if(restaurantList.getRestaurantIndex(i).getLatestInspection().getHazardRating().equals("Moderate"))
//                {
//                    markerImageId = R.drawable.yellow;
//                    hazardRating = "Moderate";
//                }
//                else {
//                    markerImageId = R.drawable.green;
//                    hazardRating = "Low";
//                }
//
//            }
//            else {
//                markerImageId = R.drawable.green;
//            }
//            String name = restaurantList.getRestaurantIndex(i).getName();
//            String address = restaurantList.getRestaurantIndex(i).getAddress();
//            restaurantTrack = restaurantList.getRestaurantIndex(i).getTrackingNum();
//            mMap.addMarker(new MarkerOptions().position(location).title(name).snippet(address + " Hazard Rating: "+ hazardRating).icon(BitmapDescriptorFactory.fromResource(markerImageId)));
//        }
        setUpCluster();
        getDeviceLocation();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnInfoWindowClickListener(this);
    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
        }
    };

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        currentLocation.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    @Override
    protected void onStart() {
        super.onStart();
        startLocationUpdates();
    }

    @Override
    protected void onStop() {
        super.onStop();
        currentLocation.removeLocationUpdates(locationCallback);
    }
    private void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void readWriteInitialData() {
        // reads data from data_restaurants.csv
        InputStream restaurantStream = getResources().openRawResource(R.raw.data_restaurants);
        BufferedReader restaurantReader = new BufferedReader(new InputStreamReader(restaurantStream, StandardCharsets.UTF_8));
        // reads data from data_inspections.csv
        InputStream inspectionStream = getResources().openRawResource(R.raw.data_inspections);
        BufferedReader inspectionReader = new BufferedReader(new InputStreamReader(inspectionStream, StandardCharsets.UTF_8));
        // the data is set using private setters in the Data class
        Data restaurantData = new Data( restaurantReader );
        Data inspectionData = new Data( inspectionReader);
        restaurantData.readRestaurantData();
        inspectionData.readInspectionData();
    }

    private void getDeviceLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(this,permissions,12345);
            return;
        }
        Task location = LocationServices.getFusedLocationProviderClient(this).getLastLocation();
        location.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                lat = location.getLatitude();
                lng = location.getLongitude();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));

                if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mMap.setMyLocationEnabled(true);
            }
        });
    }

    private void checkSettingAndStartLocationUpdates() {
        LocationSettingsRequest request = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build();
        SettingsClient client = LocationServices.getSettingsClient(this);

        Task<LocationSettingsResponse> locationSettingsResponseTask = client.checkLocationSettings(request);
        locationSettingsResponseTask.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // Starts location updates
                if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                currentLocation.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
            }
        });
    }

    private void moveCamera (LatLng latLng, float zoom)
    {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void setUpCluster() {
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng),10));

        clusterManager = new ClusterManager<RestaurantCluster>(this,mMap);
        addItems();
        for(int i=0; i<restaurantList.getRestaurantListSize();i++)
        {
            Restaurant r = restaurantList.getRestaurantIndex(i);
            LatLng coordinates = new LatLng(restaurantList.getRestaurantIndex(i).getLatitude(), restaurantList.getRestaurantIndex(i).getLongitude());
            moveCamera(coordinates, 15f);
        }

        clusterManager.setOnClusterItemInfoWindowClickListener(new ClusterManager.OnClusterItemInfoWindowClickListener<RestaurantCluster>() {
            @Override
            public void onClusterItemInfoWindowClick(RestaurantCluster item) {
                for(int i=0; i<restaurantList.getRestaurantListSize();i++)
                {
                    Restaurant r = restaurantList.getRestaurantIndex(i);
                    LatLng coordinates = new LatLng(restaurantList.getRestaurantIndex(i).getLatitude(), restaurantList.getRestaurantIndex(i).getLongitude());
                    moveCamera(coordinates, 15f);
                    if(item.getPosition().equals(coordinates))
                    {
                        Intent intent = RestaurantDetailsActivity.makeIntent(MapsActivity.this, i);
                        startActivity(intent);
                    }
                }
            }
        });
        mMap.setOnCameraIdleListener(clusterManager);
        mMap.setOnMarkerClickListener(clusterManager);
    }

    private void addItems() {
        for(int i =0 ; i<restaurantList.getRestaurantListSize();i++)
        {
            Restaurant r = restaurantList.getRestaurantIndex(i);
            double latitude = restaurantList.getRestaurantIndex(i).getLatitude();
            double longitude = restaurantList.getRestaurantIndex(i).getLongitude();
            RestaurantCluster cluster;
            if(restaurantList.getRestaurantIndex(i).numInspections() > 0)
                 cluster = new RestaurantCluster(latitude,longitude,restaurantList.getRestaurantIndex(i).getName(),
                    restaurantList.getRestaurantIndex(i).getAddress() +","+ restaurantList.getRestaurantIndex(i).getCity()+
                            "  HAZARD RATING - "+restaurantList.getRestaurantIndex(i).getLatestInspection().getHazardRating());
            else
                cluster = new RestaurantCluster(latitude,longitude,restaurantList.getRestaurantIndex(i).getName(),
                        restaurantList.getRestaurantIndex(i).getAddress()+","+restaurantList.getRestaurantIndex(i).getCity()+" \"NO INSPECTIONS YET\" ");

            clusterManager.addItem(cluster);

        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        int index = 0;
        Log.i("MARKER TITLE", marker.getTitle());
        for(int i = 0; i < restaurantList.getRestaurantListSize(); i++) {
            Log.i("MARKER TITLE", marker.getTitle());
            if(marker.getTitle().equals(restaurantList.getRestaurantIndex(i).getName())) {
                index = i;
            }
        }
        Intent intent = RestaurantDetailsActivity.makeIntent(MapsActivity.this, index);
        startActivity(intent);
    }


}
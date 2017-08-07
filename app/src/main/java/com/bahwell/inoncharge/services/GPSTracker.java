package com.bahwell.inoncharge.services;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import com.bahwell.inoncharge.other.SessionManagement;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.core.GeoHash;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.security.AccessController.getContext;

public class GPSTracker extends Service implements LocationListener{

    private Context context;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    private DatabaseReference mDatabase;
    private Location location;
    double latitude,longitude;
    SessionManagement session;

    LocationManager locationManager;
    //    AlertDialogManager am = new AlertDialogManager();
    public GPSTracker(Context context){
        this.context = context;
        session = new SessionManagement(context);
        getLocation();
    }
    public GPSTracker(){
        super();
    }
    private Location getLocation() {
        // TODO Auto-generated method stub


        mDatabase = FirebaseDatabase.getInstance().getReference();
        try{
            locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


            if (!isGPSEnabled && !isNetworkEnabled){

            } else {
                this.canGetLocation = true;

                if (isNetworkEnabled){

                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 3, this);

                    if (locationManager != null){
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null){
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }

                if (isGPSEnabled){
                    if (location == null){
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 3, this);
                        if (locationManager != null){
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null){
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                } else {
                    showAlertDialog();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return location;
    }
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(GPSTracker.this);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                dialog.cancel();
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
    public void showAlertDialog(){
//        am.showAlertDialog(GPSTracker.this, "GPS Setting", "Gps is not enabled. Do you want to enabled it ?", false);
    }
    public double getLatitude(){
        if (location != null){
            latitude = location.getLatitude();
        }

        return latitude;
    }
    public double getLongitude(){
        if (location != null){
            longitude = location.getLongitude();
        }

        return longitude;
    }
    public boolean canGetLocation(){
        return this.canGetLocation;
    }
    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        HashMap<String, String> user = session.getUserDetails();
        if (location != null){
            this.location = location;
            GeoHash geoHash = new GeoHash(new GeoLocation(location.getLatitude(),location.getLongitude()));
            Map<String, Object> updates = new HashMap<>();
//            updates.put("merchant/HpnbwznvN2fCb21ijZQd0XgHrfy2/g",geoHash.getGeoHashString());
//            updates.put("merchant/HpnbwznvN2fCb21ijZQd0XgHrfy2/l", Arrays.asList(location.getLatitude(),location.getLongitude()));
            // ttt ganti user Id coyyy
            if (user.get(SessionManagement.KEY_STATUS).equals("merchant")) {
                updates.put("merchants/" + user.get(SessionManagement.KEY_ID) + "/g", geoHash.getGeoHashString());
                updates.put("merchants/" + user.get(SessionManagement.KEY_ID) + "/l", Arrays.asList(location.getLatitude(), location.getLongitude()));
            }
//            updates.put("merchant/ll/g",geoHash.getGeoHashString());
//            updates.put("merchant/ll/l", Arrays.asList(location.getLatitude(),location.getLongitude()));
            mDatabase.updateChildren(updates);
        }
    }
    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }
    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
}
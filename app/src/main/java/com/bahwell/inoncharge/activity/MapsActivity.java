package com.bahwell.inoncharge.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bahwell.inoncharge.R;
import com.bahwell.inoncharge.other.SessionManagement;
import com.bahwell.inoncharge.other.Transaksi;
import com.bahwell.inoncharge.other.User;
import com.bahwell.inoncharge.services.GPSTracker;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.geofire.core.GeoHash;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MapsActivity  extends FragmentActivity implements
        GeoQueryEventListener,
        GoogleMap.OnCameraChangeListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener{

    private static final GeoLocation INITIAL_CENTER = new GeoLocation(37.7789, -122.4017);
    private static final int INITIAL_ZOOM_LEVEL = 14;
    private static final String GEO_FIRE_DB = "https://inon-charge.firebaseio.com";
    private static final String GEO_FIRE_REF = GEO_FIRE_DB + "/merchants";

    private GoogleMap map;
    private Marker markertemu;
    private Circle searchCircle;
    private GeoFire geoFire;
    private GeoQuery geoQuery;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    //ttt    SessionManagement session;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private Map<String,Marker> markers;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Marker mCurrLocationMarker, markermy;
    private LocationRequest mLocationRequest;
    private String key = "";
    FirebaseApp app;
    BitmapDescriptor icon, icon_mark, icon_my;
    boolean myMarkForTitikTemu;

    GPSTracker gps;
    double mylatitude;
    double mylongitude;
    double temulatitude;
    double temulongitude;
    LatLng mylatLng;

    int  _TOTAL_HARGA;
    String jamAwal,jamAhir, _date_awal, _date_ahir, _date, _time_awal, _time_ahir;
    int selisih_waktu_tmp, totalHarga, _HARGA_TOTAL;
    boolean jamOrHari, _hari_or_jam;
    String _ID, _NAME, _TITLE, _HARGA, _TOKEN, _ID_TRANSAKSI;

    SessionManagement sessionManagement;
    HashMap<String, String> user_my;


    public void setMyMarkForTitikTemu(boolean b){
        this.myMarkForTitikTemu = b;
    }

    public boolean getMyMarkForTitikTemu(){
        return this.myMarkForTitikTemu;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        try {
            mGoogleApiClient =  new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            mGoogleApiClient.connect();
            map.setMyLocationEnabled(true);
            map.getUiSettings().setZoomControlsEnabled(true);
            map.getUiSettings().setCompassEnabled(true);
            map.getUiSettings().setIndoorLevelPickerEnabled(true);
            map.setBuildingsEnabled(true);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        // setup map and camera position
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setDatabaseUrl(GEO_FIRE_DB)
                .setApiKey("AIzaSyCzXG0LOg9biUHUOmhPURW92jyrHY7LLQ0")
                .setApplicationId("1:1083404626851:android:f4d8167c9d6fc0e9").build();

        app = FirebaseApp.initializeApp(getApplicationContext(), options, createName());
        // setup GeoFire
        this.geoFire = new GeoFire(FirebaseDatabase.getInstance(app).getReferenceFromUrl(GEO_FIRE_REF));
        // radius in km
        this.geoQuery = this.geoFire.queryAtLocation(INITIAL_CENTER, 1);
        // setup markers
        this.markers = new HashMap<String, Marker>();
        mDatabase = FirebaseDatabase.getInstance().getReference();


    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }
    @Override
    protected void onStop() {
        super.onStop();
        // remove all event listeners to stop updating in the background
        this.geoQuery.removeAllListeners();
        for (Marker marker: this.markers.values()) {
            marker.remove();
        }
        this.markers.clear();
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.geoQuery.addGeoQueryEventListener(this);
    }

    @Override
    public void onKeyEntered(String key, GeoLocation location) {

        gps = new GPSTracker(getBaseContext());
        mylatitude = gps.getLatitude();
        mylongitude= gps.getLongitude();

        if (mylatitude != location.latitude && mylongitude != location.longitude){
            // Add a new marker to the map
            Marker marker = this.map.addMarker(
                    new MarkerOptions()
                            .position(new LatLng(location.latitude, location.longitude))
                            .title("Merchant")
                            .icon(icon)
            );
            this.markers.put(key, marker);
        }
    }

    @Override
    public void onKeyExited(String key) {
        // Remove any old marker
        Marker marker = this.markers.get(key);
        if (marker != null) {
            marker.remove();
            this.markers.remove(key);
        }
    }

    @Override
    public void onKeyMoved(String key, GeoLocation location) {
        // Move the marker
        Marker marker = this.markers.get(key);
        if (marker != null) {
            this.animateMarkerTo(marker, location.latitude, location.longitude);
        }
    }

    @Override
    public void onGeoQueryReady() {
    }

    @Override
    public void onGeoQueryError(DatabaseError error) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("There was an unexpected error querying GeoFire: " + error.getMessage())
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    // Animation handler for old APIs without animation support
    private void animateMarkerTo(final Marker marker, final double lat, final double lng) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final long DURATION_MS = 3000;
        final Interpolator interpolator = new AccelerateDecelerateInterpolator();
        final LatLng startPosition = marker.getPosition();
        handler.post(new Runnable() {
            @Override
            public void run() {
                float elapsed = SystemClock.uptimeMillis() - start;
                float t = elapsed/DURATION_MS;
                float v = interpolator.getInterpolation(t);

                double currentLat = (lat - startPosition.latitude) * v + startPosition.latitude;
                double currentLng = (lng - startPosition.longitude) * v + startPosition.longitude;
                marker.setPosition(new LatLng(currentLat, currentLng));

                // if animation is not finished yet, repeat
                if (t < 1) {
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    private double zoomLevelToRadius(double zoomLevel) {
        // Approximation to fit circle into view
        return 16384000/Math.pow(2, zoomLevel);
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        // Update the search criteria for this geoQuery and the circle on the map
        LatLng center = cameraPosition.target;
        double radius = zoomLevelToRadius(cameraPosition.zoom);
        this.searchCircle.setCenter(center);
        this.searchCircle.setRadius(radius);
        this.geoQuery.setCenter(new GeoLocation(center.latitude, center.longitude));
        // radius in km
        this.geoQuery.setRadius(radius/600);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        //Initialize Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();

            }
        }
        else {
            buildGoogleApiClient();
            map.setMyLocationEnabled(true);
        }

        getMyLocation();

        LatLng latLngCenter = new LatLng(INITIAL_CENTER.latitude, INITIAL_CENTER.longitude);
        this.searchCircle = this.map.addCircle(new CircleOptions().center(latLngCenter).radius(1000));
        MapsActivity.this.searchCircle.setVisible(false);
        this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngCenter, 16));
        this.map.setOnCameraChangeListener(this);

        //session
        sessionManagement = new SessionManagement(getApplicationContext());
        user_my = sessionManagement.getUserDetails();

        //icon
        icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_action_user_location_icon);
        icon_mark = BitmapDescriptorFactory.fromResource(R.drawable.ic_action_mark_location_icon);
        icon_my = BitmapDescriptorFactory.fromResource(R.drawable.ic_action_myposition_icon);

        //pasing data inten
        _ID = getIntent().getExtras().getString("_ID");
        _NAME = getIntent().getExtras().getString("_NAME");
        _TITLE = getIntent().getExtras().getString("_TITLE");
        _HARGA = getIntent().getExtras().getString("_HARGA");
        _TOTAL_HARGA = getIntent().getExtras().getInt("_HARGA_TOTAL");
        _TOKEN = getIntent().getExtras().getString("_TOKEN");
        _hari_or_jam = getIntent().getExtras().getBoolean("_hariOrJam");
        if (_hari_or_jam){
            _date_awal = getIntent().getExtras().getString("_date_awal");
            _date_ahir = getIntent().getExtras().getString("_date_ahir");

            _time_awal = "07:00";
            _time_ahir = "15:00";
        }else {
            _date = getIntent().getExtras().getString("_date");
            _time_awal = getIntent().getExtras().getString("_time_awal");
            _time_ahir = getIntent().getExtras().getString("_time_ahir");

            _date_awal = _date;
            _date_ahir = _date;
        }

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                // TODO Auto-generated method stub
                if (markertemu != null){
                    markertemu.remove();
                }

                LatLng latLngCenter = new LatLng(point.latitude,point.longitude);
                MapsActivity.this.searchCircle.setCenter(latLngCenter);
                MapsActivity.this.searchCircle = map.addCircle(new CircleOptions().center(latLngCenter));
                MapsActivity.this.searchCircle.setVisible(false);
                map.setOnCameraChangeListener(MapsActivity.this);

                temulatitude = point.latitude;
                temulongitude = point.longitude;

                markertemu = map.addMarker(
                        new MarkerOptions()
                                .position(new LatLng(point.latitude, point.longitude))
                                .title("Ketemu disini")
                                .icon(icon_mark)
                );

            }
        });

        ImageView imgMyLocation;
        imgMyLocation = (ImageView) findViewById(R.id.imgMyLocation);
        imgMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMyLocation();
            }
        });

        Button btn_confirm_order;
        btn_confirm_order = (Button) findViewById(R.id.btn_confirm_order);
        btn_confirm_order.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v){

                saveTransaksiOnFirebase(
                        Double.toString(temulongitude),
                        Double.toString(temulatitude),
                        Integer.toString(_TOTAL_HARGA),
                        _TITLE,
                        nowOrBoking(),
                        _date_awal,
                        _date_ahir,
                        _time_awal,
                        _time_ahir
                );

                sendNotification(_TOKEN);
                Intent intent = new Intent(MapsActivity.this, WaitingOrderActivity.class);
                intent.putExtra("_ID_MY", user_my.get(SessionManagement.KEY_ID));
                intent.putExtra("_ID_MERCHANT", _ID);
                intent.putExtra("_ID_TRANSAKSI", _ID_TRANSAKSI);
                startActivity(intent);

            }
        });

    }

    private String nowOrBoking(){
        if (_hari_or_jam){
            return "boking";
        }else {
            return "now";
        }
    }

    private void saveTransaksiOnFirebase(
            String lokasi_log,
            String lokasi_lat,
            String harga_total,
            String jenis_jasa,
            String now_or_boking,
            String date_awal,
            String date_ahir,
            String time_awal,
            String time_ahir
    ) {
        Transaksi transaksi = new Transaksi(
                lokasi_log,
                lokasi_lat,
                harga_total,
                jenis_jasa,
                now_or_boking,
                date_awal,
                date_ahir,
                time_awal,
                time_ahir,
                "menunggu"
        );

        _ID_TRANSAKSI = createTransaksiID();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("transaksi")
                .child("user")
                .child(user_my.get(SessionManagement.KEY_ID))
                .child(_ID)
                .child(_ID_TRANSAKSI)
                .setValue(transaksi);
    }

    public String createTransaksiID(){
        Date now = new Date();
        String id = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss",  Locale.US).format(now);
        return id;
    }


    private void getMyLocation() {
        gps = new GPSTracker(getBaseContext());
        mylatitude = gps.getLatitude();
        mylongitude= gps.getLongitude();

        temulatitude = gps.getLatitude();
        temulongitude = gps.getLongitude();

        mylatLng = new LatLng(mylatitude, mylongitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mylatLng, 18);
        map.animateCamera(cameraUpdate);

        if (markertemu != null){
            markertemu.remove();
        }

        if (markermy != null){
            markermy.remove();
        }

        markermy = map.addMarker(
                new MarkerOptions()
                        .position(mylatLng)
                        .title("me")
                        .icon(icon_my)
        );
    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation( mGoogleApiClient );
        LatLng ll = null;
        if (mLastLocation == null){
            ll = new LatLng(INITIAL_CENTER.latitude,INITIAL_CENTER.longitude);
            map.moveCamera(CameraUpdateFactory.newLatLng(ll));
        }else {
            ll = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
            map.moveCamera(CameraUpdateFactory.newLatLng(ll));
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = map.addMarker(markerOptions);

        //move map camera
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.zoomTo(11));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        map.setMyLocationEnabled(true);
                    }
                } else {
                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }

    public String createName(){
        Date now = new Date();
        String id = new SimpleDateFormat("ddHHmmss",  Locale.US).format(now);
        return id;
    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private void sendNotification(final String reg_token) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    OkHttpClient client = new OkHttpClient();
                    JSONObject json = new JSONObject();
                    JSONObject dataJson = new JSONObject();
                    JSONObject data = new JSONObject();

                    data.put("_ID_TRANSAKSI", _ID_TRANSAKSI);

                    data.put("_ID_MY", _ID);
                    data.put("_NAME_MY", _NAME);
                    data.put("_TOKEN_MY", user_my.get(SessionManagement.KEY_TOKEN));

                    data.put("_ID", user_my.get(SessionManagement.KEY_ID));
                    data.put("_NAME", user_my.get(SessionManagement.KEY_NAME));
                    data.put("_TITLE", _TITLE);

                    data.put("_TEMU_LOT", temulatitude);
                    data.put("_TEMU_LONG", temulongitude);

                    data.put("_TOTAL_HARGA", _TOTAL_HARGA);
                    data.put("_hari_or_jam", _hari_or_jam);
                    if (_hari_or_jam) {
                        data.put("_date_awal", _date_awal);
                        data.put("_date_ahir", _date_ahir);
                    } else {
                        data.put("_date", _date);
                        data.put("_time_awal", _time_awal);
                        data.put("_time_ahir", _time_ahir);
                    }

                    dataJson.put("body", user_my.get(SessionManagement.KEY_NAME) + " ingin memboking anda");
                    dataJson.put("click_action","OPEN_ACTIVITY_1");
                    dataJson.put("title", _TITLE);

                    json.put("notification", dataJson);
                    json.put("data", data);
                    json.put("to", reg_token);


                    RequestBody body = RequestBody.create(JSON, json.toString());
                    Request request = new Request.Builder()
                            .header("Authorization", "key=AIzaSyBX75fFT_2RvmG-WQh1GfckLICcWipqRc8")
                            .url("https://fcm.googleapis.com/fcm/send")
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    String finalResponse = response.body().string();
                } catch (Exception e) {
                    Log.d("send notf", e.getMessage());
                }
                return null;
            }
        }.execute();
    }
}
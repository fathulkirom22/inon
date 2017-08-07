package com.bahwell.inoncharge.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.bahwell.inoncharge.R;
import com.bahwell.inoncharge.fragment.ChatFragment;
import com.bahwell.inoncharge.fragment.DiscountFragment;
import com.bahwell.inoncharge.fragment.HistoryFragment;
import com.bahwell.inoncharge.fragment.HomeFragment;
import com.bahwell.inoncharge.other.Merchant;
import com.bahwell.inoncharge.other.SessionManagement;
import com.bahwell.inoncharge.other.SessionMerchant;
import com.bahwell.inoncharge.other.TempData;
import com.bahwell.inoncharge.other.User;
import com.bahwell.inoncharge.other.lib;
import com.bahwell.inoncharge.services.GPSTracker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private String _ID="";

    public Bitmap tmpPhotoProfil;

    private lib l = new lib(this);

    SessionManagement session;
    SessionMerchant sMerchant;

    @Override
    public void onNewIntent(Intent intents){
//        if (getIntent().getExtras() != null) {
//            for (String key : getIntent().getExtras().keySet()) {
//                String value = getIntent().getExtras().getString(key);
//                if (key.equals("AnotherActivity") && value.equals("True")) {
//                    Intent intents = new Intent(this, OrderConfirmationActivity.class);
//                    intents.putExtra("value", value);
//                    startActivity(intents);
//                    finish();
//                }
//
//            }
//        }

        if (intents.getExtras() != null) {
            Intent intent = new Intent(this, OrderConfirmationActivity.class);
            intent.putExtra("AnotherActivity", "True");
            intent.putExtra("_ID", intents.getExtras().getString("_ID"));
            intent.putExtra("_NAME", intents.getExtras().getString("_NAME"));
            intent.putExtra("_TOTAL_HARGA", intents.getExtras().getString("_TOTAL_HARGA"));
            intent.putExtra("_TITLE", intents.getExtras().getString("_TITLE"));
            intent.putExtra("_hari_or_jam", intents.getExtras().getString("_hari_or_jam"));
            if (intents.getExtras().getString("_hari_or_jam").equals("true")){
                intent.putExtra("_date_awal", intents.getExtras().getString("_date_awal"));
                intent.putExtra("_date_ahir", intents.getExtras().getString("_date_ahir"));
            }
            else if (intents.getExtras().getString("_hari_or_jam").equals("false")){
                intent.putExtra("_date", intents.getExtras().getString("_date"));
                intent.putExtra("_time_awal", intents.getExtras().getString("_time_awal"));
                intent.putExtra("_time_ahir", intents.getExtras().getString("_time_ahir"));
            }
            startActivity(intents);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GPSTracker gps = new GPSTracker(this);

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };


        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();


        //get current user
        user = FirebaseAuth.getInstance().getCurrentUser();


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // Session class instance
        session = new SessionManagement(getApplicationContext());
        sMerchant = new SessionMerchant(getApplicationContext());

        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                startActivity(new Intent(MainActivity.this, NotificationActivity.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View nav = navigationView.getHeaderView(0);

        final TextView emailUserView = (TextView) nav.findViewById(R.id.nav_view_email);
        final TextView statusUserView = (TextView) nav.findViewById(R.id.nav_view_status);
        final TextView nameUserView = (TextView) nav.findViewById(R.id.nav_view_name);
        final TextView phoneUserView = (TextView) nav.findViewById(R.id.nav_view_phone);
        final TextView addressUserView = (TextView) nav.findViewById(R.id.nav_view_address);

        MenuItem nav_home = (MenuItem) nav.findViewById(R.id.nav_home);
        MenuItem nav_discont = (MenuItem) nav.findViewById(R.id.nav_discont);
        MenuItem nav_history = (MenuItem) nav.findViewById(R.id.nav_history);
        MenuItem nav_portofolio = (MenuItem) nav.findViewById(R.id.nav_portofolio);
        MenuItem nav_promotion = (MenuItem) nav.findViewById(R.id.nav_promotion);
        MenuItem nav_messeg = (MenuItem) nav.findViewById(R.id.nav_messeg);
        MenuItem nav_about = (MenuItem) nav.findViewById(R.id.nav_about);
        MenuItem nav_add_kategori = (MenuItem) nav.findViewById(R.id.nav_add_kategori);
        MenuItem nav_logout = (MenuItem) nav.findViewById(R.id.nav_logout);
        MenuItem nav_upgrade_merchant = (MenuItem) nav.findViewById(R.id.nav_upgrade_merchant);

        de.hdodenhof.circleimageview.CircleImageView pp = (de.hdodenhof.circleimageview.CircleImageView) nav.findViewById(R.id.imageView);

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        nameUserView.setText(user.get(SessionManagement.KEY_NAME));
        emailUserView.setText(user.get(SessionManagement.KEY_EMAIL));
        phoneUserView.setText(user.get(SessionManagement.KEY_PHONE));
        addressUserView.setText(user.get(SessionManagement.KEY_ADDRESS));
        statusUserView.setText("Status : "+user.get(SessionManagement.KEY_STATUS));
        pp.setImageBitmap(new lib(this).getPhotoProfilToStorage());

        Menu menu = navigationView.getMenu();
        if (user.get(SessionManagement.KEY_STATUS).equals("merchant")){
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("merchants").child(user.get(SessionManagement.KEY_ID)).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                            // Get user value
                            Merchant merchant = dataSnapshot.getValue(Merchant.class);
//                            TempData.NO_BANK_ACCOUNT = merchant.NO_BANK_ACCOUNT;
//                            TempData.NO_KTP = merchant.NO_KTP;
//                            TempData.NO_PHONE_RELATIVE = merchant.NO_PHONE_RELATIVE;
//                            TempData.NO_KTP_RELATIVE = merchant.NO_KTP_RELATIVE;
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    }
            );

            for (int menuItemIndex = 0; menuItemIndex < menu.size(); menuItemIndex++) {
                MenuItem menuItem= menu.getItem(menuItemIndex);
                if(menuItem.getItemId() == R.id.nav_messeg){
                    menuItem.setEnabled(true);
                    menuItem.setVisible(true);
                }
                if(menuItem.getItemId() == R.id.nav_portofolio){
                    menuItem.setEnabled(true);
                    menuItem.setVisible(true);
                }
                if(menuItem.getItemId() == R.id.nav_promotion){
                    menuItem.setEnabled(true);
                    menuItem.setVisible(true);
                }
                if(menuItem.getItemId() == R.id.nav_add_kategori){
                    menuItem.setEnabled(true);
                    menuItem.setVisible(true);
                }
                if(menuItem.getItemId() == R.id.nav_upgrade_merchant){
                    menuItem.setEnabled(false);
                    menuItem.setVisible(false);
                }
            }
        } else {
            for (int menuItemIndex = 0; menuItemIndex < menu.size(); menuItemIndex++) {
                MenuItem menuItem= menu.getItem(menuItemIndex);
                if(menuItem.getItemId() == R.id.nav_messeg){
                    menuItem.setEnabled(true);
                    menuItem.setVisible(true);
                }
                if(menuItem.getItemId() == R.id.nav_portofolio){
                    menuItem.setEnabled(false);
                    menuItem.setVisible(false);
                }
                if(menuItem.getItemId() == R.id.nav_promotion){
                    menuItem.setEnabled(false);
                    menuItem.setVisible(false);
                }
                if(menuItem.getItemId() == R.id.nav_add_kategori){
                    menuItem.setEnabled(false);
                    menuItem.setVisible(false);
                }
                if(menuItem.getItemId() == R.id.nav_upgrade_merchant){
                    menuItem.setEnabled(true);
                    menuItem.setVisible(true);
                }
            }
        }

        pp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start activity white array bitmap

                String tempNama = nameUserView.getText().toString();
                String tempEmail = emailUserView.getText().toString();
                String tempPhone = phoneUserView.getText().toString();
                String tempAddress = addressUserView.getText().toString();

//                startActivity(new Intent(MainActivity.this, UserProfilActivity.class));
                Intent intent = new Intent(MainActivity.this, InfoUserActivity.class);
                sMerchant.setName(tempNama);
                sMerchant.setEmail(tempEmail);
                sMerchant.setAddress(tempAddress);
                sMerchant.setPhone(tempPhone);
//                intent.putExtra("_ID",user.getUid());
                startActivity(intent);
            }
        });

        navigationView.setNavigationItemSelectedListener(this);

        // default
        // #onNavigationItemSelected((MenuItem) findViewById(R.id.nav_home));
        // #Fragment fragment = new HomeFragment();
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.frame_container, new HomeFragment());
        tx.commit();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // this is your backendcall
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View nav = navigationView.getHeaderView(0);
        de.hdodenhof.circleimageview.CircleImageView pp = (de.hdodenhof.circleimageview.CircleImageView) nav.findViewById(R.id.imageView);
        pp.setImageBitmap(new lib(this).getPhotoProfilToStorage());

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        TextView statusUserView = (TextView) nav.findViewById(R.id.nav_view_status);
        statusUserView.setText("Status : "+user.get(SessionManagement.KEY_STATUS));

        TextView emailUserView = (TextView) nav.findViewById(R.id.nav_view_email);
        TextView nameUserView = (TextView) nav.findViewById(R.id.nav_view_name);

        nameUserView.setText(user.get(SessionManagement.KEY_NAME));
        emailUserView.setText(user.get(SessionManagement.KEY_EMAIL));


        Menu menu = navigationView.getMenu();
        if (user.get(SessionManagement.KEY_STATUS).equals("merchant")){
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("merchants").child(user.get(SessionManagement.KEY_ID)).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            // Get user value
                            Merchant merchant = dataSnapshot.getValue(Merchant.class);
//                            TempData.NO_BANK_ACCOUNT = merchant.NO_BANK_ACCOUNT;
//                            TempData.NO_KTP = merchant.NO_KTP;
//                            TempData.NO_PHONE_RELATIVE = merchant.NO_PHONE_RELATIVE;
//                            TempData.NO_KTP_RELATIVE = merchant.NO_KTP_RELATIVE;
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    }
            );

            for (int menuItemIndex = 0; menuItemIndex < menu.size(); menuItemIndex++) {
                MenuItem menuItem= menu.getItem(menuItemIndex);
                if(menuItem.getItemId() == R.id.nav_messeg){
                    menuItem.setEnabled(true);
                    menuItem.setVisible(true);
                }
                if(menuItem.getItemId() == R.id.nav_portofolio){
                    menuItem.setEnabled(true);
                    menuItem.setVisible(true);
                }
                if(menuItem.getItemId() == R.id.nav_promotion){
                    menuItem.setEnabled(true);
                    menuItem.setVisible(true);
                }
                if(menuItem.getItemId() == R.id.nav_add_kategori){
                    menuItem.setEnabled(true);
                    menuItem.setVisible(true);
                }
                if(menuItem.getItemId() == R.id.nav_upgrade_merchant){
                    menuItem.setEnabled(false);
                    menuItem.setVisible(false);
                }
            }
        } else {
            for (int menuItemIndex = 0; menuItemIndex < menu.size(); menuItemIndex++) {
                MenuItem menuItem= menu.getItem(menuItemIndex);
                if(menuItem.getItemId() == R.id.nav_messeg){
                    menuItem.setEnabled(true);
                    menuItem.setVisible(true);
                }
                if(menuItem.getItemId() == R.id.nav_portofolio){
                    menuItem.setEnabled(false);
                    menuItem.setVisible(false);
                }
                if(menuItem.getItemId() == R.id.nav_promotion){
                    menuItem.setEnabled(false);
                    menuItem.setVisible(false);
                }
                if(menuItem.getItemId() == R.id.nav_add_kategori){
                    menuItem.setEnabled(false);
                    menuItem.setVisible(false);
                }
                if(menuItem.getItemId() == R.id.nav_upgrade_merchant){
                    menuItem.setEnabled(true);
                    menuItem.setVisible(true);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        TextView emailUserView = (TextView) findViewById(R.id.nav_view_email);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            emailUserView.setText(user.getEmail());
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
//            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            startActivity(new Intent(MainActivity.this, UserProfilActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;
        if (id == R.id.nav_home) {
            fragment = new HomeFragment();
        } else if (id == R.id.nav_discont) {
            fragment = new DiscountFragment();
        } else if (id == R.id.nav_history) {
            fragment = new HistoryFragment();
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
        } else if (id == R.id.nav_logout) {
            signOut();
            finish();
        } else if (id == R.id.nav_portofolio) {
            fragment = null;
        } else if (id == R.id.nav_promotion) {
            fragment = null;
        } else if (id == R.id.nav_messeg) {
            fragment = new ChatFragment();
        } else if (id == R.id.nav_upgrade_merchant) {
            startActivity(new Intent(MainActivity.this, SignupMerchantActivity.class));
        } else if (id == R.id.nav_add_kategori) {
            startActivity(new Intent(MainActivity.this, AddKategoriActivity.class));
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //sign out method
    public void signOut() {
        auth.signOut();
    }

    /**
     * Method to show alert dialog
     * */
    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle("Response")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

    public static byte[] convertFileToByteArray(File f)
    {
        byte[] byteArray = null;
        try
        {
            InputStream inputStream = new FileInputStream(f);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024*8];
            int bytesRead =0;

            while ((bytesRead = inputStream.read(b)) != -1)
            {
                bos.write(b, 0, bytesRead);
            }

            byteArray = bos.toByteArray();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return byteArray;
    }
}
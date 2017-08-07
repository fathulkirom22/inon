package com.bahwell.inoncharge.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.bahwell.inoncharge.R;
import com.bahwell.inoncharge.other.DataListGrid;
import com.bahwell.inoncharge.other.GridViewAdapterBrowse;
import com.bahwell.inoncharge.other.SessionKategori;
import com.bahwell.inoncharge.other.lib;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BrowserMerchantActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private List<DataListGrid> listgambar;
    private GridView view_merchant_in_browser;
    private GridViewAdapterBrowse gridViewAdapter;
    private DatabaseReference mDatabase;
    private ArrayList<String> idMerchants;
    private Bitmap tempphoto;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    SessionKategori sessionKategori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_browser_merchant);
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(BrowserMerchantActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        sessionKategori = new SessionKategori(getApplicationContext());

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final String title_new = getIntent().getExtras().getString("title_new");
        setTitle(sessionKategori.getKategoriName());

        mDatabase = FirebaseDatabase.getInstance().getReference("kategory").child(title_new);

        System.out.println("Before adding listener");
        mDatabase.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("In onDataChange");
                long count = dataSnapshot.getChildrenCount();
                String strLong = Long.toString(count);
                System.out.println(strLong);

                collectUserId((Map<String,Object>) dataSnapshot.getValue());

                getProductList();
                setAdapters(title_new);
            }
            public void onCancelled(DatabaseError databaseError) { }
        });
        System.out.println("After adding listener");

        view_merchant_in_browser = (GridView) findViewById(R.id.view_merchant_in_browser);
        view_merchant_in_browser.setNestedScrollingEnabled(true);


    }

    private void collectUserId(Map<String,Object> kategory) {
        idMerchants = new ArrayList<>();
        //iterate through each user, ignoring their UID
        try {
            for (Map.Entry<String, Object> entry : kategory.entrySet()){
                System.out.println(entry.getKey());
                idMerchants.add(entry.getKey());
            }
            System.out.println(idMerchants.toString());
        } catch (Exception e){
            new lib(this).showAlert("not available service");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setAdapters(String title) {
        gridViewAdapter = new GridViewAdapterBrowse(this, R.layout.activity_browser_merchant,
                listgambar, title);
        view_merchant_in_browser.setAdapter(gridViewAdapter);
    }

    public void getProductList() {
        listgambar = new ArrayList<>();
        ArrayList<String> urls = new ArrayList<>();
        for (String object: idMerchants) {
            //object = id merchant
            System.out.println(object);
            listgambar.add(
                    new DataListGrid("gs://inon-charge.appspot.com/"+object+"/photo_profil.jpg",
                    object
            ));
        }
    }
}

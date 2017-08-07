package com.bahwell.inoncharge.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bahwell.inoncharge.R;
import com.bahwell.inoncharge.other.User;
import com.bahwell.inoncharge.other.lib;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static com.bahwell.inoncharge.R.id.txtDescription;

public class TampilGambarActivity extends AppCompatActivity {
    ImageView imageView;
    Bitmap tempphoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_gambar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        imageView = (ImageView) findViewById(R.id.imageView);
        try {
            String _ID = getIntent().getExtras().getString("_ID");
            Bitmap photo = new lib(this).getPhotoToStorageBig(_ID);
            imageView.setImageBitmap(photo);
        }catch (Exception e){
            imageView.setImageBitmap(new lib(this).getPhotoProfilToStorageBig());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

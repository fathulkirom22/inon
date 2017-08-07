package com.bahwell.inoncharge.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bahwell.inoncharge.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

public class AddKategoriActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String kategori;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth auth;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    //    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog progressDialog;
    private DatabaseReference mDatabase;
    private EditText pricehour;
    private Bitmap imageBitmapportofolio;
    private String tempNama;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kategori);
        auth = FirebaseAuth.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.arraykategory, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        pricehour = (EditText) findViewById(R.id.price_hour);
        Button send = (Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send();
            }
        });

        ImageView imageView1= (ImageView) findViewById(R.id.gportofoliokategori);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 10);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String nama= getRealPathFromURI(selectedImage);
            tempNama = nama;
            TextView image_name = (TextView) findViewById(R.id.portofolio);
            image_name.setText(nama);
            try {
                imageBitmapportofolio = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (IOException e) {
                showAlert(e.getMessage());
                e.printStackTrace();
            }
        }
    }
    public void upload(Bitmap data_gambar,String kategori){
        final ProgressDialog progressDialog = new ProgressDialog(AddKategoriActivity.this);
        progressDialog.setTitle("Uploading");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final String tmpKatgori = kategori;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        data_gambar.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] dataBAOS = baos.toByteArray();

        /***************** UPLOADS THE PIC TO FIREBASE*****************/
        // Points to the root reference
        StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://inon-charge.appspot.com/");
        StorageReference imagesRef = storageRef.child(user.getUid()+"/portofolio/"+kategori+"/"+tempNama+".jpg");

        UploadTask uploadTask = imagesRef.putBytes(dataBAOS);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                progressDialog.dismiss();
                showAlert(exception.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Intent intent = new Intent(AddKategoriActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                String pricehours = pricehour.getText().toString().trim();
                writeKategory(user.getUid(),tmpKatgori,pricehours);
                progressDialog.dismiss();
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                startActivity(intent);
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                //calculating progress percentage
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                //displaying percentage in progress dialog
                progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
            }
        });
    }
    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle("Response from Servers")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    private void send() {
        upload(imageBitmapportofolio,kategori);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        kategori = parent.getItemAtPosition(position).toString();
        
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
    private void writeKategory(String userId, String kategori, String pricehour) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("kategory").child(kategori).child(userId).child("pricehour").setValue(pricehour);
    }
    public String getRealPathFromURI(Uri contentUri) {
        // can post image
        String result = null;
        if (contentUri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = contentUri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

}
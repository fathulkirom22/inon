package com.bahwell.inoncharge.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bahwell.inoncharge.R;
import com.bahwell.inoncharge.other.Merchant;
import com.bahwell.inoncharge.other.SessionManagement;
import com.bahwell.inoncharge.other.TempData;
import com.bahwell.inoncharge.other.User;
import com.bahwell.inoncharge.other.lib;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;


public class SignupMerchantActivity extends AppCompatActivity{

    private FloatingActionButton fab;
    private Button regMerchant;
    private FirebaseAuth auth;
    private ProgressDialog dialog;
    private EditText noktp,norek,notelpsdr,noktpsdr;
    private FirebaseAuth.AuthStateListener authListener;
    private DatabaseReference mDatabase;
    private Uri selectedImage;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private Bitmap imageBitmapktp, imageBitmapkk,imageBitmapktps,imageBitmapprof = null;
    private String noktp1,norek1,notelpsdr1,noktpsdr1;

    lib l = new lib(this);

    SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(SignupMerchantActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };



        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();


        //get current user
//        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        setContentView(R.layout.activity_signup_merchant);


        // Session class instance
        session = new SessionManagement(getApplicationContext());

        noktp = (EditText) findViewById(R.id.noktp);
        norek = (EditText) findViewById(R.id.norek);
        notelpsdr = (EditText) findViewById(R.id.notelpsdr);
        noktpsdr = (EditText) findViewById(R.id.noktpsdr);


        de.hdodenhof.circleimageview.CircleImageView pp = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.profile_image);
        pp.setImageBitmap(new lib(this).getPhotoProfilToStorage());


        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        TextView nameUserView = (TextView) findViewById(R.id.nav_view_name);
        nameUserView.setText(user.get(SessionManagement.KEY_NAME));

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(SignupMerchantActivity.this);
            }
        });
        regMerchant = (Button) findViewById(R.id.reg_merchant_button);

        dialog = new ProgressDialog(this);

        regMerchant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                boolean cancel = false;
                boolean cancel1 = false;
                View focusView = null;

                noktp1 = noktp.getText().toString().trim();
                norek1 = norek.getText().toString().trim();
                notelpsdr1 = notelpsdr.getText().toString().trim();
                noktpsdr1 = noktpsdr.getText().toString().trim();


                String nn1,nn2,nn3;

                TextView image_name1 = (TextView) findViewById(R.id.nmfktp);
                TextView image_name2 = (TextView) findViewById(R.id.nmfkk);
                TextView image_name3 = (TextView) findViewById(R.id.nmfktps);

                nn1 = image_name1.getText().toString().trim();
                nn2 = image_name2.getText().toString().trim();
                nn3 = image_name3.getText().toString().trim();

                if (noktp1.equals(null) || noktp1.equals("") ){
                    noktp.setError("Not Null");
                    focusView = noktp;
                    cancel = true;
                }
                if (norek1.equals(null)|| norek1.equals("")){
                    norek.setError("Not Null");
                    focusView = norek;
                    cancel = true;
                }
                if (notelpsdr1.equals(null)|| notelpsdr1.equals("")){
                    notelpsdr.setError("Not Null");
                    focusView = notelpsdr;
                    cancel = true;
                }
                if (noktpsdr1.equals(null)|| noktpsdr1.equals("")){
                    noktpsdr.setError("Not Null");
                    focusView = noktpsdr;
                    cancel = true;
                }
                if (nn1.equals(null)|| nn1.equals("")|| nn1.equals(R.string.pID)){
                    cancel1 = true;
                }
                if (nn2.equals(null)|| nn2.equals("")|| nn2.equals(R.string.pFamilyCard)){
                    cancel1 = true;
                }
                if (nn3.equals(null)|| nn3.equals("") || nn3.equals(R.string.pIDrelation)){
                    cancel1 = true;
                }

                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                }else if (cancel1){
                    showAlert("Picture requaired not found !!!");
                }else {
                    // get user data from session
                    HashMap<String, String> user = session.getUserDetails();

                    writeNewMerchant(
                            user.get(SessionManagement.KEY_ID),
                            noktp1,
                            norek1,
                            notelpsdr1,
                            noktpsdr1
                    );
                    System.out.println("Ceksssss: " + noktp1);
                    Toast.makeText(SignupMerchantActivity.this,"Berhasil daftar",Toast.LENGTH_LONG).show();
                    finish();
                }



            }
        });

        ImageView imageView1= (ImageView) findViewById(R.id.gktp);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, 10);
            }

        });

        ImageView imageView2= (ImageView) findViewById(R.id.gkk);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, 20);
            }

        });

        ImageView imageView3= (ImageView) findViewById(R.id.gktps);
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, 30);
            }

        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10 && resultCode == RESULT_OK && null != data) {

            Uri selectedImage = data.getData();
            String nama= getRealPathFromURI(selectedImage);

            TextView image_name = (TextView) findViewById(R.id.nmfktp);
            image_name.setText(nama);
            try {
                imageBitmapktp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                upload(imageBitmapktp,"/requirement","KTP");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == 20 && resultCode == RESULT_OK && null != data) {

            Uri selectedImage = data.getData();
            String nama= getRealPathFromURI(selectedImage);

            TextView image_name = (TextView) findViewById(R.id.nmfkk);
            image_name.setText(nama);
            try {
                imageBitmapkk = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                upload(imageBitmapkk,"/requirement","KK");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == 30 && resultCode == RESULT_OK && null != data) {

            Uri selectedImage = data.getData();
            String nama= getRealPathFromURI(selectedImage);

            TextView image_name = (TextView) findViewById(R.id.nmfktps);
            image_name.setText(nama);
            try {
                imageBitmapktps = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                upload(imageBitmapktps,"/requirement","KTPS");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri selectedImage = result.getUri();
                de.hdodenhof.circleimageview.CircleImageView gbrprofile = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.profile_image);
                try {
                    imageBitmapprof = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

                    //compress
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    imageBitmapprof.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                    Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(stream.toByteArray()));
                    byte[] byteArray = stream.toByteArray();

                    upload(imageBitmapprof,"","photo_profil");
                    gbrprofile.setImageBitmap(decoded);
                    l.savePhotoProfilToStorage(byteArray);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
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

    private void writeNewMerchant(String userid,String NO_KTP,String NO_KTP_RELATIVE,String NO_BANK_ACCOUNT,String NO_PHONE_RELATIVE){
        Merchant merchant = new Merchant(NO_KTP,NO_KTP_RELATIVE,NO_BANK_ACCOUNT,NO_PHONE_RELATIVE);

        String tmpStatus = "merchant";

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("merchants").child(userid).setValue(merchant);
        mDatabase.child("users").child(userid).child("Status").setValue(tmpStatus);

        session.setStatus(tmpStatus);

        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Merchant newPost = dataSnapshot.getValue(Merchant.class);
                System.out.println("NO KTP: " + newPost.NO_KTP);
                System.out.println("NO REKENING : " + newPost.NO_BANK_ACCOUNT);
                System.out.println("NO TLPN SAUDARA: " + newPost.NO_PHONE_RELATIVE);
                System.out.println("NO KTP SAUDARA: " + newPost.NO_KTP_RELATIVE);
                System.out.println("Previous Post ID: " + prevChildKey);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                User newPost = dataSnapshot.getValue(User.class);
                System.out.println("Status : " + newPost.Status);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

    }

    public void upload(Bitmap data_gambar,String direc,String nama_photo){
        final ProgressDialog progressDialog = new ProgressDialog(SignupMerchantActivity.this);
        progressDialog.setTitle("Uploading");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        data_gambar.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] dataBAOS = baos.toByteArray();

        /***************** UPLOADS THE PIC TO FIREBASE*****************/
        // Points to the root reference
        StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://inon-charge.appspot.com/");
        StorageReference imagesRef = storageRef.child(user.getUid()+""+direc+"/"+nama_photo+".jpg");

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
                progressDialog.dismiss();
                Uri downloadUrl = taskSnapshot.getDownloadUrl();

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

    /**
     * Method to show alert dialog
     * */
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

}
package com.bahwell.inoncharge.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bahwell.inoncharge.R;
import com.bahwell.inoncharge.other.Merchant;
import com.bahwell.inoncharge.other.SessionManagement;
import com.bahwell.inoncharge.other.TempData;
import com.bahwell.inoncharge.other.User;
import com.bahwell.inoncharge.other.lib;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class UserProfilActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private DatabaseReference mDatabase;

    private FloatingActionButton fab;
    final Context context = this;
    private TextView result0, result1, result2, result3, result4, result5;
    private ImageView ll1, ll2, ll3, ll4, ll5;
    private RelativeLayout rl1;
    private static int RESULT_LOAD_IMAGE = 1;
    private static final int MY_PASSWORD_DIALOG_ID = 4;

    Bitmap imageBitmapprof = null;

    String PHOTO_PROFIL = "PHOTO_PROFIL";

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private lib l = new lib(this);

    // Session Manager Class
    SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(UserProfilActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //get current user

        setContentView(R.layout.activity_user_profil);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Session class instance
        session = new SessionManagement(getApplicationContext());

        result0 = (TextView) findViewById(R.id.user_profile_name);//textview nama
        result1 = (TextView) findViewById(R.id.editTextResult);//textview nama
        result2 = (TextView) findViewById(R.id.editTextResult2);//textview email
        result3 = (TextView) findViewById(R.id.editTextResult3);//edittext password
        result4 = (TextView) findViewById(R.id.editTextResult4);//textview phone
        result5 = (TextView) findViewById(R.id.editTextResult5);//textview alamat

        ll1 = (ImageView) findViewById(R.id.fnama);//linearlayout nama
        ll2 = (ImageView) findViewById(R.id.femail);//linearlayout email
        ll3 = (ImageView) findViewById(R.id.fepass); //linearlayout password
        ll4 = (ImageView) findViewById(R.id.fphone);//linearlayout phone
        ll5 = (ImageView) findViewById(R.id.falamat);//linearlayout alamat

        rl1 = (RelativeLayout) findViewById(R.id.profile_layout);

        fab = (FloatingActionButton) findViewById(R.id.fab);//floating action bar

        de.hdodenhof.circleimageview.CircleImageView pp = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.user_profile_photo); //view poto user

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference islandRef = storage.getReference().child(user.getUid()+"/photo_profil.jpg");

        pp.setImageBitmap(new lib(this).getPhotoProfilToStorage());

        // get user data from session
        HashMap<String, String> users = session.getUserDetails();

        result0.setText(users.get(SessionManagement.KEY_NAME));
        result1.setText(users.get(SessionManagement.KEY_NAME));
        result2.setText(users.get(SessionManagement.KEY_EMAIL));
        result4.setText(users.get(SessionManagement.KEY_PHONE));
        result5.setText(users.get(SessionManagement.KEY_ADDRESS));

        int backgroundColor = ContextCompat.getColor(context, R.color.colorPrimaryDark);
        rl1.setBackgroundColor(backgroundColor);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(UserProfilActivity.this);
            }
        });

        ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.prompts1, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        // edit text
                                        // get user data from session
                                        HashMap<String, String> users = session.getUserDetails();

                                        mDatabase.child("users").child(users.get(SessionManagement.KEY_ID)).child("Name").setValue(userInput.getText().toString().trim());

                                        session.setName(userInput.getText().toString().trim());

                                        result1.setText(userInput.getText().toString().trim());

                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        ll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.prompts2, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput2);
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        final ProgressDialog progressDialog = new ProgressDialog(UserProfilActivity.this);
                                        progressDialog.setTitle("Prosses...");
                                        progressDialog.setCancelable(false);
                                        progressDialog.setCanceledOnTouchOutside(false);
                                        progressDialog.show();
                                        // get user input and set it to result
                                        // edit text
                                        if (user != null && !userInput.getText().toString().trim().equals("")) {
                                            user.updateEmail(userInput.getText().toString().trim())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(UserProfilActivity.this, "Email address is updated. Please sign in with new email id!", Toast.LENGTH_LONG).show();
                                                                result2.setText(userInput.getText());

                                                                session.setEmail(userInput.getText().toString().trim());

                                                                mDatabase.child("users").child(user.getUid()).child("Email").setValue(userInput.getText().toString().trim());
                                                                progressDialog.dismiss();
                                                            } else {
                                                                Toast.makeText(UserProfilActivity.this, "Failed to update email!", Toast.LENGTH_LONG).show();
                                                                progressDialog.dismiss();
                                                            }
                                                        }
                                                    });
                                        } else if (userInput.getText().toString().trim().equals("")) {
                                            userInput.setError("Enter email");
                                            progressDialog.dismiss();
                                        }
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        ll3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.prompts5, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(promptsView);

                final EditText password1 = (EditText) promptsView.findViewById(R.id.password);
                final EditText password2 = (EditText) promptsView.findViewById(R.id.password2);
                final TextView error = (TextView) promptsView.findViewById(R.id.TextView_PwdProblem);

                password2.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String strPass1 = password1.getText().toString();
                        String strPass2 = password2.getText().toString();
                        if (strPass1.equals(strPass2)) {
                            error.setText("Password match");
                        } else {
                            error.setText("Pasword not match");
                        }

                    }
                });
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {

                                        final ProgressDialog progressDialog = new ProgressDialog(UserProfilActivity.this);
                                        progressDialog.setTitle("Prosses...");
                                        progressDialog.setCancelable(false);
                                        progressDialog.setCanceledOnTouchOutside(false);
                                        progressDialog.show();

                                        // get user input and set it to result
                                        // edit text
                                        String strPassword1 = password1.getText().toString();
                                        String strPassword2 = password2.getText().toString();
                                        if (strPassword1.equals(strPassword2)) {
                                            if (user != null && !password1.getText().toString().trim().equals("")) {
                                                if (password1.getText().toString().trim().length() < 6) {
                                                    password1.setError("Password too short, enter minimum 6 characters");
                                                    progressDialog.dismiss();
                                                } else {
                                                    user.updatePassword(password1.getText().toString().trim())
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        Toast.makeText(UserProfilActivity.this, "Password is updated, sign in with new password!", Toast.LENGTH_SHORT).show();
                                                                        result3.setText(password1.getText().toString().trim());
                                                                        progressDialog.dismiss();
                                                                    } else {
                                                                        Toast.makeText(UserProfilActivity.this, "Failed to update password!", Toast.LENGTH_SHORT).show();
                                                                        progressDialog.dismiss();
                                                                    }
                                                                }
                                                            });
                                                }
                                            } else if (password1.getText().toString().trim().equals("")) {
                                                password1.setError("Enter password");
                                                progressDialog.dismiss();
                                            }
                                        }else{
                                            Toast.makeText(UserProfilActivity.this, "Failed Changed Password", Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        ll4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.prompts3, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput3);
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        // edit text
                                        mDatabase.child("users").child(user.getUid()).child("Phone").setValue(userInput.getText().toString());

                                        session.setPhone(userInput.getText().toString().trim());

                                        result4.setText(userInput.getText().toString().trim());
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        ll5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.prompts4, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput4);
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        // edit text
                                        mDatabase.child("users").child(user.getUid()).child("Address").setValue(userInput.getText().toString());

                                        session.setAddress(userInput.getText().toString().trim());

                                        result5.setText(userInput.getText().toString().trim());
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });


    }

    @Override
    protected void onRestart() {
        super.onRestart();

        // get user data from session
        HashMap<String, String> users = session.getUserDetails();

        result0.setText(users.get(SessionManagement.KEY_NAME));
        result1.setText(users.get(SessionManagement.KEY_NAME));
        result2.setText(users.get(SessionManagement.KEY_EMAIL));
        result4.setText(users.get(SessionManagement.KEY_PHONE));
        result5.setText(users.get(SessionManagement.KEY_ADDRESS));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri selectedImage = result.getUri();
                de.hdodenhof.circleimageview.CircleImageView gbrprofile = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.user_profile_photo);
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

    public void upload(Bitmap data_gambar,String direc,String nama_photo){
        final ProgressDialog progressDialog = new ProgressDialog(UserProfilActivity.this);
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}

package com.bahwell.inoncharge.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.app.ProgressDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bahwell.inoncharge.R;
import com.bahwell.inoncharge.other.SessionManagement;
import com.bahwell.inoncharge.other.TempData;
import com.bahwell.inoncharge.other.User;
import com.bahwell.inoncharge.other.lib;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;
//    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog progressDialog;
    private EditText eName, eEmail, ePassword, ePasswordConfirm, ePhone, eAddress;
    private Button buttonSignup;
    private DatabaseReference mDatabase;

    SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);

        session = new SessionManagement(getApplicationContext());

        //initializing views
        eName = (EditText) findViewById(R.id.nama);
        eEmail = (EditText) findViewById(R.id.email);
        ePassword = (EditText) findViewById(R.id.password);
        ePasswordConfirm = (EditText) findViewById(R.id.confirm_password);
        ePhone = (EditText) findViewById(R.id.phone);
        eAddress = (EditText) findViewById(R.id.address);

        buttonSignup = (Button) findViewById(R.id.reg_button);

        progressDialog = new ProgressDialog(this);

        //attaching listener to button
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void registerUser(){
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        //getting email and password from edit texts
        String name = eName.getText().toString().trim();
        String email = eEmail.getText().toString().trim();
        String password  = ePassword.getText().toString().trim();
        String passwordConfirm = ePasswordConfirm.getText().toString().trim();
        String phone = ePhone.getText().toString().trim();
        String address = eAddress.getText().toString().trim();

        //checking if email and passwords are empty
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this,"Please enter name",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(passwordConfirm)){
            Toast.makeText(this,"Please enter password confirm",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(phone)){
            Toast.makeText(this,"Please enter phone",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(address)){
            Toast.makeText(this,"Please enter address",Toast.LENGTH_LONG).show();
            return;
        }

        if(!(password.equals(passwordConfirm))){
            Toast.makeText(this,"Password not match",Toast.LENGTH_LONG).show();
            return;
        }



        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();
        new lib(this).saveOfflinePhotoProfil();

//        Map<String, User> users = new HashMap<String, User>();
//        users.put(email, new User(name, phone, address, "user"));
//        mDatabase.setValue(users);

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            //display some message here
                            String name = eName.getText().toString().trim();
                            String phone = ePhone.getText().toString().trim();
                            String address = eAddress.getText().toString().trim();
                            String status = "user";

                            FirebaseUser user = task.getResult().getUser();
                            Intent intent = new Intent(SignupActivity.this, MainActivity.class);

                            session.setName(name);
                            session.setEmail(user.getEmail());
                            session.setPhone(phone);
                            session.setAddress(address);
                            session.setStatus(status);

                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            writeNewUser(user.getUid(), name, user.getEmail(), phone, address, status);
                            startActivity(intent);
                            finish();
                        }else{
                            //display some message here
                            AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                            builder.setMessage(task.getException().getMessage())
                                    .setTitle("Error")
                                    .setPositiveButton(android.R.string.ok, null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    // [START basic_write]
    private void writeNewUser(String userId, String name, String email, String phone, String address, String status) {
        User user = new User(name, email, phone, address, status);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(userId).setValue(user);
    }
    // [END basic_write]
}
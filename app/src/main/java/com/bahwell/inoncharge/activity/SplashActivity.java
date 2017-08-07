package com.bahwell.inoncharge.activity;

import android.animation.ObjectAnimator;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;

import com.bahwell.inoncharge.R;
import com.bahwell.inoncharge.other.SessionManagement;
import com.bahwell.inoncharge.other.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;


public class SplashActivity extends AppCompatActivity {

    ProgressBar mprogressBar;
    private DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    // Session Manager Class
    SessionManagement session;
    String _ID, _NAME, _TOTAL_HARGA, _TITLE, _hari_or_jam, _date_awal, _date_ahir, _date, _time_awal, _time_ahir;


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



//        Log.d("TEMP", "Extras 1111111");

//        if (intents.getExtras() != null) {
//            Intent intent = new Intent(this, OrderConfirmationActivity.class);
//            intent.putExtra("AnotherActivity", "True");
//            intent.putExtra("_ID", intents.getExtras().getString("_ID"));
//            intent.putExtra("_NAME", intents.getExtras().getString("_NAME"));
//            intent.putExtra("_TOTAL_HARGA", intents.getExtras().getString("_TOTAL_HARGA"));
//            intent.putExtra("_TITLE", intents.getExtras().getString("_TITLE"));
//            intent.putExtra("_hari_or_jam", intents.getExtras().getString("_hari_or_jam"));
//            if (intents.getExtras().getString("_hari_or_jam").equals("true")){
//                intent.putExtra("_date_awal", intents.getExtras().getString("_date_awal"));
//                intent.putExtra("_date_ahir", intents.getExtras().getString("_date_ahir"));
//            }
//            else if (intents.getExtras().getString("_hari_or_jam").equals("false")){
//                intent.putExtra("_date", intents.getExtras().getString("_date"));
//                intent.putExtra("_time_awal", intents.getExtras().getString("_time_awal"));
//                intent.putExtra("_time_ahir", intents.getExtras().getString("_time_ahir"));
//            }
//            startActivity(intents);
//            finish();
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.d("TEMP", "Extras are 22222222222");
//
//
//
//
//        Intent intents = getIntent();
//        if (intents.getExtras() != null) {
//            Bundle extras = intents.getExtras();
//            String tabNumber;
//
//            if(extras != null) {
//                _ID             = extras.getString("_ID");
//                _NAME           = extras.getString("_NAME");
//                _TOTAL_HARGA    = extras.getString("_TOTAL_HARGA");
//                _TITLE          = extras.getString("_TITLE");
//                _hari_or_jam    = extras.getString("_hari_or_jam");
//
//                if (_hari_or_jam.equals("true")){
//                    _date_awal = extras.getString("_date_awal");
//                    _date_ahir = extras.getString("_date_ahir");
//                }
//                else if (_hari_or_jam.equals("false")){
//                    _date = extras.getString("_date");
//                    _time_awal = extras.getString("_time_awal");
//                    _time_ahir = extras.getString("_time_ahir");
//                }
//                Log.d("TEMP", _TITLE);
//            } else {
//                Log.d("TEMP", "Extras are NULL");}
//
//            Intent intent = new Intent(this, OrderConfirmationActivity.class);
//            intent.setAction(Intent.ACTION_MAIN);
//            intent.addCategory(Intent.CATEGORY_LAUNCHER);
//            intent.putExtra("AnotherActivity", "True");
//            intent.putExtra("_ID", _ID);
//            intent.putExtra("_NAME", _NAME);
//            intent.putExtra("_TOTAL_HARGA", _TOTAL_HARGA);
//            intent.putExtra("_TITLE", _TITLE);
//            intent.putExtra("_hari_or_jam", _hari_or_jam);
//            if (_hari_or_jam.equals("true")){
//                intent.putExtra("_date_awal", _date_awal);
//                intent.putExtra("_date_ahir", _date_ahir);
//            }
//            else if (_hari_or_jam.equals("false")){
//                intent.putExtra("_date", _date);
//                intent.putExtra("_time_awal", _time_awal);
//                intent.putExtra("_time_ahir", _time_ahir);
//            }
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                    PendingIntent.FLAG_UPDATE_CURRENT);
//
//            startActivity(intent);
//            finish();
//        }else{
//            Log.d("TEMP", "Extras are tidakkkkk");
//        }







        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Session Manager
        session = new SessionManagement(getApplicationContext());

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        } else {
            String id = user.getUid();
            session.setID(id);
            session.setToken(FirebaseInstanceId.getInstance().getToken());

            mDatabase = FirebaseDatabase.getInstance().getReference();
            token(mDatabase,user.getUid());
            mDatabase.child("users").child(user.getUid()).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Thread timerThread = new Thread() {
                                public void run() {
                                    try {
                                        sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    } finally {
                                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            };
                            timerThread.start();
                            // Get user value
                            User user = dataSnapshot.getValue(User.class);
                            session.setName(user.Name);
                            session.setEmail(user.Email);
                            session.setPhone(user.Phone);
                            session.setAddress(user.Address);
                            session.setStatus(user.Status);


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w("getUser:onCancelled", databaseError.toException());
                            // ...
                        }
                    });
        }
        mprogressBar = (ProgressBar) findViewById(R.id.circular_progress_bar);
        ObjectAnimator anim = ObjectAnimator.ofInt(mprogressBar, "progress", 0, 50);
        anim.setDuration(5000);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.start();
    }

    @Override
    protected void onPause(){
        super.onPause();
        finish();
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

    public void token(DatabaseReference ref,String a) {
        String token = FirebaseInstanceId.getInstance().getToken();
        ref.child("users").child(a).child("pushToken").setValue(token);
    }

}

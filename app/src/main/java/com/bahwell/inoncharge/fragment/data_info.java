package com.bahwell.inoncharge.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bahwell.inoncharge.R;
import com.bahwell.inoncharge.activity.LoginActivity;
import com.bahwell.inoncharge.activity.UserProfilActivity;
import com.bahwell.inoncharge.other.SessionManagement;
import com.bahwell.inoncharge.other.SessionMerchant;
import com.bahwell.inoncharge.other.lib;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class data_info extends Fragment {

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private DatabaseReference mDatabase;

    private TextView result0, result1, result2, result3, result4, result5;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    // Session Manager Class
    SessionMerchant sMerchant;

    public data_info() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.data_info1, container, false);

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }
        };

        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        sMerchant = new SessionMerchant(getContext().getApplicationContext());

        result1 = (TextView) rootView.findViewById(R.id.editTextResult);//textview nama
        result2 = (TextView) rootView.findViewById(R.id.editTextResult1);//textview email
        result3 = (TextView) rootView.findViewById(R.id.editTextResult2);//textview phone
        result4 = (TextView) rootView.findViewById(R.id.editTextResult3);//textview alamat
        // Inflate the layout for this fragment

        HashMap<String, String> users = sMerchant.getUserDetails();

        result1.setText(users.get(SessionMerchant.KEY_NAME));
        result2.setText(users.get(SessionMerchant.KEY_EMAIL));
        result3.setText(users.get(SessionMerchant.KEY_PHONE));
        result4.setText(users.get(SessionMerchant.KEY_ADDRESS));
        return rootView;
    }

}

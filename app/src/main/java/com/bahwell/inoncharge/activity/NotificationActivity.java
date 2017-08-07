package com.bahwell.inoncharge.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.bahwell.inoncharge.R;
import com.bahwell.inoncharge.fragment.AcceptedFragment;
import com.bahwell.inoncharge.fragment.RejectedFragment;

/**
 * Created by INDRA on 28/07/2017.
 */

public class NotificationActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notification);

        String status = getIntent().getExtras().getString("status");
        if (status.equals("diterima"))
        {
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            AcceptedFragment fragment = new AcceptedFragment();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
        }
        else if (status.equals("ditolak"))
        {
            FragmentManager fragmentManager = getFragmentManager();
            RejectedFragment fragment = new RejectedFragment();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
        }
    }
}

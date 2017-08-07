package com.bahwell.inoncharge.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.bahwell.inoncharge.R;

public class CancelReasonActivity extends AppCompatActivity implements View.OnClickListener {

    CheckBox Sibuk, AK, JP, LK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_reason);

        Sibuk = (CheckBox) findViewById(R.id.CheckBox1);
        Sibuk.setOnClickListener(this);
        AK = (CheckBox) findViewById(R.id.CheckBox2);
        AK.setOnClickListener(this);
        JP = (CheckBox) findViewById(R.id.CheckBox3);
        JP.setOnClickListener(this);
        LK = (CheckBox) findViewById(R.id.CheckBox4);
        LK.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.CheckBox1:
                if (Sibuk.isChecked())
                    Toast.makeText(getApplicationContext(), "Android", Toast.LENGTH_LONG).show();
                    AK.setChecked(false);
                    JP.setChecked(false);
                    LK.setChecked(false);
                break;
            case R.id.CheckBox2:
                if (AK.isChecked())
                    Toast.makeText(getApplicationContext(), "Java", Toast.LENGTH_LONG).show();
                    Sibuk.setChecked(false);
                    JP.setChecked(false);
                    LK.setChecked(false);
                break;
            case R.id.CheckBox3:
                if (JP.isChecked())
                    Toast.makeText(getApplicationContext(), "PHP", Toast.LENGTH_LONG).show();
                    AK.setChecked(false);
                    Sibuk.setChecked(false);
                    LK.setChecked(false);
                break;
            case R.id.CheckBox4:
                if (LK.isChecked())
                    Toast.makeText(getApplicationContext(), "Python", Toast.LENGTH_LONG).show();
                    AK.setChecked(false);
                    JP.setChecked(false);
                    Sibuk.setChecked(false);
                break;
        }
    }
}

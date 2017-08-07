package com.bahwell.inoncharge.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bahwell.inoncharge.R;
import com.bahwell.inoncharge.activity.OrderPayActivity;
import com.bahwell.inoncharge.activity.WorkOrientedActivity;
import com.bahwell.inoncharge.other.SessionKategori;

import org.joda.time.DateTime;
import org.joda.time.Seconds;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by INDRA on 14/07/2017.
 */

public class NowOrder extends Fragment {
//    private Spinner spinner;
Button btnTimePicker, btnTimePicker2;
    TextView txtTime, txtTime2, txtTotalWaktu, name;
    LinearLayout rl1, rl2;
    RelativeLayout responsivLay;
    de.hdodenhof.circleimageview.CircleImageView pp;
    private int mHour, mMinute, mHour2, mMinute2;
    String jamAwal,jamAhir, _time_awal, _time_ahir;
    int selisih_waktu_tmp;

    boolean jamOrHari;

    SessionKategori sessionKategori;


    public NowOrder() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_now_order, container, false);

        sessionKategori = new SessionKategori(getContext().getApplicationContext());
        name = (TextView) rootView.findViewById(R.id.TV2);

        String title = sessionKategori.getKategoriName();
        name.setText(title);

        rl2 = (LinearLayout) rootView.findViewById(R.id.rl2);

        btnTimePicker=(Button)rootView.findViewById(R.id.btn_time);
        btnTimePicker2=(Button) rootView.findViewById(R.id.btn_time2);

        txtTime=(TextView) rootView.findViewById(R.id.in_time);
        txtTime2=(TextView) rootView.findViewById(R.id.in_time2);

        txtTotalWaktu=(TextView) rootView.findViewById(R.id.TW);

        btnTimePicker.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String AM_PM ;
                                if(hourOfDay < 12) {
                                    AM_PM = "AM";
                                } else {
                                    AM_PM = "PM";
                                }
                                _time_awal = hourOfDay + ":" + minute;
                                txtTime.setText(_time_awal + " " + AM_PM);
                                jamAwal = hourOfDay + ":" + minute + ":00";
                                hitungTotalWaktuDalamJam();
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });
        btnTimePicker2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final Calendar c = Calendar.getInstance();
                mHour2 = c.get(Calendar.HOUR_OF_DAY);
                mMinute2 = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String AM_PM2 ;
                                if(hourOfDay < 12) {
                                    AM_PM2 = "AM";
                                } else {
                                    AM_PM2 = "PM";
                                }
                                _time_ahir = hourOfDay + ":" + minute;
                                txtTime2.setText(_time_ahir + " " + AM_PM2);
                                jamAhir = hourOfDay + ":" + minute + ":00";
                                hitungTotalWaktuDalamJam();
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

//        String [] values = {"Time Of Service","One Hour","Two Hour","Three Hour","Four Hour","Five Hour","Six Hour","Seven Hour","Eight Hour",};
//        spinner = (Spinner) rootView.findViewById(R.id.spinner1);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, values);
//        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
//        spinner.setAdapter(adapter);

        return rootView;
    }

    private void hitungTotalWaktuDalamJam(){
        try {
            SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
            DateTime dtAwal = new DateTime(sdfTime.parse(jamAwal));
            DateTime dtAkhir = new DateTime(sdfTime.parse(jamAhir));
            int selisih_detik = Math.abs(Seconds.secondsBetween(dtAwal, dtAkhir).getSeconds());

            int jam = selisih_detik / 3600;
            selisih_detik %= 3600;

            int menit = 0;
            int detik = 0;
            int tmpJam = 0;
            if(selisih_detik >= 60) {
                menit = selisih_detik / 60;
                detik = selisih_detik % 60;
                tmpJam = 1;
            } else {
                detik = selisih_detik;
            }
            selisih_waktu_tmp = jam + tmpJam;
            String selisih_waktu = selisih_waktu_tmp+" ("+jam+":"+menit+") jam";
            txtTotalWaktu.setText(selisih_waktu);
        } catch (Exception e){

        }
    }

}

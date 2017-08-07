package com.bahwell.inoncharge.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.bahwell.inoncharge.R;
import com.bahwell.inoncharge.other.lib;

import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class OrderPayActivity extends AppCompatActivity {

    Button btnDatePicker, btnDatePicker2, btnDatePicker3, btnTimePicker, btnTimePicker2, pay;
    TextView txtDate, txtDate2, txtDate3, txtTime, txtTime2, name, txtKategori, txtHagra,
            txtTotalWaktu, txtTotalHarga;
    LinearLayout rl1, rl2;
    RelativeLayout responsivLay;
    de.hdodenhof.circleimageview.CircleImageView pp;
    private int mYear, mMonth, mDay, mHour, mMinute, mHour2, mMinute2;

    String jamAwal,jamAhir, _date_awal, _date_ahir, _date, _time_awal, _time_ahir;
    int selisih_waktu_tmp, totalHarga;
    boolean jamOrHari;
    String _ID, _NAME, _TITLE, _HARGA, _TOKEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pay);

        responsivLay = (RelativeLayout) findViewById(R.id.ResponsivLayout);

        rl1 = (LinearLayout) findViewById(R.id.rl1);
        rl2 = (LinearLayout) findViewById(R.id.rl2);

        rl1.setVisibility(View.GONE);
        rl2.setVisibility(View.GONE);

        btnDatePicker=(Button)findViewById(R.id.btn_date);
        btnTimePicker=(Button)findViewById(R.id.btn_time);
        btnDatePicker2=(Button)findViewById(R.id.btn_date2);
        btnTimePicker2=(Button)findViewById(R.id.btn_time2);
        btnDatePicker3=(Button) findViewById(R.id.btn_date3);
        pay=(Button) findViewById(R.id.reg_merchant_button);

        txtDate=(TextView) findViewById(R.id.in_date);
        txtTime=(TextView) findViewById(R.id.in_time);
        txtDate2=(TextView) findViewById(R.id.in_date2);
        txtTime2=(TextView) findViewById(R.id.in_time2);
        txtDate3=(TextView) findViewById(R.id.in_date3);
        txtKategori=(TextView) findViewById(R.id.TV2);
        txtHagra=(TextView) findViewById(R.id.TV3);
        txtTotalWaktu=(TextView) findViewById(R.id.TW);
        txtTotalHarga=(TextView) findViewById(R.id.TB);

        pp = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.profile_image);
        name = (TextView) findViewById(R.id.name);

        _ID = getIntent().getExtras().getString("_ID");
        _NAME = getIntent().getExtras().getString("_NAME");
        _TITLE = getIntent().getExtras().getString("_TITLE");
        _HARGA = getIntent().getExtras().getString("_HARGA");
        _TOKEN = getIntent().getExtras().getString("_TOKEN");

        Bitmap photo = new lib(this).getPhotoToStorage(_ID);
        pp.setImageBitmap(photo);
        name.setText(_NAME);
        txtKategori.setText(_TITLE);
        txtHagra.setText(_HARGA + " / hour");

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(OrderPayActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                _date_awal = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                txtDate.setText(_date_awal);
                                hitungTotalWaktuDalamHari();
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        btnDatePicker2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(OrderPayActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                _date_ahir = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                txtDate2.setText(_date_ahir);
                                hitungTotalWaktuDalamHari();
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        btnDatePicker3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(OrderPayActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                _date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                txtDate3.setText(_date);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        btnTimePicker.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(OrderPayActivity.this,
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
                TimePickerDialog timePickerDialog = new TimePickerDialog(OrderPayActivity.this,
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

        pay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                goToMapsActivity();
            }
        });

        pay.setEnabled(false);
    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_hari:
                if (checked)
                    rl1.setVisibility(View.VISIBLE);
                rl2.setVisibility(View.GONE);
                setRefresCheckedRadioButton();
                jamOrHari = true;
                break;
            case R.id.radio_jam:
                if (checked)
                    rl1.setVisibility(View.GONE);
                rl2.setVisibility(View.VISIBLE);
                setRefresCheckedRadioButton();
                jamOrHari = false;
                break;
        }
    }

    private  void setRefresCheckedRadioButton(){
        pay.setEnabled(false);

        txtDate.setText(null);
        txtDate2.setText(null);
        txtDate3.setText(null);
        txtTime.setText(null);
        txtTime2.setText(null);

        txtTotalWaktu.setText(null);
        txtTotalHarga.setText(null);
    }

    private static long daysBetween(Calendar tanggalAwal, Calendar tanggalAkhir) {
        long lama = 0;
        Calendar tanggal = (Calendar) tanggalAwal.clone();
        while (tanggal.before(tanggalAkhir)) {
            tanggal.add(Calendar.DAY_OF_MONTH, 1);
            lama++;
        }
        return lama;
    }

    private void hitungTotalWaktuDalamHari(){
        try{
            String tglAwal = txtDate.getText().toString().trim();
            String tglAhir = txtDate2.getText().toString().trim();
            DateFormat date = new SimpleDateFormat("dd/MM/yyyy");
            Date tglAwalTemp = date.parse(tglAwal);
            Date tglAhirTemp = date.parse(tglAhir);

            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(tglAwalTemp);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(tglAhirTemp);

            String sTelat = String.valueOf(daysBetween(cal1, cal2));
            int sTelatInt = Integer.parseInt(sTelat);
            selisih_waktu_tmp = sTelatInt * 8;
            txtTotalWaktu.setText(selisih_waktu_tmp + " jam");
        }catch(Exception e){

        }
        hitungHargaTotal();
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
        hitungHargaTotal();
    }

    private void hitungHargaTotal(){
        int harga = Integer.parseInt(getIntent().getExtras().getString("_HARGA"));
        totalHarga = harga * selisih_waktu_tmp;
        txtTotalHarga.setText(totalHarga + " IDR");
        pay.setEnabled(true);
    }

    private void  goToMapsActivity(){
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("_NAME",_NAME);
        intent.putExtra("_HARGA",_HARGA);
        intent.putExtra("_HARGA_TOTAL",totalHarga);
        intent.putExtra("_ID",_ID);
        intent.putExtra("_TOKEN",_TOKEN);
        intent.putExtra("_TITLE",_TITLE);
        intent.putExtra("_hariOrJam", jamOrHari);
        if (jamOrHari){
            intent.putExtra("_date_awal", _date_awal);
            intent.putExtra("_date_ahir", _date_ahir);
        }else {
            intent.putExtra("_date", _date);
            intent.putExtra("_time_awal", _time_awal);
            intent.putExtra("_time_ahir", _time_ahir);
        }
        startActivity(intent);
    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private void sendNotification(final String reg_token) {
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    OkHttpClient client = new OkHttpClient();
                    JSONObject json = new JSONObject();
                    JSONObject dataJson = new JSONObject();
                    JSONObject data = new JSONObject();

                    data.put("_ID", _ID);
                    data.put("_NAME", _NAME);
                    data.put("_TOTAL_HARGA", totalHarga);
                    data.put("_TITLE", _TITLE);
                    data.put("_hariOrJam", jamOrHari);
                    if (jamOrHari){
                        data.put("_date_awal", _date_awal);
                        data.put("_date_ahir", _date_ahir);
                    }else {
                        data.put("_date", _date);
                        data.put("_time_awal", _time_awal);
                        data.put("_time_ahir", _time_ahir);
                    }

                    dataJson.put("body",_NAME+ " ingin memboking anda");
                    dataJson.put("title",_TITLE);

                    json.put("notification",dataJson);
                    json.put("data",data);
                    json.put("to",reg_token);

                    RequestBody body = RequestBody.create(JSON, json.toString());
                    Request request = new Request.Builder()
                            .header("Authorization","key=AIzaSyBX75fFT_2RvmG-WQh1GfckLICcWipqRc8")
                            .url("https://fcm.googleapis.com/fcm/send")
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    String finalResponse = response.body().string();
                }catch (Exception e){
                    Log.d("send notf",e.getMessage());
                }
                return null;
            }
        }.execute();

    }
}

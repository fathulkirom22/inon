package com.bahwell.inoncharge.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bahwell.inoncharge.R;
import com.bahwell.inoncharge.other.GridViewAdapterBrowse;
import com.bahwell.inoncharge.other.SessionManagement;
import com.bahwell.inoncharge.other.lib;
import com.bahwell.inoncharge.services.MyJobService;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Trigger;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class OrderConfirmationActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    String _ID, _NAME, _TOTAL_HARGA, _TITLE, _hari_or_jam, _date_awal, _date_ahir, _date,
            _time_awal, _time_ahir, _ID_TRANSAKSI;
    TextView namaPelanggan, biayaTotal, kategori, tglMulai, tglSampai, jamMulai, jamSampai;
    Bitmap tempphoto;
    Button button, button2;
    de.hdodenhof.circleimageview.CircleImageView imgPP;

    SessionManagement sessionManagement;
    HashMap<String, String> user_my;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        namaPelanggan = (TextView) findViewById(R.id.namaPelanggan);
        biayaTotal = (TextView) findViewById(R.id.biayaTotal);
        kategori = (TextView) findViewById(R.id.kategori);
        tglMulai = (TextView) findViewById(R.id.tglMulai);
        tglSampai = (TextView) findViewById(R.id.tglSelesai);
        jamMulai = (TextView) findViewById(R.id.jamMulai);
        jamSampai = (TextView) findViewById(R.id.jamSampai);

        sessionManagement = new SessionManagement(getApplicationContext());
        user_my = sessionManagement.getUserDetails();

        button = (Button) findViewById(R.id.reg_merchant_button1);
        button2 = (Button) findViewById(R.id.reg_merchant_button2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStatusTransaksi("ditolak");
                Intent intent = new Intent(OrderConfirmationActivity.this, CancelReasonActivity.class);
                startActivity(intent);

                //sementara
                finish();

            }
        });


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStatusTransaksi("diterima");

                //sementara
                finish();

            }
        });







        imgPP= (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.photoProfil);

        onNewIntent(getIntent());

        loadImageProfilUser(_ID);
        namaPelanggan.setText(_NAME);
        biayaTotal.setText(_TOTAL_HARGA);
        kategori.setText(_TITLE);

        if (_hari_or_jam.equals("true")){
            tglMulai.setText(_date_awal);
            tglSampai.setText(_date_ahir);
            jamMulai.setText("07:00");
            jamSampai.setText("15:00");
        }
        else if (_hari_or_jam.equals("false")){
            tglMulai.setText(_date);
            tglSampai.setText(_date);
            jamMulai.setText(_time_awal);
            jamSampai.setText(_time_ahir);
        }

    }

    public void updateStatusTransaksi(String status){
        Log.d("cek child status", _ID+" / "+user_my.get(SessionManagement.KEY_ID)+" / "+_ID_TRANSAKSI);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("transaksi")
                .child("user")
                .child(_ID)
                .child(user_my.get(SessionManagement.KEY_ID))
                .child(_ID_TRANSAKSI)
                .child("status")
                .setValue(status);
    }

    @Override
    public void onNewIntent(Intent intent){
        Bundle extras = intent.getExtras();
        String tabNumber;

        if(extras != null) {
            _ID             = extras.getString("_ID");
            _ID_TRANSAKSI   = extras.getString("_ID_TRANSAKSI");
            _NAME           = extras.getString("_NAME");
            _TOTAL_HARGA    = extras.getString("_TOTAL_HARGA");
            _TITLE          = extras.getString("_TITLE");
            _hari_or_jam    = extras.getString("_hari_or_jam");

            if (_hari_or_jam.equals("true")){
                _date_awal = extras.getString("_date_awal");
                _date_ahir = extras.getString("_date_ahir");
            }
            else if (_hari_or_jam.equals("false")){
                _date = extras.getString("_date");
                _time_awal = extras.getString("_time_awal");
                _time_ahir = extras.getString("_time_ahir");
            }
        } else {
            Log.d("TEMP", "Extras are NULL");

        }
    }

    public void loadImageProfilUser(String id){
        String url = "gs://inon-charge.appspot.com/"+id+"/photo_profil.jpg";
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(url);
        final long ONE_MEGABYTE = 1024 * 1024;
        storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                if (bytes!=null){
                    // compress http://voidcanvas.com/whatsapp-like-image-compression-in-android/
                    try {
                        tempphoto = new AsyncTaskEx(bytes).execute().get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    imgPP.setImageBitmap(tempphoto);
                } else {
                    tempphoto = new lib(OrderConfirmationActivity.this).getDefautlPhotoProfil();
                    imgPP.setImageBitmap(tempphoto);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                tempphoto = new lib(OrderConfirmationActivity.this).getDefautlPhotoProfil();
                imgPP.setImageBitmap(tempphoto);
            }
        });
    }

    private class AsyncTaskEx extends AsyncTask<Void, Void, Bitmap> {

        private byte[] by;

        public AsyncTaskEx(byte[] bytes){
            super();
            this.by = bytes;
        }

        public void setBy(byte[] bytes){
            this.by = bytes;
        }

        public  byte[] getBy(){
            return this.by;
        }

        @Override
        protected Bitmap doInBackground(Void... arg0) {
            Bitmap bitmap = decodeSampledBitmapFromResource(100, 100);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            //Write some code you want to execute on UI after doInBackground() completes
            return ;
        }

        public int calculateInSampleSize(
                BitmapFactory.Options options, int reqWidth, int reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {

                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while ((halfHeight / inSampleSize) >= reqHeight
                        && (halfWidth / inSampleSize) >= reqWidth) {
                    inSampleSize *= 2;
                }
            }
            return inSampleSize;
        }

        public Bitmap decodeSampledBitmapFromResource(int reqWidth, int reqHeight) {

            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(getBy(),0, getBy().length, options);
            String imageType = options.outMimeType;

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeByteArray(getBy(),0, getBy().length, options);
        }

    }

}

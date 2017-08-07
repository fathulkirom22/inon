package com.bahwell.inoncharge.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.bahwell.inoncharge.R;
import com.bahwell.inoncharge.fragment.BookingOrder;
import com.bahwell.inoncharge.other.GridViewAdapterBrowse;
import com.bahwell.inoncharge.other.Transaksi;
import com.bahwell.inoncharge.other.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WaitingOrderActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    public String _ID_MY, _ID_MERCHANT, _ID_TRANSAKSI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_order);

        _ID_MY = getIntent().getExtras().getString("_ID_MY");
        _ID_MERCHANT = getIntent().getExtras().getString("_ID_MERCHANT");
        _ID_TRANSAKSI = getIntent().getExtras().getString("_ID_TRANSAKSI");

        new syncListenerChangeStatus(_ID_MY, _ID_MERCHANT, _ID_TRANSAKSI).execute();

    }


    private class syncListenerChangeStatus extends AsyncTask<Void, Void, Void> {

        public String _ID_MY, _ID_MERCHANT, _ID_TRANSAKSI;

        public syncListenerChangeStatus(String _ID_MY, String _ID_MERCHANT, String _ID_TRANSAKSI){
            super();
            this._ID_MY = _ID_MY;
            this._ID_MERCHANT = _ID_MERCHANT;
            this._ID_TRANSAKSI = _ID_TRANSAKSI;
        }

        @Override
        protected Void doInBackground(Void... params) {
            listenerChangeStatus();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}

        public void listenerChangeStatus(){
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("transaksi")
                    .child("user")
                    .child(_ID_MY)
                    .child(_ID_MERCHANT)
                    .child(_ID_TRANSAKSI)
                    .addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    // Get user value
                                    Transaksi transaksi = dataSnapshot.getValue(Transaksi.class);

                                    Log.d("Status", transaksi.status);
                                    if (!transaksi.status.equals("menunggu")){
                                        Log.d("Status test", transaksi.status);
                                        startActivityNotificationActivity(transaksi.status);
                                        finish();
                                    } else {
                                        listenerChangeStatus();
                                    }

                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.w("getUser:onCancelled", databaseError.toException());
                                    // ...
                                }
                            });
        }

        public void startActivityNotificationActivity(String status)
        {
            Intent intent = new Intent(WaitingOrderActivity.this, NotificationActivity.class);
            intent.putExtra("status", status);
            startActivity(intent);
        }
    }
}

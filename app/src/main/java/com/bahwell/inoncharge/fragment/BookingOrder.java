package com.bahwell.inoncharge.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.bahwell.inoncharge.R;
import com.bahwell.inoncharge.other.DataListGrid;
import com.bahwell.inoncharge.other.GridViewAdapterBrowse;
import com.bahwell.inoncharge.other.SessionKategori;
import com.bahwell.inoncharge.other.lib;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by INDRA on 14/07/2017.
 */

public class BookingOrder extends Fragment implements View.OnClickListener {
    private List<DataListGrid> listgambar;

    private DatabaseReference mDatabase;
    private GridView gridView;
    private GridViewAdapterBrowse gridViewAdapter;
    private ArrayList<String> idMerchants;
    SessionKategori sessionKategori;

    public BookingOrder() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.my_gridview, container, false);

        sessionKategori = new SessionKategori(getContext().getApplicationContext());

        final String title = sessionKategori.getKategoriName();

//        final String title = getArguments().getString("title");
        mDatabase = FirebaseDatabase.getInstance().getReference("kategory").child(title);
        System.out.println("Before adding listener");
        mDatabase.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("In onDataChange");
                long count = dataSnapshot.getChildrenCount();
                String strLong = Long.toString(count);
                System.out.println(strLong);

                collectUserId((Map<String,Object>) dataSnapshot.getValue());

                getProductList();
                setAdapters(title);
            }
            public void onCancelled(DatabaseError databaseError) { }
        });
        System.out.println("After adding listener");

        gridView = (GridView) rootView.findViewById(R.id.mygridview);
        gridView.setNestedScrollingEnabled(true);
        return rootView;
    }

    private void collectUserId(Map<String,Object> kategory) {
        idMerchants = new ArrayList<>();
        //iterate through each user, ignoring their UID
        try {
            for (Map.Entry<String, Object> entry : kategory.entrySet()){
                System.out.println(entry.getKey());
                idMerchants.add(entry.getKey());
            }
            System.out.println(idMerchants.toString());
        } catch (Exception e){
            new lib(this.getActivity()).showAlert("not available service");
        }

    }

    private void setAdapters(String title) {
        gridViewAdapter = new GridViewAdapterBrowse(
                this.getActivity(),
                R.layout.fragment_booking_order,
                listgambar,
                title
        );
        gridView.setAdapter(gridViewAdapter);

//        getProductList();
//        new LongOperation(this.getActivity(),title).execute();
    }

    private class LongOperation extends AsyncTask<Void, Void, GridViewAdapterBrowse> {

        private Context context;
        private String title;

        public LongOperation(Context c, String title){
            super();
            this.context = c;
            this.title = title;
        }

        public String getTitel(){
            return this.title;
        }

        public void setTitle(String title){
            this.title = title;
        }

        @Override
        protected GridViewAdapterBrowse doInBackground(Void... params) {
            GridViewAdapterBrowse gvab =  new GridViewAdapterBrowse(context, R.layout.fragment_booking_order, listgambar, getTitel());
            return gvab;
        }

        @Override
        protected void onPostExecute(GridViewAdapterBrowse result) {
//            TextView txt = (TextView) findViewById(R.id.output);
//            txt.setText("Executed"); // txt.setText(result);
            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
            gridView.setAdapter(result);
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    @Override
    public void onClick(View v) {

    }

    public List<DataListGrid> getProductList() {
        listgambar = new ArrayList<>();
        ArrayList<String> urls = new ArrayList<>();
        for (String object: idMerchants) {
            //object = id merchant
            System.out.println(object);
            listgambar.add(new DataListGrid("gs://inon-charge.appspot.com/"+object+"/photo_profil.jpg",object));
        }
        return listgambar;
    }



}

package com.bahwell.inoncharge.other;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bahwell.inoncharge.R;
import com.bahwell.inoncharge.activity.BrowserMerchantActivity;
import com.bahwell.inoncharge.activity.InfoUserActivity;
import com.bahwell.inoncharge.activity.MainActivity;
import com.bahwell.inoncharge.activity.SplashActivity;
import com.bahwell.inoncharge.fragment.data_info;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * Created by NgocTri on 10/22/2016.
 */

public class GridViewAdapterBrowse extends ArrayAdapter<DataListGrid> {

    Bitmap tempphoto;
    private String title_new, harga_tmp;
    Context context;
    SessionMerchant sessionMerchant;

    public void setTitleNew(String v){
        this.title_new = v;
    }

    public String getTitleNew(){
        return this.title_new;
    }

    public GridViewAdapterBrowse(Context context,
                                 int resource,
                                 List<DataListGrid> objects,
                                 String title
    ) {
        super(context, resource, objects);
        this.context = context;
        setTitleNew(title);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (null == v) {
            LayoutInflater inflater =
                    (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.fragment_booking_order, null);
        }
        sessionMerchant = new SessionMerchant(getContext().getApplicationContext());
        DataListGrid product = getItem(position);
        final ImageView img = (ImageView) v.findViewById(R.id.imageView1);
        final TextView label = (TextView) v.findViewById(R.id.title_nama1);
        final TextView em = (TextView) v.findViewById(R.id.emailMerchant1);
        final TextView pm = (TextView) v.findViewById(R.id.phoneMerchant1);
        final TextView adm = (TextView) v.findViewById(R.id.addressMerchant1);
        final TextView token = (TextView) v.findViewById(R.id.tokenMercant1);
        final TextView harga = (TextView) v.findViewById(R.id.harga1);
        final String title = product.getTitle();
//
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(product.getUrl());
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
                    new lib(getContext()).savePhotoToStorage(title,bytes);
                    img.setImageBitmap(tempphoto);

                } else {
                    tempphoto = new lib(getContext()).getDefautlPhotoProfil();
                    new lib(getContext()).savePhotoToStorage(title,tempphoto);
                    img.setImageBitmap(tempphoto);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                tempphoto = new lib(getContext()).getDefautlPhotoProfil();
                new lib(getContext()).savePhotoToStorage(title,tempphoto);
                img.setImageBitmap(tempphoto);
            }
        });

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(title).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        token.setText(user.pushToken);
                        label.setText(user.Name);
                        em.setText(user.Email);
                        pm.setText(user.Phone);
                        adm.setText(user.Address);

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("getUser:onCancelled", databaseError.toException());
                        // ...
                    }
                });

        mDatabase.child("kategory").child(getTitleNew()).child(title).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Kategory kategory = dataSnapshot.getValue(Kategory.class);
                        harga_tmp = kategory.pricehour;
                        harga.setText(kategory.pricehour);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("getUser:onCancelled", databaseError.toException());
                        // ...
                    }
                });

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempNama = label.getText().toString();
                String tempEmail = em.getText().toString();
                String tempPhone = pm.getText().toString();
                String tempAddress = adm.getText().toString();
                String tempHarga = harga.getText().toString();
                String tokenMercant = token.getText().toString();
                Intent intent = new Intent(getContext(), InfoUserActivity.class);
                intent.putExtra("_NAME",tempNama);
                sessionMerchant.setName(tempNama);
                sessionMerchant.setEmail(tempEmail);
                sessionMerchant.setAddress(tempAddress);
                sessionMerchant.setPhone(tempPhone);
                intent.putExtra("_HARGA",tempHarga);
                intent.putExtra("_ID",title);
                intent.putExtra("_TOKEN",tokenMercant);
                intent.putExtra("_TITLE",getTitleNew());
                getContext().startActivity(intent);
            }
        });
        return v;
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
            Bitmap bitmap = decodeSampledBitmapFromResource(200, 200);
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
package com.bahwell.inoncharge.other;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bahwell.inoncharge.R;
import com.bahwell.inoncharge.activity.ChatActivity;
import com.bahwell.inoncharge.activity.InfoUserActivity;
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

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

/**
 * Created by bahwell on 04/08/17.
 */

public class ListViewAdapterChat extends ArrayAdapter<DataListGrid> {

    Bitmap tempphoto;
//    de.hdodenhof.circleimageview.CircleImageView img;
//    TextView nama;

    public ListViewAdapterChat(Context context, int resource, List<DataListGrid> obj) {
        super(context, resource, obj);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if(null == v) {
            LayoutInflater inflater = (LayoutInflater)getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_user_chat, null);
        }
        DataListGrid product = getItem(position);

//        final Bitmap tempphoto;
        final de.hdodenhof.circleimageview.CircleImageView img = (de.hdodenhof.circleimageview.CircleImageView) v.findViewById(R.id.user_profile_photo);
        final TextView nama = (TextView) v.findViewById(R.id.list_title);
        final String title = product.getTitle();
        final String url = product.getUrl();
        TextView id = (TextView) v.findViewById(R.id.list_id);

        id.setText(product.getTitle());
        img.setImageBitmap(new lib(getContext()).getPhotoToStorage(product.getTitle()));

        Log.d("aaa",product.getTitle());

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                nama.setText(user.Name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.child("users").child(product.getTitle()).addValueEventListener(postListener);

//
//        .addListenerForSingleValueEvent(
//                new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        User user = dataSnapshot.getValue(User.class);
//
//                        nama.setText(user.Name);
////                        Log.d("bbbbbbbbb",user.Name);
//                    }
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        Log.w("getUser:onCancelled", databaseError.toException());
//                        // ...
//                    }
//                });
//        new AsyncTaskLoadFromFirebase(product.getTitle(), product.getUrl()).execute();

//        loadFromFirebase(product.getTitle(), product.getUrl());

//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageRef = storage.getReferenceFromUrl(url);
//        final long ONE_MEGABYTE = 1024 * 1024;
//        storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//            @Override
//            public void onSuccess(byte[] bytes) {
//                if (bytes!=null){
//                    try {
//                        tempphoto = new AsyncTaskPhotoProfil(bytes).execute().get();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    } catch (ExecutionException e) {
//                        e.printStackTrace();
//                    }
//                    img.setImageBitmap(tempphoto);
//
//                } else {
//                    tempphoto = new lib(getContext()).getDefautlPhotoProfil();
//                    img.setImageBitmap(tempphoto);
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                tempphoto = new lib(getContext()).getDefautlPhotoProfil();
//                img.setImageBitmap(tempphoto);
//            }
//        });



//        v.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getContext().startActivity(new Intent(getContext(), ChatActivity.class));
//            }
//        });

        return v;
    }

//    private void loadFromFirebase(String id, String urlPhoto){
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageRef = storage.getReferenceFromUrl(urlPhoto);
//        final long ONE_MEGABYTE = 1024 * 1024;
//        storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//            @Override
//            public void onSuccess(byte[] bytes) {
//                if (bytes!=null){
//                    try {
//                        tempphoto = new AsyncTaskPhotoProfil(bytes).execute().get();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    } catch (ExecutionException e) {
//                        e.printStackTrace();
//                    }
//                    img.setImageBitmap(tempphoto);
//
//                } else {
//                    tempphoto = new lib(getContext()).getDefautlPhotoProfil();
//                    img.setImageBitmap(tempphoto);
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                tempphoto = new lib(getContext()).getDefautlPhotoProfil();
//                img.setImageBitmap(tempphoto);
//            }
//        });
//
//        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
//        mDatabase.child("users").child(id).addListenerForSingleValueEvent(
//                new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        User user = dataSnapshot.getValue(User.class);
//                        nama.setText(user.Name);
//                    }
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        Log.w("getUser:onCancelled", databaseError.toException());
//                        // ...
//                    }
//                });
//    }

//    private class AsyncTaskLoadFromFirebase extends AsyncTask<Void, Void, Void> {
//
//        String id, url;
//
//        public AsyncTaskLoadFromFirebase(String id, String url){
//            super();
//            this.id = id;
//            this.url = url;
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//
//            String urlPhoto = url;
//
//            FirebaseStorage storage = FirebaseStorage.getInstance();
//            StorageReference storageRef = storage.getReferenceFromUrl(urlPhoto);
//            final long ONE_MEGABYTE = 1024 * 1024;
//            storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//                @Override
//                public void onSuccess(byte[] bytes) {
//                    if (bytes!=null){
//                        try {
//                            tempphoto = new AsyncTaskPhotoProfil(bytes).execute().get();
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        } catch (ExecutionException e) {
//                            e.printStackTrace();
//                        }
//                        img.setImageBitmap(tempphoto);
//
//                    } else {
//                        tempphoto = new lib(getContext()).getDefautlPhotoProfil();
//                        img.setImageBitmap(tempphoto);
//                    }
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
//                    tempphoto = new lib(getContext()).getDefautlPhotoProfil();
//                    img.setImageBitmap(tempphoto);
//                }
//            });
//
//            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
//            mDatabase.child("users").child(id).addListenerForSingleValueEvent(
//                    new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            User user = dataSnapshot.getValue(User.class);
//                            nama.setText(user.Name);
//                        }
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//                            Log.w("getUser:onCancelled", databaseError.toException());
//                            // ...
//                        }
//                    });
//
//            return null;
//        }
//    }

    private class AsyncTaskPhotoProfil extends AsyncTask<Void, Void, Bitmap> {

        private byte[] by;

        public AsyncTaskPhotoProfil(byte[] bytes){
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

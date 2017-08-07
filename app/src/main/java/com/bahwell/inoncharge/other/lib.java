package com.bahwell.inoncharge.other;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.bahwell.inoncharge.R;
import com.bahwell.inoncharge.activity.OrderConfirmationActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import static android.content.ContentValues.TAG;

/**
 * Created by bahwell on 31/05/17.
 */

public class lib {

    Context context;
    Bitmap tmpPhotoProfil;
    String PHOTO_PROFIL = "PHOTO_PROFIL";


    public lib(Context current){
        this.context = current;
    }

    public Bitmap getTmpPhotoProfil(){
        return tmpPhotoProfil;
    }

    public void setTmpPhotoProfil(Bitmap b){
        this.tmpPhotoProfil = b;
    }

    public void getPhotoProfilFromFirebase(final String id){

        String url = "gs://inon-charge.appspot.com/"+id+"/photo_profil.jpg";

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(url);
        final long ONE_MEGABYTE = 1024 * 1024;
        storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] obytes) {
                if (obytes!=null){
                    savePhotoToStorage(id,obytes);
                } else {

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });

    }

    public void savePhotoProfilToStorage(byte[] bytes){
//        storeImage(bitmap, PHOTO_PROFIL);
        String filename = PHOTO_PROFIL;
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(bytes);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void savePhotoToStorage(String s,Bitmap b){
        String filename = s;
        FileOutputStream outputStream;
        try {

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            byte[] bytes = stream.toByteArray();

            outputStream = new FileOutputStream(new File(context.getCacheDir(), filename));
            outputStream.write(bytes);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void savePhotoToStorage(String s,byte[] b){
        String filename = s;
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(new File(context.getCacheDir(), filename));
            outputStream.write(b);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap getPhotoToStorage(String s){
        File file = new File(context.getCacheDir(), s);

        if (!file.exists()){
            getPhotoProfilFromFirebase(s);
            getPhotoToStorage(s);
        }

        byte[] b = convertFileToByteArray(file);
        Bitmap bitmap = null;
        try {
            bitmap = new AsyncTaskEx(b, 200, 200).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public Bitmap getPhotoProfilToStorage(){
        File file = new File("/data/user/0/"
                +context.getApplicationContext().getPackageName()
                +"/files/"
                +PHOTO_PROFIL
        );
        byte[] bytesArray = convertFileToByteArray(file);
        try {
            tmpPhotoProfil = new AsyncTaskEx(bytesArray, 200, 200).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return tmpPhotoProfil;

    }

    public Bitmap getPhotoToStorageBig(String s){
        File file = new File(context.getCacheDir(), s);

        if (!file.exists()){
            getPhotoProfilFromFirebase(s);
            getPhotoToStorageBig(s);
        }

        byte[] b = convertFileToByteArray(file);
        Bitmap bitmap = null;
        try {
            bitmap = new AsyncTaskEx(b, 700, 700).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    public Bitmap getPhotoProfilToStorageBig(){
        File file = new File("/data/user/0/"
                +context.getApplicationContext().getPackageName()
                +"/files/"
                +PHOTO_PROFIL
        );
        byte[] bytesArray = convertFileToByteArray(file);
        try {
            tmpPhotoProfil = new AsyncTaskEx(bytesArray, 700, 700).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return tmpPhotoProfil;

    }

    /** Create a File for saving an image or video */
    public File getOutputMediaFile(String nameFile){
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + context.getApplicationContext().getPackageName()
                + "/Files");
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        File mediaFile;
        String mImageName = nameFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    public void storeImage(Bitmap image, String nameFile) {
        File pictureFile = getOutputMediaFile(nameFile);
        if (pictureFile == null) {
            Log.d(TAG,
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }

    public static byte[] convertFileToByteArray(File f)
    {
        byte[] byteArray = null;
        try
        {
            InputStream inputStream = new FileInputStream(f);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024*8];
            int bytesRead =0;

            while ((bytesRead = inputStream.read(b)) != -1)
            {
                bos.write(b, 0, bytesRead);
            }

            byteArray = bos.toByteArray();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return byteArray;
    }

    /**
     * Method to show alert dialog
     * */
    public void showAlert(String message) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(message).setTitle("Response")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // do nothing
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } catch (Exception e){
            Log.d("dialog", e.getMessage());
        }

    }

    public void getOnlinePhotoProfil(){
        saveOfflinePhotoProfil();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        StorageReference islandRef = storage.getReference().child(user.getUid()+"/photo_profil.jpg");
        final long ONE_MEGABYTE = 1024 * 1024;
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                if (bytes!=null){
                    try {
                        tmpPhotoProfil = new AsyncTaskEx(bytes, 200, 200).execute().get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    savePhotoProfilToStorage(bytes);
                } else {
                    saveOfflinePhotoProfil();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                //showAlert("Failed change photo profil");
                saveOfflinePhotoProfil();
            }
        });

    }

    public void  saveOfflinePhotoProfil(){
        tmpPhotoProfil = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        tmpPhotoProfil.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        byte[] byteArray = stream.toByteArray();
        savePhotoProfilToStorage(byteArray);
    }

    public Bitmap getDefautlPhotoProfil(){
        tmpPhotoProfil = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);
        return tmpPhotoProfil;
    }

    private class AsyncTaskEx extends AsyncTask<Void, Void, Bitmap> {

        private byte[] by;
        private int h,w;

        public AsyncTaskEx(byte[] bytes, int h, int w){
            super();
            this.by = bytes;
            this.h = h;
            this.w = w;
        }

        public void setBy(byte[] bytes){
            this.by = bytes;
        }

        public  byte[] getBy(){
            return this.by;
        }

        public void setH(int h){
            this.h = h;
        }

        public  int getH(){
            return this.h;
        }

        public void setW(int w){
            this.w = w;
        }

        public  int getW(){
            return this.w;
        }

        @Override
        protected Bitmap doInBackground(Void... arg0) {
            Bitmap bitmap = decodeSampledBitmapFromResource(getW(), getH());
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

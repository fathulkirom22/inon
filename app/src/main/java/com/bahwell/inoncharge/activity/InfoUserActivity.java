package com.bahwell.inoncharge.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bahwell.inoncharge.R;
import com.bahwell.inoncharge.fragment.data_info;
import com.bahwell.inoncharge.fragment.grid_portofolio;
import com.bahwell.inoncharge.fragment.list_promo;
import com.bahwell.inoncharge.other.SessionManagement;
import com.bahwell.inoncharge.other.TempData;
import com.bahwell.inoncharge.other.User;
import com.bahwell.inoncharge.other.lib;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class InfoUserActivity extends AppCompatActivity  implements View.OnClickListener {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Button btnPesan, setting ;
    private TextView txtDescription;
    private de.hdodenhof.circleimageview.CircleImageView imageView;
    private String _ID="";
    private String _NAME="";
    private String _TITLE="";
    private String _TOKEN="";
    private String _HARGA="";
    private Bitmap tempphoto;

    private LruCache<String, Bitmap> mMemoryCache;
    private LruCache<String, String> mMemoryCacheName;

    SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_user);

        session = new SessionManagement(getApplicationContext());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        btnPesan = (Button) findViewById(R.id.tombolPesan);
        btnPesan.setOnClickListener(this);
        setupTabIcons();

        txtDescription = (TextView) findViewById(R.id.txtDescription);
        imageView = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.imageView);


        try {
            _ID = getIntent().getExtras().getString("_ID");
            _NAME = getIntent().getExtras().getString("_NAME");
            _TITLE = getIntent().getExtras().getString("_TITLE");
            _HARGA = getIntent().getExtras().getString("_HARGA");
            _TOKEN = getIntent().getExtras().getString("_TOKEN");

            Log.d("Token mercant", _TOKEN);

            setTitle(_TITLE);
            Bitmap photo = new lib(this).getPhotoToStorage(_ID);
            imageView.setImageBitmap(photo);
            txtDescription.setText(_NAME);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(InfoUserActivity.this, TampilGambarActivity.class);
                    intent.putExtra("_ID",_ID);
                    startActivity(intent);
                }
            });
        }catch (Exception e){

            // get user data from session
            HashMap<String, String> user = session.getUserDetails();

            txtDescription.setText(user.get(SessionManagement.KEY_NAME));
            imageView.setImageBitmap(new lib(this).getPhotoProfilToStorage());
            btnPesan.setVisibility(View.GONE);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(InfoUserActivity.this, TampilGambarActivity.class);
                    startActivity(intent);
                }
            });
        }


    }

    public String getNameFromMemCache(String key) {
        return mMemoryCacheName.get(key);
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    /**
     * Adding custom view to tab
     */
    private void setupTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("Info");

        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Portofolio");

        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("Promo");

        tabLayout.getTabAt(2).setCustomView(tabThree);
    }

    /**
     * Adding fragments to ViewPager
     * @param viewPager
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new data_info(), "Info User");
        adapter.addFrag(new grid_portofolio(), "Galery user");
        adapter.addFrag(new list_promo(), "----");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tombolPesan:
                Intent intent = new Intent(InfoUserActivity.this, OrderPayActivity.class);
                intent.putExtra("_ID",_ID);
                intent.putExtra("_NAME",_NAME);
                intent.putExtra("_TOKEN",_TOKEN);
                intent.putExtra("_TITLE",_TITLE);
                intent.putExtra("_HARGA",_HARGA);
                startActivity(intent);
                break;

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private void sendNotification(final String reg_token) {
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    OkHttpClient client = new OkHttpClient();
                    JSONObject json=new JSONObject();
                    JSONObject dataJson=new JSONObject();
                    dataJson.put("body","Saya ingin memboking anda");
                    dataJson.put("title","Inon");
                    json.put("notification",dataJson);
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

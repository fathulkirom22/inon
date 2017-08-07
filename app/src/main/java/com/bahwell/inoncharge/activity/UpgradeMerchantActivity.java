package com.bahwell.inoncharge.activity;

import android.app.Fragment;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.bahwell.inoncharge.R;
import com.bahwell.inoncharge.fragment.HomeFragment;
import com.bahwell.inoncharge.fragment.UpgradeMerchaneKategoriFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UpgradeMerchantActivity extends AppCompatActivity {

//    private FragmentTransaction mFragmentTransaction;
//    private FragmentManager mFragmentManager;

    // Array of strings for ListView Title
    String[] listviewTitle = new String[]{
            "in-biro", "in-consult",
            "in-course", "in-design",
            "in-drive", "in-guide",
            "in-healthy", "in-message",
            "in-motiva", "in-rent",
            "in-save", "in-translate",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_merchant);

        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 12; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("listview_title", listviewTitle[i]);
            aList.add(hm);
        }
        String[] from = {"listview_title"};
        int[] to = {R.id.title_kategori};
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, aList, R.layout.list_select_kategori, from, to);
        ListView androidListView = (ListView) findViewById(R.id.list_select_kategori);
        androidListView.setAdapter(simpleAdapter);

//        mFragmentManager = getSupportFragmentManager();
//        mFragmentTransaction = mFragmentManager.beginTransaction();
//        mFragmentTransaction.replace(R.id.upgrade_merchan_parent, new UpgradeMerchaneKategoriFragment());
//        mFragmentTransaction.commit();

    }
}

package com.bahwell.inoncharge.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.bahwell.inoncharge.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpgradeMerchaneKategoriFragment extends Fragment {

    // Array of strings for ListView Title
    String[] listviewTitle = new String[]{
            "biro", "consult",
            "course", "design",
            "drive", "guide",
            "healthy", "message",
            "motiva", "rent",
            "save", "translate",
    };


    public UpgradeMerchaneKategoriFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upgrade_merchane_kategori, container, false);

        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 12; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("listview_title", listviewTitle[i]);
            aList.add(hm);
        }
        String[] from = {"listview_title"};
        int[] to = {R.id.title_kategori};
        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), aList, R.layout.list_select_kategori, from, to);
        ListView androidListView = (ListView) view.findViewById(R.id.list_select_kategori);
        androidListView.setAdapter(simpleAdapter);

        return view;
    }

}

package com.bahwell.inoncharge.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.bahwell.inoncharge.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiscountFragment extends Fragment {

    // Array of strings for ListView Title
    String[] listviewTitle = new String[]{
            "Discount 1", "Discount 2", "Discount 3",
    };


    int[] listviewImage = new int[]{
            R.drawable.iklan_satu, R.drawable.iklan_dua, R.drawable.iklan_tiga,
    };

    public DiscountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_discount, container, false);

        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 3; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("listview_title", listviewTitle[i]);
            hm.put("listview_image", Integer.toString(listviewImage[i]));
            aList.add(hm);
        }
        String[] from = {"listview_image", "listview_title"};
        int[] to = {R.id.list_discount_image, R.id.list_discount_title};
        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), aList, R.layout.list_discount, from, to);
        ListView androidListView = (ListView) view.findViewById(R.id.list_discount);
        androidListView.setAdapter(simpleAdapter);

        return view;
    }

}

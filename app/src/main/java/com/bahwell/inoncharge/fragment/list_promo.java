package com.bahwell.inoncharge.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.bahwell.inoncharge.R;
import com.bahwell.inoncharge.other.DataListGrid;
import com.bahwell.inoncharge.other.ListViewAdapterPromo;

import java.util.ArrayList;
import java.util.List;

public class list_promo extends Fragment implements View.OnClickListener {
    private List<DataListGrid> listgambar;
    private ListView listView;
    private ListViewAdapterPromo listViewAdapter;


    public list_promo() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.my_listview, container, false);

       listView = (ListView) rootView.findViewById(R.id.mylistview);
        listView.setNestedScrollingEnabled(true);
        getProductList();
        setAdapters();
//        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        toolbar.setVisibility(View.GONE);
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
//                Toast.makeText(getActivity(),  " - " , Toast.LENGTH_SHORT).show();
//            }
//        });
//        getProductList();
//        stubGrid.setVisibility(View.VISIBLE);
//        setAdapters();
        return rootView;
    }
    private void setAdapters() {
            listViewAdapter = new ListViewAdapterPromo(this.getActivity(), R.layout.list_promo, listgambar);
            listView.setAdapter(listViewAdapter);
    }
    @Override
    public void onClick(View view) {

    }
    public List<DataListGrid> getProductList() {
        listgambar = new ArrayList<>();
        listgambar.add(new DataListGrid(R.drawable.icon_android));
        listgambar.add(new DataListGrid(R.drawable.icon_android));
        listgambar.add(new DataListGrid(R.drawable.icon_android));
        listgambar.add(new DataListGrid(R.drawable.icon_android));
        listgambar.add(new DataListGrid(R.drawable.icon_android));
        listgambar.add(new DataListGrid(R.drawable.icon_android));
        listgambar.add(new DataListGrid(R.drawable.icon_android));
        listgambar.add(new DataListGrid(R.drawable.icon_android));
        listgambar.add(new DataListGrid(R.drawable.icon_android));
        listgambar.add(new DataListGrid(R.drawable.icon_android));
        listgambar.add(new DataListGrid(R.drawable.icon_android));
        listgambar.add(new DataListGrid(R.drawable.icon_android));
        listgambar.add(new DataListGrid(R.drawable.icon_android));
        listgambar.add(new DataListGrid(R.drawable.icon_android));
        listgambar.add(new DataListGrid(R.drawable.icon_android));
        listgambar.add(new DataListGrid(R.drawable.icon_android));
        listgambar.add(new DataListGrid(R.drawable.icon_android));
        listgambar.add(new DataListGrid(R.drawable.icon_android));
        listgambar.add(new DataListGrid(R.drawable.icon_android));
        listgambar.add(new DataListGrid(R.drawable.icon_android));
        listgambar.add(new DataListGrid(R.drawable.icon_android));
        listgambar.add(new DataListGrid(R.drawable.icon_android));
        listgambar.add(new DataListGrid(R.drawable.icon_android));
        listgambar.add(new DataListGrid(R.drawable.icon_android));
        listgambar.add(new DataListGrid(R.drawable.icon_android));
        listgambar.add(new DataListGrid(R.drawable.icon_android));
        listgambar.add(new DataListGrid(R.drawable.icon_android));
        listgambar.add(new DataListGrid(R.drawable.icon_android));
        listgambar.add(new DataListGrid(R.drawable.icon_android));
        listgambar.add(new DataListGrid(R.drawable.icon_android));
        listgambar.add(new DataListGrid(R.drawable.icon_android));
        listgambar.add(new DataListGrid(R.drawable.icon_android));
        return listgambar;
    }
}

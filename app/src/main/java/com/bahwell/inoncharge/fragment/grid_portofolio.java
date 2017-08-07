package com.bahwell.inoncharge.fragment;

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
import com.bahwell.inoncharge.other.GridViewAdapterPortofolio;

import java.util.ArrayList;
import java.util.List;

public class grid_portofolio extends Fragment implements View.OnClickListener {
    private List<DataListGrid> listgambar;
    private GridView gridView;
    private GridViewAdapterPortofolio gridViewAdapter;


    public grid_portofolio() {
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

        gridView = (GridView) rootView.findViewById(R.id.mygridview);
        gridView.setNestedScrollingEnabled(true);
        getProductList();
        setAdapters();

        gridView.setOnItemClickListener(onItemClick);
        return rootView;
    }
    private void setAdapters() {
            gridViewAdapter = new GridViewAdapterPortofolio(this.getActivity(), R.layout.grid_portofolio, listgambar);
            gridView.setAdapter(gridViewAdapter);
    }
    @Override
    public void onClick(View view) {

    }
    AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(getActivity(),  " - " , Toast.LENGTH_SHORT).show();
        }
    };

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

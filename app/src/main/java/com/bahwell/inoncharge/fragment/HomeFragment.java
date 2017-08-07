package com.bahwell.inoncharge.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.bahwell.inoncharge.R;
import com.bahwell.inoncharge.activity.AboutActivity;
import com.bahwell.inoncharge.activity.BrowserMerchantActivity;
import com.bahwell.inoncharge.activity.MainActivity;
import com.bahwell.inoncharge.activity.WorkOrientedActivity;
import com.bahwell.inoncharge.other.DataListGrid;
import com.bahwell.inoncharge.other.HomeMenuAdapter;
import com.bahwell.inoncharge.other.SessionKategori;
import com.bahwell.inoncharge.other.SlideHomeAdapter;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private List<DataListGrid> listgambar;


    SessionKategori sessionKategori;

    public HomeFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        getProductList();

        GridView gridView = (GridView) rootView.findViewById(R.id.HomeMenu);
        gridView.setAdapter(new HomeMenuAdapter(this.getActivity(), R.layout.adapter_home, listgambar));
//      this.getActivity(), R.layout.adapter_home, listgambar
        sessionKategori = new SessionKategori(getContext().getApplicationContext());
        gridView.setOnItemClickListener(onItemClick);

        SlideHomeAdapter mAdapter = new SlideHomeAdapter(getContext());
        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        viewPager.setAdapter(mAdapter);

        CircleIndicator indicator = (CircleIndicator) rootView.findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
        mAdapter.registerDataSetObserver(indicator.getDataSetObserver());

        return rootView;
    }

    AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getActivity(), WorkOrientedActivity.class);
            String tmpValue = null;
            switch (position){
                case 0:
                    tmpValue = "InDesign";
                    sessionKategori.setKategoriName("InDesign");
                    Log.d("cek", sessionKategori.getKategoriName());
                    break;
                case 1:
                    tmpValue = "InConsult";
                    sessionKategori.setKategoriName("InConsult");
                    break;
                case 2:
                    tmpValue = "InCourse";
                    sessionKategori.setKategoriName("InCourse");
                    break;
                case 3:
                    tmpValue = "InBiro";
                    sessionKategori.setKategoriName("InBiro");
                    break;
                case 4:
                    tmpValue = "InDrive";
                    sessionKategori.setKategoriName("InDrive");
                    break;
                case 5:
                    tmpValue = "InGuide";
                    sessionKategori.setKategoriName("InGuide");
                    break;
                case 6:
                    tmpValue = "InHealthy";
                    sessionKategori.setKategoriName("InHealthy");
                    break;
                case 7:
                    tmpValue = "InSales";
                    sessionKategori.setKategoriName("InSales");
                    break;
                case 8:
                    tmpValue = "InMotiva";
                    sessionKategori.setKategoriName("InMotiva");
                    break;
                case 9:
                    tmpValue = "InRent";
                    sessionKategori.setKategoriName("InRent");
                    break;
                case 10:
                    tmpValue = "InSave";
                    sessionKategori.setKategoriName("InSave");
                    break;
                case 11:
                    tmpValue = "InTranslate";
                    sessionKategori.setKategoriName("InTranslate");
                    break;
                case 12:
                    tmpValue = "InDoctor";
                    sessionKategori.setKategoriName("InDoctor");
                    break;
                case 13:
                    tmpValue = "InHelp";
                    sessionKategori.setKategoriName("InHelp");
                    break;
                case 14:
                    tmpValue = "InPublic";
                    sessionKategori.setKategoriName("InPublic");
                    break;
                case 15:
                    tmpValue = "InService";
                    sessionKategori.setKategoriName("InService");
                    break;

            }
            intent.putExtra("title_new",tmpValue);
            startActivity(intent);
        }
    };

    public List<DataListGrid> getProductList() {
        listgambar = new ArrayList<>();
        listgambar.add(new DataListGrid(R.drawable.design));
        listgambar.add(new DataListGrid(R.drawable.consult));
        listgambar.add(new DataListGrid(R.drawable.course));
        listgambar.add(new DataListGrid(R.drawable.biro));
        listgambar.add(new DataListGrid(R.drawable.drive));
        listgambar.add(new DataListGrid(R.drawable.guide));
        listgambar.add(new DataListGrid(R.drawable.healthy));
        listgambar.add(new DataListGrid(R.drawable.sales));
        listgambar.add(new DataListGrid(R.drawable.motiva));
        listgambar.add(new DataListGrid(R.drawable.rent));
        listgambar.add(new DataListGrid(R.drawable.save));
        listgambar.add(new DataListGrid(R.drawable.translate));
        listgambar.add(new DataListGrid(R.drawable.doctor));
        listgambar.add(new DataListGrid(R.drawable.help));
        listgambar.add(new DataListGrid(R.drawable.publik));
        listgambar.add(new DataListGrid(R.drawable.services));

        return listgambar;
    }

}

package com.bahwell.inoncharge.other;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bahwell.inoncharge.R;

import java.util.List;

/**
 * Created by NgocTri on 10/22/2016.
 */

public class GridViewAdapterPortofolio extends ArrayAdapter<DataListGrid> {

    public GridViewAdapterPortofolio(Context context, int resource, List<DataListGrid> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (null == v) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.grid_portofolio, null);
        }
        DataListGrid product = getItem(position);
        ImageView img = (ImageView) v.findViewById(R.id.imageView);
        img.setImageResource(product.getImageId());

        return v;
    }
}
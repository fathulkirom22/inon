package com.bahwell.inoncharge.other;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bahwell.inoncharge.R;
import com.bahwell.inoncharge.activity.MainActivity;

import java.util.List;

/**
 * Created by bahwell on 17/05/17.
 */
//ArrayAdapter<DataListGrid>
public class HomeMenuAdapter extends ArrayAdapter<DataListGrid> {

    private Context mContext;

    public  HomeMenuAdapter(Context c, int resource, List<DataListGrid> objects){
        super(c, resource, objects);
        mContext =c;
    }

//    public int getCount() {
//        return mThumbIds.length;
//    }
//
//    public Object getItem(int position) {
//        return null;
//    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (null == v) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.adapter_home, null);
        }


        DataListGrid product = getItem(position);
        ImageView img = (ImageView) v.findViewById(R.id.home_menu);
        img.setImageResource(product.getImageId());

        return v;

//        ImageView imageBtn ;
//       FrameLayout FL;
//
//        if (convertView == null) {
//            imageBtn = new ImageView(mContext);
//            imageBtn.setLayoutParams(new GridView.LayoutParams(180, 180));
//
////            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
////            imageBtn.setLayoutParams(new GridView.LayoutParams(params));
//            imageBtn.setScaleType(ImageView.ScaleType.FIT_XY);
//            imageBtn.setBackgroundColor(Color.parseColor("#fbdcbb"));
//            imageBtn.setBackgroundResource(R.drawable.grid_items_border);
//        }
//        else
//        {
//            imageBtn = (ImageView) convertView;
//        }
//        imageBtn.setImageResource(mThumbIds[position]);
//        return imageBtn;

    }

//    public static int getPixelsFromDPs(Activity activity, int dps){
//        Resources r = activity.getResources();
//        int  px = (int) (TypedValue.applyDimension(
//                TypedValue.COMPLEX_UNIT_DIP, dps, r.getDisplayMetrics()));
//        return px;
//    }


//     Keep all Images in array
//    public Integer[] mThumbIds = {
//            R.drawable.design, R.drawable.consult,
//            R.drawable.course, R.drawable.biro,
//            R.drawable.drive, R.drawable.guide,
//            R.drawable.healthy, R.drawable.sales,
//            R.drawable.motiva, R.drawable.rent,
//            R.drawable.save, R.drawable.translate
//    };
}

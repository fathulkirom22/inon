package com.bahwell.inoncharge.other;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by INDRA on 21/07/2017.
 */

public class SlideHomeAdapter extends PagerAdapter {


    private Context mContext;

    public SlideHomeAdapter(Context context) {
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        ModelObjectSlideHome modelObject = ModelObjectSlideHome.values()[position];
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(modelObject.getLayoutResId(), collection, false);
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return ModelObjectSlideHome.values().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        ModelObjectSlideHome customPagerEnum = ModelObjectSlideHome.values()[position];
        return mContext.getString(customPagerEnum.getTitleResId());
    }
}

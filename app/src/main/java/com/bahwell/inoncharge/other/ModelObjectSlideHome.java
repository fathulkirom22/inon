package com.bahwell.inoncharge.other;

import com.bahwell.inoncharge.R;

/**
 * Created by INDRA on 21/07/2017.
 */

public enum ModelObjectSlideHome {
    RED(R.string.red, R.layout.slide_home_red),
    BLUE(R.string.blue, R.layout.slide_home_blue),
    GREEN(R.string.green, R.layout.slide_home_green);

    private int mTitleResId;
    private int mLayoutResId;

    ModelObjectSlideHome(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }

}

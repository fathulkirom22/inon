package com.bahwell.inoncharge.other;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by bahwell on 12/07/17.
 */

public class SessionKategori {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "KategoriPref";
    private static final String KATEGORI_NAME = "KATEGORI_NAME";

    // Constructor
    public SessionKategori(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setKategoriName(String v){
        editor.putString(KATEGORI_NAME, v);
        editor.commit();
    }

    public String getKategoriName(){
        return pref.getString(KATEGORI_NAME, null);
    }
}

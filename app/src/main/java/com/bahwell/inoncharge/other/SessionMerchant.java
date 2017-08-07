package com.bahwell.inoncharge.other;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by INDRA on 28/07/2017.
 */

public class SessionMerchant {

    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "MerchantPref";

    // All Shared Preferences Keys
    private static final String MERCHANT_NAME = "MERCHANT_NAME";

    public static final String KEY_NAME = "name";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    // phone address (make variable public to access from outside)
    public static final String KEY_PHONE = "phone";

    // address address (make variable public to access from outside)
    public static final String KEY_ADDRESS = "address";

    public SessionMerchant(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setName(String v){
        editor.putString(KEY_NAME, v);
        // commit changes
        editor.commit();
    }
    public void setAddress(String v){
        editor.putString(KEY_ADDRESS, v);
        // commit changes
        editor.commit();
    }
    public void setEmail(String v){
        editor.putString(KEY_EMAIL, v);
        // commit changes
        editor.commit();
    }
    public void setPhone(String v){
        editor.putString(KEY_PHONE, v);
        // commit changes
        editor.commit();
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        // user email id
        user.put(KEY_ADDRESS, pref.getString(KEY_ADDRESS, null));

        // user email id
        user.put(KEY_PHONE, pref.getString(KEY_PHONE, null));

        // return user
        return user;
    }

    public boolean MerchantName(){
        return pref.getBoolean(MERCHANT_NAME, false);
    }




}

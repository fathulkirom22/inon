package com.bahwell.inoncharge.other;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by bahwell on 23/05/17.
 */
@IgnoreExtraProperties
public class Merchant {

    public String NO_KTP;
    public String NO_KTP_RELATIVE;
    public String NO_BANK_ACCOUNT;
    public String NO_PHONE_RELATIVE;

    public Merchant(){}

    public Merchant(String NO_KTP,String NO_KTP_RELATIVE,String NO_BANK_ACCOUNT,String NO_PHONE_RELATIVE) {
        this.NO_KTP = NO_KTP;
        this.NO_KTP_RELATIVE = NO_KTP_RELATIVE;
        this.NO_BANK_ACCOUNT= NO_BANK_ACCOUNT;
        this.NO_PHONE_RELATIVE = NO_PHONE_RELATIVE;
    }


}

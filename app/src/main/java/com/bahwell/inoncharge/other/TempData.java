package com.bahwell.inoncharge.other;

import com.orm.SugarRecord;

/**
 * Created by bahwell on 03/06/17.
 */

public class TempData extends SugarRecord<TempData> {
    public String IDUser;
    public String Name;
    public String Phone;
    public String Address;
    public String Email;
    public String Status;
//    public String NO_KTP;
//    public String NO_KTP_RELATIVE;
//    public String NO_BANK_ACCOUNT;
//    public String NO_PHONE_RELATIVE;

    public TempData(){
    }

    public TempData(String IDUser, String Name , String Phone , String Address , String Status , String Email ){
        this.IDUser   = IDUser;
        this.Name     = Name;
        this.Phone    = Phone;
        this.Address  = Address;
        this.Status   = Status;
        this.Email    = Email;
    }
}

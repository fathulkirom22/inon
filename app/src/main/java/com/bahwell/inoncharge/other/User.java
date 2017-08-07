package com.bahwell.inoncharge.other;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bahwell on 23/05/17.
 */
@IgnoreExtraProperties
public class User {

    public String Name;
    public String Phone;
    public String Address;
    public String Status;
    public String Email;
    public String pushToken;

    public  User(){

    }


    public User(String Name, String Email, String Phone, String Address, String Status) {
        this.Name = Name;
        this.Email = Email;
        this.Phone = Phone;
        this.Address = Address;
        this.Status = Status;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("Name", Name);
        result.put("Email", Email);
        result.put("Phone", Phone);
        result.put("Address", Address);
        result.put("Status", Status);
        result.put("pushToken", pushToken);

        return result;
    }


}

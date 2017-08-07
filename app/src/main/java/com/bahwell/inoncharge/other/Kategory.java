package com.bahwell.inoncharge.other;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bahwell on 10/07/17.
 */

public class Kategory {

    public String pricehour;

    public  Kategory(){

    }

    public Kategory(String pricehour) {
        this.pricehour = pricehour;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("pricehour", pricehour);
        return result;
    }
}

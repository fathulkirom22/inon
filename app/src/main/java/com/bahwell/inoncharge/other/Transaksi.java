package com.bahwell.inoncharge.other;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bahwell on 02/08/17.
 */

@IgnoreExtraProperties
public class Transaksi {

    public String lokasi_log;
    public String lokasi_lat;
    public String harga_total;
    public String jenis_jasa;
    public String now_or_boking;
    public String date_awal;
    public String date_ahir;
    public String time_awal;
    public String time_ahir;
    public String status;

    public Transaksi(){

    }

    public Transaksi(
            String lokasi_log,
            String lokasi_lat,
            String harga_total,
            String jenis_jasa,
            String now_or_boking,
            String date_awal,
            String date_ahir,
            String time_awal,
            String time_ahir,
            String status
            ){
                this.lokasi_log = lokasi_log;
                this.lokasi_lat = lokasi_lat;
                this.harga_total = harga_total;
                this.jenis_jasa = jenis_jasa;
                this.now_or_boking = now_or_boking;
                this.date_awal = date_awal;
                this.date_ahir = date_ahir;
                this.time_awal = time_awal;
                this.time_ahir = time_ahir;
                this.status = status;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("lokasi_log", lokasi_log);
        result.put("lokasi_lat", lokasi_lat);
        result.put("harga_total", harga_total);
        result.put("jenis_jasa", jenis_jasa);
        result.put("now_or_boking", now_or_boking);
        result.put("date_awal", date_awal);
        result.put("date_ahir", date_ahir);
        result.put("time_awal", time_awal);
        result.put("time_ahir", time_ahir);
        result.put("status", status);
        return result;
    }
}

package com.tack;

import com.google.gson.Gson;
import org.json.JSONObject;

import java.util.ArrayList;

public class Jasonparser {
    public String getAllRecords(ArrayList<Drone> drones,ArrayList<Medication> medications,String requestUrl){
        JSONObject parent_js = new JSONObject();
        JSONObject drone_js = new JSONObject();
        JSONObject medics_js = new JSONObject();

        for (Drone drone:drones){
            drone_js.put(String.valueOf(drone.getSerial_number()),String.valueOf (new Gson().toJson(drone,Drone.class)));
        }
        for (Medication medication:medications){
            medication.setImage(requestUrl+"images?item="+medication.getImage()+".jpg");
            medics_js.put(medication.getCode(),String.valueOf(new Gson().toJson(medication,Medication.class)));
        }
        parent_js.put("drones",drone_js);
        parent_js.put("medications",medics_js);
        return String.valueOf(parent_js);
    }
}

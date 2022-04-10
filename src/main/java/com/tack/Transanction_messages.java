package com.tack;

import org.json.JSONObject;

public class Transanction_messages {
    //TODO add method to log every request and tidy up response
    private static JSONObject getMessage(String message,String status){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message",message);
        jsonObject.put("status",status);
        return jsonObject;
    }

    public static String bad_syntax(String request_data,String url){
        JSONObject response = getMessage("Bad request","failed");
        Util.logAll(request_data,url,response.getString("message"),"error");
        return response.toString();
    }

    public static String key_not_found(String request_data,String url){
        JSONObject response = getMessage("Expected key not found","failed");
        Util.logAll(request_data,url,response.getString("message"),"error");
        return response.toString();
    }

    public static String invalid_field_data(String request_data,String url){
        JSONObject response = getMessage("Data field compliance error","failed");
        Util.logAll(request_data,url,response.getString("message"),"error");
        return response.toString();
    }

    public static String invalid_load_request(String request_data,String url){
        JSONObject response = getMessage("This loading request is invalid","failed");
        Util.logAll(request_data,url,response.getString("message"),"error");
        return response.toString();
    }

    public static String duplicate_serial_number(String request_data,String url){
        JSONObject response = getMessage("Duplicate serial number","failed");
        Util.logAll(request_data,url,response.getString("message"),"error");
        return response.toString();
    }

    public static String success(String request_data,String url){
        JSONObject response = getMessage("Entry successful","success");
        Util.logAll(request_data,url,response.getString("message"),"fine");
        return response.toString();
    }

    public static String loading(String request_data,String url){
        JSONObject response = getMessage("Status changed to loading","success");
        Util.logAll(request_data,url,response.getString("message"),"fine");
        return response.toString();
     }

    public static String medication_is_over_drone_capacity(String request_data,String url){
        JSONObject response = getMessage("Drone failed the required  capacity","failed");
        Util.logAll(request_data,url,response.getString("message"),"info");
        return response.toString();
    }

    public static String battery_level_is_low(String request_data,String url){
        JSONObject response = getMessage("Drone is not cleared to dispatch due to low battery level","failed");
        Util.logAll(request_data,url,response.getString("message"),"info");
        return response.toString();
    }

    public static String drone_is_not_available(String request_data,String url){
        JSONObject response = getMessage("Selected drone cant be engaged","failed");
        Util.logAll(request_data,url,response.getString("message"),"info");
        return response.toString();
    }






}

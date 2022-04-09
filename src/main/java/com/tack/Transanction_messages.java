package com.tack;

public class Transanction_messages {
    public static String bad_syntax  = "{\"status\":\"failed\",\"message\":\"bad request\"}";
    public static String key_not_found  = "{\"status\":\"failed\",\"message\":\"expected key not found\"}";
    public static String invalid_field_data  = "{\"status\":\"failed\",\"message\":\"Data field compliance error\"}";
    public static String invalid_load_request  = "{\"status\":\"failed\",\"message\":\"This loading request is invalid\"}";

    public static String duplicate_serial_number  = "{\"status\":\"failed\",\"message\":\"Duplicate serial number\"}";
    public static String success  = "{\"status\":\"success\",\"message\":\"Entry successful\"}";
    public static String loading = "{\"status\":\"success\",\"message\":\"Status changed to loading\"}";
    public static String medication_is_over_drone_capacity = "{\"status\":\"failed\",\"message\":\"Drone failed the required  capacity\"}";
    public static String battery_level_is_low =  "{\"status\":\"failed\",\"message\":\"Drone is not cleared to dispatch due to low battery level\"}";






}

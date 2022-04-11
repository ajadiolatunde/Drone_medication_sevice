package com.tack;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;

public class Singleton {
    JSONObject drone_js;
    JSONObject medication_js;
    Drone selected_drone;


    private static Singleton single_instance = null;
    public static Singleton getInstance()
    {
        if (single_instance == null)
            single_instance = new Singleton();
        return single_instance;
    }


}

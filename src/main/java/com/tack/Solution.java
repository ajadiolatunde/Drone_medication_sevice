package com.tack;

import com.google.gson.Gson;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Solution extends RouterNanoHTTPD {
    public Solution() throws IOException {
        super(8181);
        addMappings();
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nRunning! Point your browsers to http://localhost:8181/ \n");
    }

    @Override
    public void addMappings() {
        addRoute("/register",Traffichandler.RegistrationHandler.class);
        addRoute("/selectdronemedication",Traffichandler.AvailableDroneHandler.class);
        addRoute("/loaditems",Traffichandler.ItemHandler.class);
        addRoute("/batterylevel",Traffichandler.BatteryLevelHandler.class);
        addRoute("/image/*",Traffichandler.FileHandler.class);

        // todo
    }

    public static void main(String[] args) throws SQLException,ClassNotFoundException {
        Singleton singleton = Singleton.getInstance();
        Util.preloadDb();
        int g =(int) SqlQuery.getItem("40734567","Drone",false);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                new Solution();
                }catch (IOException e){

                }
            }
        });
        thread.start();

    }

}

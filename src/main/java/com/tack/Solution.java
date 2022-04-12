package com.tack;

import com.google.gson.Gson;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Solution extends RouterNanoHTTPD {
    private static final ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

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
        addRoute("/loadeditem",Traffichandler.ItemLoadedHandler.class);
        addRoute("/images",Traffichandler.FileHandler.class);
    }

    public static void main(String[] args) throws Exception {

        if (args.length>0){
            //TODO
        }
        Singleton.getInstance();
        Util.preloadDb();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                new Solution();
                }catch (IOException e){
                  e.printStackTrace();
                }
            }
        });
        thread.start();
        Runnable batterylevelchecker = new Runnable() {
            @Override
            public void run() {
                Util.getBatteryLevel();
            }
        };
        service.scheduleAtFixedRate(batterylevelchecker, 10, 10, TimeUnit.SECONDS);

    }

}

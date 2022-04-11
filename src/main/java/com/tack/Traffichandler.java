package com.tack;

import com.google.gson.Gson;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static fi.iki.elonen.NanoHTTPD.MIME_PLAINTEXT;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class Traffichandler  {
    private static final String MIME_JSON = "application/json";
    public static class FileHandler extends RouterNanoHTTPD.GeneralHandler {
        @Override
        public NanoHTTPD.Response get(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
            System.out.println(session.getUri());
            FileInputStream fis = null;
            File file = new File(Util.getUserResourceDir(), "images/ola.jpg");
            System.out.println(file.toString());
            try {
                if (file.exists()){
                    fis = new FileInputStream(file);
                    System.out.println("Yes file exist");
                    return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "image/jpeg", fis, file.length());
                } else {
                    return newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, MIME_PLAINTEXT, "File not found");
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, MIME_PLAINTEXT, "File not found");
            }
        }
    }


    public static class AvailableDroneHandler extends RouterNanoHTTPD.GeneralHandler {

        @Override
        public NanoHTTPD.Response get(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {

            Util.logAll(session.getMethod().name(),session.getUri(),NanoHTTPD.Response.Status.BAD_REQUEST.toString(),"error");
            try {
                return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST,MIME_JSON,SqlQuery.view_drones_medication("http://"+session.getHeaders().get("host")+"/"));
            } catch (SQLException e) {
                e.printStackTrace();
                return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST,MIME_PLAINTEXT,"Bad request");

            }
        }


    }

    public static class ItemHandler extends RouterNanoHTTPD.GeneralHandler {

        //Load medication into drone
        //Expecting json format {drone:serial_number,medication:code}
        //Prevent drone overweight

        @Override
        public NanoHTTPD.Response get(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
            Util.logAll(session.getMethod().name(),session.getUri(),NanoHTTPD.Response.Status.BAD_REQUEST.toString(),"error");
            return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST,MIME_PLAINTEXT," ");
        }

        @Override
        public NanoHTTPD.Response post(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
            //Expecting json formatted data in the parameter key "json"
            Integer contentLength = Integer.parseInt(session.getHeaders().get("content-length"));
            String data = Util.getFormdata(contentLength,session.getInputStream());

            if (data != null) {

                if (!Util.isDataJson(data, false)) {
                    return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, MIME_JSON, Transanction_messages.bad_syntax(data,session.getUri()));
                    //Presumed only an item can be loaded ,therefore expect a drone serialid with matching item weight
                } else {

                    try {
                        if (!Util.isDroneMedicationValid(data)) {

                            return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, MIME_JSON, Transanction_messages.invalid_load_request(data,session.getUri()));

                        } else {
                            //Data sent are valid can be extracted from singleton variable
                            int battery_level = Singleton.getInstance().drone_js.getInt("capacity");
                            String status = Singleton.getInstance().drone_js.getString("state");
                            if (!status.equals("IDLE")) {
                                return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, MIME_JSON, Transanction_messages.drone_is_not_available(data,session.getUri()));

                            } else if (battery_level < Util.MIN_BATTERY_LEVEL) {
                                //Prevent the dispatch request due to low battery level

                                return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, MIME_JSON, Transanction_messages.battery_level_is_low(data,session.getUri()));

                            } else if (Util.isOverWeight()) {
                                return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, MIME_JSON, Transanction_messages.medication_is_over_drone_capacity(data,session.getUri()));

                            } else {
                                //Todo :add record to dispatch table and update both drone and medication
                                Item item = new Item();
                                item.setDrone(Singleton.getInstance().drone_js.getString("serial_number"));
                                item.setMedication(Singleton.getInstance().medication_js.getString("code"));
                                SqlQuery.insert_data(item);
                                return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, MIME_JSON, Transanction_messages.loading(data,session.getUri()));
                            }

                        }
                    } catch (SQLException e) {

                        e.printStackTrace();
                        return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, MIME_JSON, Transanction_messages.key_not_found(data,session.getUri()));

                    }
                }

            } else {
                return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, MIME_JSON, Transanction_messages.key_not_found(data,session.getUri()));

            }

        }

    }

    public static class RegistrationHandler extends RouterNanoHTTPD.GeneralHandler {
        @Override
        public NanoHTTPD.Response get(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
            Util.logAll(session.getParms().entrySet().toString(),session.getUri(),NanoHTTPD.Response.Status.BAD_REQUEST.toString(),"error");
            return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, MIME_PLAINTEXT, " ");
        }

        @Override
        public NanoHTTPD.Response post(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
            //Expecting json formatted data in the parameter key "json"
            Integer contentLength = Integer.parseInt(session.getHeaders().get("content-length"));
            String data = Util.getFormdata(contentLength,session.getInputStream());
            if (data != null) {
                if (!Util.isDataJson(data, true)) {
                    return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, MIME_JSON, Transanction_messages.bad_syntax(data,session.getUri()));

                } else if (!Util.isRegDataValid(data)) {
                    return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, MIME_JSON, Transanction_messages.invalid_field_data(data,session.getUri()));

                } else {
                    try {
                        if (Util.isDuplicateEntry(data)) {

                            return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, MIME_JSON, Transanction_messages.duplicate_serial_number(data,session.getUri()));

                        } else {
                            Drone drone = new Gson().fromJson(data, Drone.class);
                            try {
                                SqlQuery.insert_data(drone);
                                return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, MIME_JSON, Transanction_messages.success(data,session.getUri()));

                            } catch (Exception e) {
                                //Todo
                                e.printStackTrace();
                                return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, MIME_PLAINTEXT, e.getMessage());

                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        //Todo
                        return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, MIME_PLAINTEXT, e.getMessage());

                    }
                }

            } else {
                return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, MIME_JSON, Transanction_messages.key_not_found(data,session.getUri()));

            }


        }

    }
}

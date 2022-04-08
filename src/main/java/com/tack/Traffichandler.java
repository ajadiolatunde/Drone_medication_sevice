package com.tack;

import com.google.gson.Gson;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
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
            File file = new File(Util.getUserDir(), "images/ola.jpg");
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


    public static class AvailableDroneHandler extends RouterNanoHTTPD.DefaultHandler {
        @Override
        public String getText() {
            try {
                return SqlQuery.view_drones_medication();
            } catch (SQLException e) {
                e.printStackTrace();
                return e.getMessage();
            }
        }

        @Override
        public String getMimeType() {
            return MIME_JSON;
        }

        @Override
        public NanoHTTPD.Response.IStatus getStatus() {
            return NanoHTTPD.Response.Status.OK;
        }
    }

    public static class ItemHandler extends RouterNanoHTTPD.GeneralHandler {
        /**
         * First confirm proper json data  , and compare the selected drone capacity with  medication weight.
         *
         * @param uriResource
         * @param urlParams
         * @param session
         * @method isDroneMedicationValid
         * @return
         */
        //Load medication into drone
        //Expecting json format {drone:serial_number,medication:code}
        //Prefect drone overweight

        @Override
        public NanoHTTPD.Response get(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
            return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST,MIME_PLAINTEXT," ");
        }

        @Override
        public NanoHTTPD.Response post(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
            //Expecting json formatted data in the parameter key "json"
            if (session.getParameters().containsKey("json")){
                String data = session.getParameters().get("json").get(0);
                if (!Util.isDataJson(data,false)){
                    return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST,MIME_JSON,Transanction_messages.bad_syntax);
                //Presumed only an item can be loaded ,therefore expect a drone serialid with matching item weight
                }else {
                    try {
                        if (!Util.isDroneMedicationValid(data)){
                            return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST,MIME_JSON,Transanction_messages.invalid_load_request);

                        }else {
                            return newFixedLengthResponse(NanoHTTPD.Response.Status.OK,MIME_JSON,Transanction_messages.loading);

                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            }else{
                return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST,MIME_JSON,Transanction_messages.key_not_found);

            }
            return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST,MIME_JSON,Transanction_messages.key_not_found);

        }

    }

    public static class RegistrationHandler extends RouterNanoHTTPD.GeneralHandler {
        @Override
        public NanoHTTPD.Response get(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
            return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST,MIME_PLAINTEXT," ");
        }

        @Override
        public NanoHTTPD.Response post(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
            //Expecting json formatted data in the parameter key "json"
            if (session.getParameters().containsKey("json")){
                String data = session.getParameters().get("json").get(0);
                if (!Util.isDataJson(data,true)){
                    return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST,MIME_JSON,Transanction_messages.bad_syntax);

                }else if (!Util.isRegDataValid(data)){
                    return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST,MIME_JSON,Transanction_messages.invalid_field_data);

                }else {
                    try {
                        if (Util.isDuplicateEntry(data)){

                            return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST,MIME_JSON,Transanction_messages.duplicate_serial_number);

                        }else{
                            Drone drone = new Gson().fromJson(data,Drone.class);
                            try {
                                SqlQuery.insert_data(drone);
                                return newFixedLengthResponse(NanoHTTPD.Response.Status.OK,MIME_JSON,Transanction_messages.success);

                            } catch (SQLException e) {
                                e.printStackTrace();
                                return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST,MIME_PLAINTEXT,e.getMessage());

                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                                return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST,MIME_PLAINTEXT,e.getMessage());

                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            }else{
                return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST,MIME_JSON,Transanction_messages.key_not_found);

            }

            return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST,MIME_JSON,Transanction_messages.key_not_found);
        }

    }
}

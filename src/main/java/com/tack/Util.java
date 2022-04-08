package com.tack;

import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Util {


    static String[] STATE={"IDLE", "LOADING", "LOADED", "DELIVERING", "DELIVERED", "RETURNING"};
    static String[] MODELS ={"Lightweight", "Middleweight", "Cruiserweight", "Heavyweight"};
    public static String getUserDir(){
        final String dir = System.getProperty("user.dir");
        return  dir+"/src/main/resources";
    }

    public static void preloadDb() throws SQLException,ClassNotFoundException {
        Create_db create_db = new Create_db();
        Drone drone = new Drone();
        drone.setModel("RED");
        drone.setBattery_capacity(90);
        drone.setSerial_number("40734567");
        drone.setWeight(34);
        drone.setState("AVAILABLE");
        Medication medication = new Medication();
        medication.setCode("fe3456");
        medication.setWeight(39);
        medication.setName("Panadol");
        medication.setImage("ddfdrre343");
       // SqlQuery.insert_data(drone);
        //SqlQuery.insert_data(medication);
    }

    /**
     * This function handle task for drone registration endpoint when isregistration _task parameter is true
     * and for loading medication endpoint  when false
     * @param data
     * @param isregistration_task
     * **/
    //Ensure recieved data is proper json object


    public static boolean isDataJson(String data, boolean isregistration_task){
        System.out.println("------"+data);

        try{
            JSONObject jsonObject = new JSONObject(data);
            System.out.println(jsonObject);
            //Registring a new drone
            if (isregistration_task){
                System.out.println("Checking fields");
                System.out.println(jsonObject.keySet());

                // Check all drone field members
                ArrayList<String> jsfields = new ArrayList<>();
                for (String key :jsonObject.keySet()){
                    jsfields.add(key);
                }
                for (Field droneField:Drone.class.getDeclaredFields()){
                    if (!jsfields.contains(droneField.getName())){
                        System.out.println("\n "+droneField.getName()+" not found in json keys");
                        return false;
                    }
                }

            }else{
                //todo lookup drone and medication id in the db
                if (!jsonObject.has("drone") && !jsonObject.has("medication")){
                    return false;
                }

            }

            return true;
        }catch (JSONException e){
            System.out.println(e.getMessage());

            return false;
        }
    }

    //check drone data field compliance
    public static boolean isRegDataValid(String data){
        List<String> models = Arrays.asList(MODELS);
        List<String> states = Arrays.asList(STATE);
        try{
            JSONObject jsonObject = new JSONObject(data);
            Drone drone = new Gson().fromJson(String.valueOf(jsonObject),Drone.class);
            if (drone.getWeight()>500){
                System.out.println("Problem weight");

                return  false;
            }else if(drone.getSerial_number().length()>100){
                System.out.println("Problem serial");

                return false;
            }else if (drone.getBattery_capacity()>100 || drone.getBattery_capacity()<0){
                System.out.println("Problem capacity");

                return false;
            }else if (!models.contains( drone.getModel())){

                return false;
            }else return states.contains(drone.getState());
        }catch (JSONException e){
            return false;
        }

    }
    //This prevent duplicate entry and hide sql constraint error message
    public static boolean isDuplicateEntry(String data) throws SQLException {
        //TODO
        Drone drone = new Gson().fromJson(data,Drone.class);
        System.out.println("Getting serial "+drone.getSerial_number());
        int rowcount = (int)SqlQuery.getItem(drone.getSerial_number(),"Drone",false) ;
        if (rowcount>0){
            return true;
        }
        return false;
    }

    //After ensuring data sent is valid, drone capacity is compared  to medication weight before dispatch
    public static boolean isDroneMedicationValid(String data) throws SQLException {
        Singleton singleton = Singleton.getInstance();
        Item item = new Gson().fromJson(data,Item.class);
        singleton.drone_js = (JSONObject) SqlQuery.getItem(item.getDrone_serialnumber(),"Drone",true) ;
        singleton.medication_js = (JSONObject) SqlQuery.getItem(item.getDrone_serialnumber(),"Medication",true) ;

        //Validity check here
        if (singleton.drone_js.names().length()>0 && singleton.medication_js.names().length()>0){
            return true;
        }

        return false;
    }

    public static boolean isOverWeight(JSONObject drone_js, JSONObject medication_js){
        //- model (Lightweight, Middleweight, Cruiserweight, Heavyweight);
       // - weight limit (500gr max);

        return true;
    }

    public  static int get_battery_level(Drone drone){
        return 1;
    }


}

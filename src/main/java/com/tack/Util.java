package com.tack;

import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.ls.LSInput;

import java.io.*;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Util {
    static String[] STATE={"IDLE", "LOADING", "LOADED", "DELIVERING", "DELIVERED", "RETURNING"};
    public static String[] MODELS ={"Lightweight", "Middleweight", "Cruiserweight", "Heavyweight"};
    public static String getUserDir(){
        final String dir = System.getProperty("user.dir");
        return  dir+"/src/main/resources";
    }

    public static ArrayList<Object> loadDataFromCsvFile(){
        File file = new File(getUserDir(),"data.csv");
        ArrayList<Object> arrayList = new ArrayList<>();

        try {
            BufferedReader csvreader = new BufferedReader(new FileReader(file));
            String row;
            while ((row = csvreader.readLine()) != null){
                String[] data = row.split(",");
                switch (data.length){
                    case 4://Medication
                        Medication medication = new Medication(data[0],data[2],Integer.valueOf(data[1]),data[3]);
                        arrayList.add(medication);
                        break;
                    case 5://Drone
                        Drone drone = new Drone(data[1],data[2],Integer.valueOf(data[0]),data[4],Integer.valueOf(data[3]));
                         arrayList.add(drone);
                        break;

                }

            }
        }catch (FileNotFoundException e){
                e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();

        }catch (Exception e){
            e.printStackTrace();
        }
        return arrayList;
    }

    public static void preloadDb() throws Exception {
        new Create_db();
        ArrayList<Object> objectArrayList = loadDataFromCsvFile();
        System.out.println("-------------------1------------------------");

        for (Object obj:objectArrayList){
               SqlQuery.insert_data(obj);
        }

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
                // lookup drone and medication id in the sent js data
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

    //Ensure request to obtain a drone records is json formatted
    public static boolean isDataJson(String data){
        //Todo
        return true;
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
        System.out.println("----Util line 164-----"+item.getDrone()+" "+item.getMedication());
        System.out.println("----here--165---"+data);


        singleton.drone_js = (JSONObject) SqlQuery.getItem(item.getDrone(),"Drone",true) ;
        singleton.medication_js = (JSONObject) SqlQuery.getItem(item.getMedication(),"Medication",true) ;
        System.out.println("----here---170--"+singleton.drone_js);
        System.out.println("----here---171--"+singleton.medication_js);


        //Validity check here
        if (singleton.drone_js.names().length()>0 && singleton.medication_js.names().length()>0){
            return true;
        }

        return false;
    }
    //Subject the request to weight test
    // models (Lightweight, Middleweight, Cruiserweight, Heavyweight);
    public static boolean isOverWeight(){
        Drone drone = new Gson().fromJson(String.valueOf(Singleton.getInstance().drone_js),Drone.class);
        Medication medication = new Gson().fromJson(String.valueOf(Singleton.getInstance().medication_js),Medication.class);
        switch (drone.getModel()){
            case "Lightweight"://Medication weight 100gr
                if (medication.getWeight()<=100) return false;
                break;
            case  "Middleweight"://Medication weight 200gr
                if (medication.getWeight()<=200) return false;
                break;
            case  "Cruiserweight"://Medication weight 300gr
                if (medication.getWeight()<=300) return false;
                break;
            case "Heavyweight"://Medication weight 500gr
                if (medication.getWeight()<=500) return false;
                break;
        }

        return true;
    }

    public  static int get_battery_level(Drone drone){
        return 1;
    }


}

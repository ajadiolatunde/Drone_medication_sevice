package com.tack;

import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

class UtilTest {

    @Test
    void testGetUserResourceDir() {
        String result = Util.getUserResourceDir();
        Assertions.assertEquals("/home/olatunde/IdeaProjects/thack4/src/main/resources", result);
    }

    @Test
    void testLoadDataFromCsvFile() {
        ArrayList<Object> result = Util.loadDataFromCsvFile();
        Assertions.assertEquals(new ArrayList<Object>(Arrays.asList(" ")), result);
    }

    @Test
    void testPreloadDb() {
        Util.preloadDb();
    }

    @Test
    void testIsDataJson() {
        String data = "{\"drone\":\"234FGT67\",\"medication\":\"34ERTY67\"}";
        String data_reg  = "{\"serial_number\":\"40734567\",\"model\":\"Lightweight\",\"weight\":34,\"battery_capacity\":90,\"state\":\"IDLE\"}";
        JSONObject jsonObject = new JSONObject(data);
        JSONObject jsonObject_reg = new JSONObject(data_reg);
        boolean result = Util.isDataJson(jsonObject.toString(), false);
        boolean result_reg = Util.isDataJson(jsonObject.toString(), true);
        boolean result_reg_true = Util.isDataJson(jsonObject_reg.toString(), true);
        Assertions.assertEquals(true, result);
        Assertions.assertEquals(false, result_reg);
        Assertions.assertEquals(true, result_reg_true);


    }

    @Test
    void testIsRegDataValid() {
        String data_reg  = "{\"serial_number\":\"40734567\",\"model\":\"Lightweight\",\"weight\":34,\"battery_capacity\":90,\"state\":\"IDLE\"}";
        String data_model  = "{\"serial_number\":\"40734567\",\"model\":\"Lightweiht\",\"weight\":34,\"battery_capacity\":90,\"state\":\"IDLE\"}";
        String data_weight  = "{\"serial_number\":\"40734567\",\"model\":\"Lightweight\",\"weight\":834,\"battery_capacity\":90,\"state\":\"IDLE\"}";
        String data_capacity  = "{\"serial_number\":\"40734567\",\"model\":\"Lightweight\",\"weight\":34,\"battery_capacity\":190,\"state\":\"IDLE\"}";
        String data_state  = "{\"serial_number\":\"40734567\",\"model\":\"Lightweight\",\"weight\":34,\"battery_capacity\":90,\"state\":\"IDL\"}";

        boolean result = Util.isRegDataValid(data_reg);
        Assertions.assertEquals(true, result);
        result = Util.isRegDataValid(data_model);
        Assertions.assertEquals(false, result);
        result = Util.isRegDataValid(data_weight);
        Assertions.assertEquals(false, result);
        result = Util.isRegDataValid(data_capacity);
        Assertions.assertEquals(false, result);
        result = Util.isRegDataValid(data_state);
        Assertions.assertEquals(false, result);

    }

    @Test
    void testIsDuplicateEntry() throws SQLException {
        String data_reg  = "{\"serial_number\":\"40734567\",\"model\":\"Lightweight\",\"weight\":34,\"battery_capacity\":90,\"state\":\"IDLE\"}";
        JSONObject jsonObject = new JSONObject(data_reg);
        boolean result = Util.isDuplicateEntry(jsonObject.toString());
        Assertions.assertEquals(true, result);
    }

    @Test
    void testIsDroneMedicationValid() throws SQLException {
        String data = "{\"drone\":\"23DSD5GH67\", \"medication\":\"29EOPLIKKM\"}";
        JSONObject jsonObject = new JSONObject(data);
        boolean result = Util.isDroneMedicationValid(jsonObject.toString());
        Assertions.assertEquals(true, result);
    }

    @Test
    void testIsOverWeight() {
        boolean result = Util.isOverWeight();
        Assertions.assertEquals(true, result);
    }

    @Test
    void testLogAll() {
        Util.logAll("log", "url", "response", "type");
    }

    @Test
    void testGetFormdata() {
        String result = Util.getFormdata(0, null);
        Assertions.assertEquals(" ", result);
    }

    @Test
    void testGetBatteryLevel() {
        int result = Util.getBatteryLevel();
        Assertions.assertEquals(0, result);
    }
}


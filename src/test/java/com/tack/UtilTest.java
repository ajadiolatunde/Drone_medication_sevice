package com.tack;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

class UtilTest {

    @Test
    void testGetUserResourceDir() {
        String result = Util.getUserResourceDir();
        Assertions.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    void testLoadDataFromCsvFile() {
        ArrayList<Object> result = Util.loadDataFromCsvFile();
        Assertions.assertEquals(new ArrayList<Object>(Arrays.asList("replaceMeWithExpectedResult")), result);
    }

    @Test
    void testPreloadDb() throws Exception {
        Util.preloadDb();
    }

    @Test
    void testIsDataJson() {
        boolean result = Util.isDataJson("data", true);
        Assertions.assertEquals(true, result);
    }

    @Test
    void testIsRegDataValid() {
        boolean result = Util.isRegDataValid("data");
        Assertions.assertEquals(true, result);
    }

    @Test
    void testIsDuplicateEntry() throws SQLException {
        boolean result = Util.isDuplicateEntry("data");
        Assertions.assertEquals(true, result);
    }

    @Test
    void testIsDroneMedicationValid() throws SQLException {
        boolean result = Util.isDroneMedicationValid("data");
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
}


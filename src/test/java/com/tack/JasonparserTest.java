package com.tack;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

class JasonparserTest {
    Jasonparser jasonparser = new Jasonparser();

    @Test
    void testGetAllRecords() {
        String result = jasonparser.getAllRecords(new ArrayList<Drone>(Arrays.asList(new Drone("serial_number", null, 0, null, 0))), new ArrayList<Medication>(Arrays.asList(new Medication(null, "code", 0, "image"))), "requestUrl");
        Assertions.assertEquals(" ", result);
    }
}

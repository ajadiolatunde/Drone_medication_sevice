package com.tack;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

class SqlQueryTest {

    @Test
    void testDb_connect() {
        Connection result = SqlQuery.db_connect();
        Assertions.assertEquals(null, result);
    }

    @Test
    void testView_drones_medication() throws SQLException {
        String result = SqlQuery.view_drones_medication("requestUrl");
        Assertions.assertEquals(" ", result);
    }

    @Test
    void testInsert_data() {
        SqlQuery.insert_data("obj");
    }

    @Test
    void testGetItem() throws SQLException {
        Object result = SqlQuery.getItem("id", "table", true);
        Assertions.assertEquals(" ", result);
    }

    @Test
    void testGetLoadedItem() {
        String result = SqlQuery.getLoadedItem();
        Assertions.assertEquals(" ", result);
    }

    @Test
    void testView_battery() {
        Object result = SqlQuery.view_battery();
        Assertions.assertEquals(" ", result);
    }
}


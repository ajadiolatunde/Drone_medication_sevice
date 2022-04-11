package com.tack;

import java.sql.*;

public class Create_db {
    public Create_db() throws SQLException, ClassNotFoundException {
        Statement stmt = null;
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection(SqlQuery.url);
        conn.setAutoCommit(false);
        System.out.println("Opened database successfully");
        stmt = conn.createStatement();

        stmt.addBatch(SqlQuery.sql_drone);
        System.out.println("A new TABLE DRONE has been created.");

        stmt.addBatch(SqlQuery.sql_medication);
        System.out.println(SqlQuery.sql_medication);
        System.out.println("A new TABLE MEDICATION has been created.");

        stmt.addBatch(SqlQuery.sql_dispatch);
        System.out.println("A new TABLE DISPATCH has been created.");
        stmt.executeBatch();
        conn.commit();


        stmt.close();
        if (conn != null) {
            DatabaseMetaData meta = conn.getMetaData();
            System.out.println("The driver name is " + meta.getDriverName());
            System.out.println("A new database has been created.");
        }
        conn.close();

    }
}

package com.tack;

import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;

public class SqlQuery {
    public static String url = "jdbc:sqlite:"+Util.getUserDir()+ "/ola.db";


    public static String sql_drone = "CREATE TABLE IF NOT EXISTS Drone " +
            "(drone_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            " serial_number  varchar(20) not null UNIQUE, " +
            " model  varchar(20)  not null, " +
            " weight  int," +
            " state  varchar(20), " +
            " capacity  int  not null)";

    public static String sql_medication = "CREATE TABLE IF NOT EXISTS Medication " +
            "(medics_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            " name  varchar(20)  not null, " +
            " code  varchar(10)  not null UNIQUE, " +
            " weight   int   not  null, " +
            " image  varchar(50))";

    public static String sql_dispatch = "CREATE TABLE IF NOT EXISTS Dispatch " +
            "(id INT IDENTITY(1,1)  PRIMARY KEY," +
            " code  varchar(10) , " +
            " serial_number   varchar(100), " +
            " created_date  date not null, " +
            " status varchar(20) not null, " +
            " FOREIGN KEY (code) REFERENCES Medication(code), "+
            " FOREIGN KEY (serial_number) REFERENCES Drone(serial_number))";

    public static Connection db_connect() {
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(SqlQuery.url);
        } catch (SQLException e ) {
            System.out.println(e.getMessage());
        }catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        return conn;
    }

    //Todo filter status
    public static String view_drones_medication() throws SQLException{
        Connection conn = db_connect();
        Statement stmt = conn.createStatement();

        String d_query = "select * from Drone";
        String m_query = "select * from Medication";
        ResultSet m_resultSet = stmt.executeQuery(m_query);
        ArrayList<Medication> medicationArrayList = new ArrayList<>();

        while (m_resultSet.next()){
            Medication medication = new Medication(m_resultSet.getString(2),m_resultSet.getString(3),Integer.valueOf(m_resultSet.getString(4)),m_resultSet.getString(5));
            medicationArrayList.add(medication);
        }

        ResultSet d_result =  stmt.executeQuery(d_query);
        ArrayList<Drone> droneArrayList = new ArrayList<>();
        while (d_result.next()){
            Drone drone = new Drone(d_result.getString(2),d_result.getString(3),Integer.valueOf(d_result.getString(4)),d_result.getString(5),Integer.valueOf(d_result.getString(6)));
            droneArrayList.add(drone);
        }
        String js_result = new Jasonparser().getAllRecords(droneArrayList,medicationArrayList);
        stmt.close();
        conn.close();
        return js_result;

    }

    public static void insert_data(Object obj)  {
        String query = null;
        if (obj instanceof Drone){
           Drone drone = (Drone) obj;
           query =  "insert into Drone(serial_number,model,weight,state,capacity) values ('"+drone.getSerial_number()+"','"+drone.getModel()+"',"+drone.getWeight()+",'"+drone.getState()+"',"+drone.getBattery_capacity()+")";
       }
       if (obj instanceof Medication){
           Medication medication = (Medication) obj;
           query = "insert into Medication(weight,code,name,image) values ("+medication.getWeight() +",'"+medication.getCode()+"','"+medication.getName()+"','"+medication.getImage()+"')";
       }
        try {
            Connection conn = db_connect();
            Statement stmt = conn.createStatement();
        stmt.executeUpdate(query);
        stmt.close();
        conn.close();

        }catch (Exception e){
           // e.printStackTrace();
        }
    }

    //Query to retrieve  single drone or medication.
    public static Object getItem(String id,String table,boolean sendItemList) throws SQLException {
        Connection conn = db_connect();
        String searchcolumn = (table == "Drone")?"serial_number":"code";
        PreparedStatement pstmt = conn.prepareStatement(" select * from "+table+" where "+searchcolumn+" = ?");

        pstmt.setString(1, id);
        ResultSet results = pstmt.executeQuery();
        ResultSetMetaData rsmd = results.getMetaData();
        //Get the column type
        int column_count = rsmd.getColumnCount();
        JSONObject js = new JSONObject();
        int i = 0;
        while(results.next()) {
            for (int x = 2 ;x<=column_count;x++) {
                js.put(rsmd.getColumnName(x), results.getString(x));
            }
            i++;
        }

        if (sendItemList)return js;
        return i;
    }

    //Query to get single medication with code


}
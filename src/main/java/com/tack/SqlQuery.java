package com.tack;

import org.json.JSONObject;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;

public class SqlQuery {
    static String ready = "READY";
    static String gone = "GONE";
    public static String url = "jdbc:sqlite:"+Util.getUserResourceDir()+ "/ola.db";


    public static String sql_drone = "CREATE TABLE IF NOT EXISTS Drone " +
            "(drone_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            " serial_number  varchar(20) not null UNIQUE, " +
            " model  varchar(20)  not null, " +
            " weight  int," +
            " state  varchar(20), " +
            " capacity  int  not null," +
            " lastmodifiedtime timestamp default current_timestamp )";

    public static String sql_medication = "CREATE TABLE IF NOT EXISTS Medication " +
            "(medics_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            " name  varchar(20)  not null, " +
            " code  varchar(10)  not null UNIQUE, " +
            " weight   int   not  null, " +
            " image  varchar(50)," +
            " state varchar(20) default " +"'"+ready+"'"+" not null)";


    public static String sql_dispatch = "CREATE TABLE IF NOT EXISTS Dispatch " +
            "(id integer PRIMARY KEY AUTOINCREMENT," +
            " code  varchar(10) , " +
            " serial_number   varchar(100), " +
            " dispatchtime timestamp default current_timestamp, " +
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


    public static String view_drones_medication(String requestUrl) throws SQLException{
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
        String js_result = new Jasonparser().getAllRecords(droneArrayList,medicationArrayList,requestUrl);
        stmt.close();
        conn.close();
        return js_result;

    }

    public static void insert_data(Object obj)  {
        String query = null;
        String m_up_query = null;
        String d_up_query = null;
        Item item = null;
        boolean updateDroneMedicationTable = false;
        if (obj instanceof Drone){
           Drone drone = (Drone) obj;
           query =  "insert into Drone(serial_number,model,weight,state,capacity) values ('"+drone.getSerial_number()+"','"+drone.getModel()+"',"+drone.getWeight()+",'"+drone.getState()+"',"+drone.getBattery_capacity()+")";
       }
       if (obj instanceof Medication){
           Medication medication = (Medication) obj;
           query = "insert into Medication(weight,code,name,image) values ("+medication.getWeight() +",'"+medication.getCode()+"','"+medication.getName()+"','"+medication.getImage()+"')";
       }

        //Add dispatch details and update Drone status
        //after all other conditions are met.
        if (obj instanceof Item){
            updateDroneMedicationTable = true;
           item = (Item) obj;
           //
           String status = "LOADING";
           query = "insert into Dispatch(code,serial_number,status) values ("+"'"+item.getMedication()+"',"+"'"+item.getDrone()+"',"+"'"+status+"')";
           System.out.println("----"+query+"----------");
           //Todo update other tables
       }
        if (updateDroneMedicationTable){
            d_up_query  = "update Drone set state="+"'LOADING'"+" where serial_number='"+item.getDrone()+"'";
            m_up_query = "update Medication set state="+"'LOADING'"+" where code='"+item.getMedication()+"'";
        }
        try {
            Connection conn = db_connect();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
            //stmt.close();
            if (updateDroneMedicationTable){
                int result = stmt.executeUpdate(d_up_query);
                System.out.println(result);
                stmt.executeUpdate(m_up_query);
                stmt.close();
            }
            if (stmt != null)stmt.close();

        conn.close();

        }catch (Exception e){
           e.printStackTrace();
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

    public static String getLoadedItem(){
        //Todo
        return " ";
    }

    public static Object view_battery(){
        Connection conn = db_connect();
        String query = "select serial_number from Drone where capacity<25";
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int column_count = resultSetMetaData.getColumnCount();
            int row_count = 0;
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            System.out.println("The following devices need charging @"+now );
            while (resultSet.next()){
                for (int x = 1 ;x<=column_count;x++) {
                    //Send notification via mail
                    System.out.println(resultSetMetaData.getColumnName(x)+":"+ resultSet.getString(x));
                }
                row_count++;
            }
            return row_count;
        }catch (SQLException e){
            e.printStackTrace();
            return 0;
        }
        //TOdo
    }


}
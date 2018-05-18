
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author cb-kiruthika
 */
class DirectoryDB {

    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    PreparedStatement insert = null;
    PreparedStatement search = null;
    PreparedStatement update = null;
    Long searchKey;
    
    
    void connect() throws SQLException {
        
        //connect to db
        conn = DriverManager.getConnection("jdbc:mysql://localhost/test?" + "user=root&password=");
        stmt = conn.createStatement();
        insert = conn.prepareStatement("INSERT INTO PD VALUES(NULL,?,?,?,?,?);");
        search = conn.prepareStatement("SELECT * FROM PD WHERE NAME REGEXP ? OR MOBILE REGEXP ? OR HOME REGEXP ? OR WORK REGEXP ?;");
        //update = conn.prepareStatement("UPDATE PD SET ? = ? WHERE ID = ?;");

    }

    void loadCSV(String path) throws SQLException {
        
        //create db and table from csv
        stmt.executeUpdate("DROP DATABASE IF EXISTS PHONE_DIRECTORY;");
        stmt.executeUpdate("CREATE DATABASE PHONE_DIRECTORY;");
        stmt.executeUpdate("USE PHONE_DIRECTORY;");
        stmt.executeUpdate("CREATE TABLE PD(ID BIGINT NOT NULL AUTO_INCREMENT,NAME VARCHAR(20),ADDRESS VARCHAR(100),MOBILE VARCHAR(10),HOME VARCHAR(10),WORK VARCHAR(10),PRIMARY KEY (ID));");

        stmt.executeUpdate("LOAD DATA INFILE \'" + path + " \'INTO TABLE PD FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\\n' IGNORE 1 ROWS (NAME,ADDRESS,MOBILE,HOME,WORK);");

    }

    void disconnect() throws SQLException {
        if (rs != null) {
            rs.close();
            rs = null;
        }
        if (stmt != null) {
            stmt.close();
            stmt = null;
        }
        if (conn != null) {
            conn.close();
            conn = null;
        }
    }

    int searchString(String find) throws SQLException {
        
        //search name and numbers;
        search.setString(1, find);
        search.setString(2, find);
        search.setString(3, find);
        search.setString(4, find);
        rs = search.executeQuery();
        int count = 0;
        if (!(rs.next())) {
            System.out.println("NO MATCH");
        } else {
            do {
                count++;
                System.out.println("Name: " + rs.getString(2));
                System.out.println("Address: " + rs.getString(3));
                System.out.println("Mobile: " + rs.getString(4));
                System.out.println("Home: " + rs.getString(5));
                System.out.println("Work: " + rs.getString(6));
            } while (rs.next());
        }
        return count;
    }

    void addData(String name, String address, String mobile, String home, String work) throws SQLException {
        insert.setString(1, name);
        insert.setString(2, address);
        insert.setString(3, mobile);
        insert.setString(4, home);
        insert.setString(5, work);
        System.out.println(insert.executeUpdate()+" was inserted");
    }


    boolean updateSearch(String find) throws SQLException {
        search.setString(1, find);
        search.setString(2, find);
        search.setString(3, find);
        search.setString(4, find);
        rs = search.executeQuery();
        int count = 0;
        if (!(rs.next())) {
            System.out.println("NO MATCH");
        } else {
            do {
                count++;
                searchKey = rs.getLong(1);
                System.out.println("Name: " + rs.getString(2));
                System.out.println("Address: " + rs.getString(3));
                System.out.println("Mobile: " + rs.getString(4));
                System.out.println("Home: " + rs.getString(5));
                System.out.println("Work: " + rs.getString(6));
            } while (rs.next());
        }
        if(count == 1){
            return true;
        }else{
            return false;
        }
    
    }

    void update(String field, String value) throws SQLException {
        field = field.toUpperCase();
        
        update = conn.prepareStatement("UPDATE PD SET "+field+" = ? WHERE ID = ?;");
        update.setString(1,value);
        update.setLong(2, searchKey);
        System.out.println(update.executeUpdate()+"was updated");
    }
    
}

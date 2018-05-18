
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
    
    void connect() throws SQLException {
        
        //connect to db
        conn = DriverManager.getConnection("jdbc:mysql://localhost/test?" + "user=root&password=");
        stmt = conn.createStatement();

    }

    void loadCSV(String path) throws SQLException {
        
        //create db and table from csv
        stmt.executeUpdate("DROP DATABASE IF EXISTS PHONE_DIRECTORY;");
        stmt.executeUpdate("CREATE DATABASE PHONE_DIRECTORY;");
        stmt.executeUpdate("USE PHONE_DIRECTORY;");
        stmt.executeUpdate("CREATE TABLE PD(NAME VARCHAR(20),ADDRESS VARCHAR(100),MOBILE VARCHAR(10),HOME VARCHAR(10),WORK VARCHAR(10));");

        stmt.executeUpdate("LOAD DATA INFILE \'" + path + " \'INTO TABLE PD FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\\n' IGNORE 1 ROWS;");

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

    void searchString(String find) throws SQLException {
        
        //search name and numbers;
        rs = stmt.executeQuery("SELECT * FROM PD WHERE NAME REGEXP \'" + find + "\' OR MOBILE REGEXP \'" + find + "\' OR HOME REGEXP \'" + find + "\' OR WORK REGEXP \'" + find + "\';");
        
        if (!(rs.next())) {
            System.out.println("NO MATCH");
        } else {
            do {
                System.out.println("Name: " + rs.getString(1));
                System.out.println("Address: " + rs.getString(2));
                System.out.println("Mobile: " + rs.getString(3));
                System.out.println("Home: " + rs.getString(4));
                System.out.println("Work: " + rs.getString(5));
            } while (rs.next());
        }
    }

}

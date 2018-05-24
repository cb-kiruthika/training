
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    static Connection conn = null;
    static Statement stmt = null;
    static ResultSet rs = null;
    static Long searchKey;
    
    
    static void connect() throws SQLException {
        
        //connect to db
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DirectoryDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        conn = DriverManager.getConnection("jdbc:mysql://localhost/test?" + "user=root&password=");
        stmt = conn.createStatement();
        stmt.execute("USE PHONE_DIRECTORY");
        
        
        //update = conn.prepareStatement("UPDATE PD SET ? = ? WHERE ID = ?;");

    }



    static void disconnect() throws SQLException {
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

    static HashMap<String,ArrayList<String>> searchString(String find) throws SQLException {
        HashMap<String,ArrayList<String>> contacts = new HashMap<>();
        
        PreparedStatement search = conn.prepareStatement("SELECT * FROM PD WHERE NAME REGEXP ? OR MOBILE REGEXP ? OR HOME REGEXP ? OR WORK REGEXP ?;");
        
        search.setString(1, find);
        search.setString(2, find);
        search.setString(3, find);
        search.setString(4, find);
        rs = search.executeQuery();
        
        contacts.put("name", new ArrayList<>());
        contacts.put("address", new ArrayList<>());
        contacts.put("mobile", new ArrayList<>());
        contacts.put("home", new ArrayList<>());
        contacts.put("work", new ArrayList<>());
        //rs.last();
        
        while ((rs.next())) {
            int a = rs.getRow();
            contacts.get("name").add(rs.getString(2));
            contacts.get("address").add(rs.getString(3));
            contacts.get("mobile").add(rs.getString(4));
            contacts.get("home").add(rs.getString(5));
            contacts.get("work").add(rs.getString(6));
            
        }
        return contacts;
    }

    static boolean addData(String name, String address, String mobile, String home, String work) throws SQLException {
        
        PreparedStatement insert = conn.prepareStatement("INSERT INTO PD VALUES(NULL,?,?,?,?,?);");
        insert.setString(1, name);
        insert.setString(2, address);
        insert.setString(3, mobile);
        insert.setString(4, home);
        insert.setString(5, work);
        return (insert.executeUpdate()==1);
    }

}

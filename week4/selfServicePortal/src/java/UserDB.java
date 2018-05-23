
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
class UserDB {

    Connection conn;
    Statement stmt;
    ResultSet rs;

    UserDB() {
        this.rs = null;
        this.stmt = null;
        this.conn = null;
    }

    void connect() throws SQLException {

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        conn = DriverManager.getConnection("jdbc:mysql://localhost/test?" + "user=root&password=");
        stmt = conn.createStatement();
        stmt.execute("USE self_service_portal");
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

    int add(String firstName, String lastName, String email, String password) throws SQLException {

        PreparedStatement add = conn.prepareStatement("INSERT INTO users VALUES(?,?,?,?,NULL)");
        add.setString(1, email);
        add.setString(2, password);
        add.setString(3, firstName);
        add.setString(4, lastName);
        return add.executeUpdate();
    }

    int delete(String email) throws SQLException {
        PreparedStatement add = conn.prepareStatement("DELETE FROM users WHERE email = ?");
        add.setString(1, email);
        return add.executeUpdate();

    }

    int update(String email, String firstName, String lastName, String address) throws SQLException {
        PreparedStatement up = conn.prepareStatement("UPDATE users SET first_name = ?, last_name = ?, address = ? WHERE email = ?");

        up.setString(1, firstName);
        up.setString(2, lastName);
        up.setString(3, address);
        up.setString(4, email);

        return up.executeUpdate();
    }

    int update(String email, String address) throws SQLException {
        PreparedStatement up = conn.prepareStatement("UPDATE users SET address = ? WHERE email = ?");

        up.setString(1, address);
        up.setString(2, email);

        return up.executeUpdate();
    }

    User get(String email) throws SQLException {
        PreparedStatement get = conn.prepareStatement("SELECT * FROM users WHERE email = ?");

        get.setString(1, email);

        rs = get.executeQuery();

        User user = new User();
        while (rs.next()) {
            user.email = rs.getString("email");
            user.firstName = rs.getString("first_name");
            user.lastName = rs.getString("last_name");
            user.address = rs.getString("address");
        }
        return user;
    }

    String getPassword(String email) throws SQLException {
        PreparedStatement get = conn.prepareStatement("SELECT password FROM users WHERE email = ?");

        get.setString(1, email);

        rs = get.executeQuery();

        while (rs.next()) {
            return rs.getString(1);
        }
        return null;
    }

}

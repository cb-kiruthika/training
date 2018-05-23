
import java.sql.Connection;
import java.sql.DriverManager;
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
public class Users {

    private UserDB db;

    public Users() {
        this.db = new UserDB();
    }

    public User addUser(String firstName, String lastName, String email, String password) throws SQLException {
        db.connect();
        String checkExisting = db.getPassword(email);
        if (checkExisting != null) {
            return null;
        }
        int row = db.add(firstName, lastName, email, password);
        User user = new User();
        if (row == 1) {
            user = db.get(email);
        }
        db.disconnect();
        return user;
    }

    public boolean updateUser(String email, String address) throws SQLException {
        db.connect();
        int out = db.update(email, address);
        db.disconnect();
        return (out == 1);
    }

    public boolean updateUser(String email, String firstName, String lastName, String address) throws SQLException {
        db.connect();
        int out = db.update(email, firstName, lastName, address);
        db.disconnect();
        return (out == 1);
    }

    public boolean deleteUser(String email) throws SQLException {
        db.connect();
        int out = db.delete(email);
        db.disconnect();
        return (out == 1);
    }

    public User loginUser(String email, String password) throws SQLException {
        db.connect();
        String dbPassword = db.getPassword(email);

        User user = new User();
        if (dbPassword == null) {
            db.disconnect();
            return null;
        } else if ((dbPassword.equals(password))) {
            user = db.get(email);
            db.disconnect();
            return user;
        } else {
            db.disconnect();
            return null;
        }

    }

}

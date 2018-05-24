
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author cb-kiruthika
 */
class PhoneDirectory {

    static boolean add(String name, String address, String mobile, String home, String work) throws SQLException {
        
        DirectoryDB.connect();
        boolean add = DirectoryDB.addData(name, address, mobile, home, work);
        DirectoryDB.disconnect();
        
        return add;
    }

    static HashMap<String,ArrayList<String>> search(String find) throws SQLException {
        
        DirectoryDB.connect();
        HashMap<String,ArrayList<String>> contacts = DirectoryDB.searchString(find);
        DirectoryDB.disconnect();
        return contacts;
    }
    
}

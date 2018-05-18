
import java.sql.SQLException;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author cb-kiruthika
 */
public class PhoneDirectory {
    public static void main(String[] args) throws SQLException{
        
        String path = "/Users/cb-kiruthika/training/week3/day5/PhoneDirectory.csv";
        
        DirectoryDB ddb = new DirectoryDB();
        ddb.connect();
        ddb.loadCSV(path);
        
        Scanner in = new Scanner(System.in);
        String find = in.next();
        
        //get search string
        while (!(find.equals("exit") | find.equals("quit"))) {
            ddb.searchString(find);
            find = in.next();
        }
        
        ddb.disconnect();
    }
}


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

    public static void main(String[] args) throws SQLException {

        String path = "/Users/cb-kiruthika/training/week3/day5/PhoneDirectory.csv";

        DirectoryDB ddb = new DirectoryDB();
        ddb.connect();
        ddb.loadCSV(path);

        Scanner in = new Scanner(System.in);
        String option = "";

        //get search string
        while (!(option.equals("exit") | option.equals("quit"))) {
            System.out.println("search/add/update");
            option = in.next();
            if (option.equalsIgnoreCase("search")) {
                System.out.println("\nEnter string to search");
                String find = in.next();
                while (!(find.equals("exit") | find.equals("quit"))) {
                    ddb.searchString(find);
                    System.out.println("\nEnter string to search");
                    find = in.next();
                }
            }else if (option.equalsIgnoreCase("add")) {
                System.out.println("Enter name to add");
                String name = in.next();
                System.out.println("Enter address to add");
                String address = in.next();
                System.out.println("Enter mobile to add");
                String mobile = in.next();
                System.out.println("Enter home number to add");
                String home = in.next();
                System.out.println("Enter work number to add");
                String work = in.next();
                ddb.addData(name,address,mobile,home,work);
            }else if (option.equalsIgnoreCase("update")) {
                
                System.out.println("\nEnter data to search ");
                String find = in.next();
                
                while(!(ddb.updateSearch(find))){
                    System.out.println("\nEnter different data to search ");
                    find = in.next();
                }
                
                System.out.println("Enter name/address/mobile/home/work number to change?");
                String field = in.next();
                
                System.out.println("Enter new value");
                String value = in.next();
                ddb.update(field,value);
                
            }

        }

        ddb.disconnect();
    }
}

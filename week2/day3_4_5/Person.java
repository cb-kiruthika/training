
import org.apache.commons.csv.CSVRecord;
import org.json.JSONException;
import org.json.JSONObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author cb-kiruthika
 */
public class Person {

    String name;
    String address;
    String mobile;
    String home;
    String work;

    Person(String[] person) {
        this.name = person[0];
        this.address = person[1];
        this.mobile = person[2];
        if (person.length > 3) {
            this.home = person[3];
            if (person.length > 4) {
                this.work = person[4];
            }
        }
    }

    Person(CSVRecord person) {
        this.name = person.get("name");
        this.address = person.get("address");
        this.mobile = person.get("mobile");
        if (person.get("home") != null) {
            this.home = person.get("home");
        }
        if (person.get("work") != null) {
            this.work = person.get("work");
        }
    }
    
    Person(JSONObject person) throws JSONException{
        this.name = person.getString("name");
        this.address = person.getString("address");
        this.mobile = person.getString("mobile");
        if (person.get("home") != null) {
            this.home = person.getString("home");
        }
        if (person.get("work") != null) {
            this.work = person.getString("work");
        }
    }

    public void displayPerson() {

        System.out.println();
        System.out.println("Name:    " + this.name);
        System.out.println("Address: " + this.address);
        System.out.println("Mobile:  " + this.mobile);
        if ((this.home != null) & (this.home!="")) {
            System.out.println("Home:    " + this.home);
        }
        if ((this.work != null) & (this.work!="")) {
            System.out.println("Work:    " + this.work);
        }

        System.out.println();
    }

}

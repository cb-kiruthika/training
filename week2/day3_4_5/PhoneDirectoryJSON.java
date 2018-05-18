
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author cb-kiruthika
 */
public class PhoneDirectoryJSON {
    
    private TreeMap<String, ArrayList<Person>> directory = new TreeMap<>();

    public static void main(String[] args) throws IOException, FileNotFoundException, JSONException {
        String inputPath = "/Users/cb-kiruthika/training/week2/day3/phoneDirectory.json";
        PhoneDirectoryJSON pd = new PhoneDirectoryJSON(inputPath);
        Scanner in = new Scanner(System.in);
        String find = in.next();
        while (!(find.equals("exit") | find.equals("quit"))) {
            pd.searchPerson(find);
            find = in.next();
        }

    }

    PhoneDirectoryJSON(String pathToInput) throws FileNotFoundException, IOException, JSONException {
        JSONObject input = new JSONObject(new JSONTokener(new FileReader(pathToInput)));
        JSONArray contacts = ((JSONArray) input.get("Contacts"));
        for(int i = 0;i<contacts.length();i++){
            
            Person p = new Person(contacts.getJSONObject(i));
            if (!(this.directory.containsKey(p.name))) {
                this.directory.put(p.name, new ArrayList<>());
            }
            this.directory.get(p.name).add(p);
        }
}

    private void searchPerson(String find) {
        ArrayList<Person> result = new ArrayList<>();
        if (Character.isDigit(find.charAt(0))) {
            result = this.searchNumber(find);
        } else if (Character.isLetter(find.charAt(0))) {
            result = this.searchName(find);
        }
        if ((result != null) & (!(result.isEmpty()))) {
            for (Person p : result) {
                p.displayPerson();
            }
        } else {
            System.out.println("NO MATCH");
        }
    }

    private ArrayList<Person> searchNumber(String find) {
        Pattern pattern = Pattern.compile(find);
        Iterator<ArrayList<Person>> it = this.directory.values().iterator();
        ArrayList<Person> result = new ArrayList<Person>();
        while (it.hasNext()) {
            for (Person p : it.next()) {
                Matcher m = pattern.matcher(p.mobile);
                if ((m.find())) {
                    result.add(p);
                } else if (p.home != null) {
                    m = pattern.matcher(p.home);
                    if (m.find()) {
                        result.add(p);
                    } else if (p.work != null) {
                        m = pattern.matcher(p.work);
                        if (m.find()) {
                            result.add(p);
                        }
                    }
                }
            }
        }
        return result;

    }

    private ArrayList<Person> searchName(String find) {
        ArrayList<Person> result = new ArrayList<Person>();
        if (this.directory.containsKey(find)) {
            result.addAll(this.directory.get(find));
        } else {
            Pattern p = Pattern.compile(find);
            for (String name : this.directory.keySet()) {
                Matcher m = p.matcher(name);
                if (m.find()) {
                    result.addAll(this.directory.get(name));
                }
            }
        }
        return result;
    }
}

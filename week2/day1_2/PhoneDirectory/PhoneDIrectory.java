package PhoneDirectory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class PhoneDirectory {
    // sample data 
    private static final String[][] inputData = {{"abcd", "30, efhg lane, ijkl.", "9123456789", "8765432190"}, {"ashy", "21,litter lane, hest", "7635421309"}, {"riyan", "43, kital road, eastcity", "9988776655", "8899776655", "7744332211"}, {"abcd", "56,rocky street, asia", "9898767654"}};
    
    private TreeMap<String, ArrayList<Person>> directory = new TreeMap<>();

    public static void main(String[] args) {
        PhoneDirectory pd = new PhoneDirectory();
        Scanner in = new Scanner(System.in);
        System.out.println("Enter name / number to search: ");
        String find = "";
        // get search string
        while (!(find.equals("exit") | find.equals("quit"))) {
            find = in.next();
            pd.searchPerson(find);
        }

    }

    PhoneDirectory() {
        //intialize with sample data
        for (String[] prsn : PhoneDirectory.inputData) {
            Person p = new Person(prsn);
            if (!(this.directory.containsKey(p.name))) {
                this.directory.put(p.name, new ArrayList<>());
            }
            this.directory.get(p.name).add(p);
        }
    }

    private void searchPerson(String find) {
        //decide to search number or name 
        ArrayList<Person> result = new ArrayList<>();
        if (Character.isDigit(find.charAt(0))) {
            result = this.searchNumber(find);
        } else if (Character.isLetter(find.charAt(0))) {
            result = this.searchName(find);
        }
        // print result
        if ((result != null) & (!(result.isEmpty()))) {
            for (Person p : result) {
                p.displayPerson();
            }
        } else {
            System.out.println("NO MATCH");
        }
    }

    private ArrayList<Person> searchNumber(String find) {
        //search in person's number
        Pattern pattern = Pattern.compile(find);
        Iterator<ArrayList<Person>> it = this.directory.values().iterator();
        ArrayList<Person> result = new ArrayList<Person>();
        // check all numbers
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
                        m = pattern.matcher(p.home);
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
        //search in person name
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

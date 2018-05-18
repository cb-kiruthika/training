
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
public class WordMap {

    public static void main(String[] args) { //input list of strings
        mapToLength(args);
        
    }

    private static void mapToLength(String[] args) {

        Map<Integer,LinkedList<String>> wordMap = new HashMap<>();

        for(String str:args){

            if(!(wordMap.containsKey(str.length()))){
                //new entry in map
                wordMap.put(str.length(), new LinkedList<>());
            }
            // add to length as key
            wordMap.get(str.length()).add(str);

        }
        // print map
        for(Map.Entry m: wordMap.entrySet()){
            System.out.println(m.getKey()+" - "+ m.getValue().toString().replace("[", "").replace(",", "").replace("]", ""));
        }
    }

   
}

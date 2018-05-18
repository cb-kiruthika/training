
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;



public class PrefixMap {
    
    public static void main(String[] args) { // args list of input strings to be given
        mapToLength(args);
        
    }

    private static void mapToLength(String[] args) {

        Map<String,LinkedList<String>> wordMap = new TreeMap<>();
        //fill map from input
        for(String str:args){
            if(!(wordMap.containsKey(str.substring(0, 3)))){
                wordMap.put(str.substring(0, 3), new LinkedList<>());
            }
            wordMap.get(str.substring(0, 3)).add(str);

        }
        //print map
        for(Map.Entry m: wordMap.entrySet()){
            System.out.println(m.getKey()+" - "+ m.getValue().toString().replace("[", "").replace(",", "").replace("]", ""));
        }
    }

   
}

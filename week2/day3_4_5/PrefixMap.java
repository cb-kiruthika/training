
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author cb-kiruthika
 */


public class PrefixMap {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String[] str = {"hihh", "hihii", "hhh", "jjjj", "dh"};
        mapToLength(str);
        
    }

    private static void mapToLength(String[] args) {
        Map<String,LinkedList<String>> wordMap = new TreeMap<>();
        for(String str:args){
            if(!(wordMap.containsKey(str.substring(0, 2)))){
                wordMap.put(str.substring(0, 2), new LinkedList<>());
            }
            wordMap.get(str.substring(0, 2)).add(str);

        }
        
        for(Map.Entry m: wordMap.entrySet()){
            System.out.println(m.getKey()+" - "+ m.getValue().toString().replace("[", "").replace(",", "").replace("]", ""));
        }
    }

   
}

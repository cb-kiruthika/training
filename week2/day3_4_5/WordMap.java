/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author cb-kiruthika
 */
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
public class WordMap {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String[] str = {"hihh", "hihii", "hhh", "jjjj", "dh"};
        mapToLength(str);
        
    }

    private static void mapToLength(String[] args) {
        Map<Integer,LinkedList<String>> wordMap = new HashMap<>();
        for(String str:args){
            if(!(wordMap.containsKey(str.length()))){
                wordMap.put(str.length(), new LinkedList<>());
            }
            wordMap.get(str.length()).add(str);

        }
        for(Map.Entry m: wordMap.entrySet()){
            System.out.println(m.getKey()+" - "+ m.getValue().toString().replace("[", "").replace(",", "").replace("]", ""));
        }
    }

   
}

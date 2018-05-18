/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author cb-kiruthika
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import org.apache.commons.csv.*;
public class removeDuplicateCSV {
    public static void main(String[] args) throws FileNotFoundException, IOException{
        
        String path = "/Users/cb-kiruthika/training/week2/day3/example.csv";
        String outputPath = "/Users/cb-kiruthika/training/week2/day3/exampleWithoutDuplicate.csv";
        Reader in = new BufferedReader(new FileReader(path));
        CSVParser parser = CSVParser.parse(in,CSVFormat.DEFAULT);
        ArrayList<CSVRecord> records = (ArrayList<CSVRecord>) parser.getRecords();
        for(int i = 0;i <records.size();i++){
            for(int j = i+1; j<records.size();j++){
                if(checkEqual(records.get(i),records.get(j))){
                    records.remove(i);
                    i--;
                    break;
                }
            }
        }
        CSVPrinter out = new CSVPrinter(new PrintWriter(new FileWriter(outputPath)),CSVFormat.DEFAULT);
        for (CSVRecord rec:records){
            System.out.println(rec.toString());
            out.printRecord(rec);
        }
        in.close();
        out.close();
    }

    private static boolean checkEqual(CSVRecord rec1, CSVRecord rec2) {
        
        for(int i = 0; i<rec1.size();i++){
            if(!(rec1.get(i).equals(rec2.get(i)))){
                
                return false;
            }
        }
        return true;
    }
}

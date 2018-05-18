
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
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
public class CSVChanger {

    CSVChanger(String inputPath, String outputPath, String configPath) throws IOException, JSONException, ParseException {

        CSVParser input = new CSVParser(new FileReader(inputPath), CSVFormat.DEFAULT.withFirstRecordAsHeader());
        JSONObject config = new JSONObject(new JSONTokener(new FileReader(configPath)));
        List<CSVRecord> records = input.getRecords();
        List<String[]> output = changeCSV(records, config);
        CSVPrinter out = new CSVPrinter(new PrintWriter(new FileWriter(outputPath)), CSVFormat.DEFAULT);
        for (String[] rec : output) {
            out.printRecord(rec);
        }
        out.close();
    }

    public static void main(String[] args) throws IOException, JSONException, ParseException {
        String input = "/Users/cb-kiruthika/training/week2/day3/Input.csv";
        String output = "/Users/cb-kiruthika/training/week2/day3/output.csv";
        String config = "/Users/cb-kiruthika/training/week2/day3/configuration.json";
        CSVChanger changer = new CSVChanger(input, output, config);
    }

    private List<String[]> changeCSV(List<CSVRecord> input, JSONObject config) throws JSONException, IOException, ParseException {
        
        List<String[]> output = new ArrayList<>();

        /* get CSVheaders from JSON*/
        
        List<String> headerRecord = new ArrayList<>();
        JSONArray headers = ((JSONArray) config.get("fields"));
        for (int j = 0; j < headers.length(); j++) {
            //JSONObject field = headers.getJSONObject(j);
            Iterator it = headers.getJSONObject(j).keys();
            while (it.hasNext()) {
                headerRecord.add((String) it.next());
            }
        }
        output.add((String[]) headerRecord.toArray(new String[0]));

        
        for (CSVRecord record : input) {
            
            /*Each record in input CSV*/
            try{
            List<String> outputRecord = new ArrayList<>();
            for (int i = 0; i < ((JSONArray) config.get("fields")).length(); i++) {
                //json fields
                
                Iterator it = ((JSONArray) config.get("fields")).getJSONObject(i).keys();
                while (it.hasNext()) {
                    //field properties
                    
                    //String title = (String) it.next();
                    JSONObject content = ((JSONArray) config.get("fields")).getJSONObject(i).getJSONObject((String) it.next());
                    //String type = content.getString("type");
                    if (content.getString("type").equals("String")) {
                        
                        outputRecord.add(record.get(content.getString("title")));
                        
                    } else if (content.getString("type").equals("JSON")) {
                        
                        JSONObject json = new JSONObject();
                        Iterator itt = content.getJSONObject("JSONFields").keys();
                        while (itt.hasNext()) {
                            //output json fields
                            
                            String subfield = (String) itt.next();
                            json.put(subfield, record.get(content.getJSONObject("JSONFields").getString(subfield)));
                        }
                        outputRecord.add(json.toString(4));
                        
                    }else if (content.getString("type").equals("Date")) {
                        
                        Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(record.get(content.getString("title")));
                        outputRecord.add(new SimpleDateFormat("MM/dd/yyyy' 'HH:mm':00'").format(date));
                        
                    }
                }
            }

            output.add((String[]) outputRecord.toArray(new String[0]));
            }catch(Exception e){
                System.out.println(e + "in record" + record.getRecordNumber());
            }
        }
        return output;
    }

}

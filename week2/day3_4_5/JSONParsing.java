/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author cb-kiruthika
 */

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class JSONParsing {
    class Student{
        String dateOfJoining;
        String id;
        HashMap<String,Long> marks;
        String name;
        String std;
        
        Student(JSONObject json) throws JSONException{
            this.dateOfJoining = (String) json.get("Date Of Joining");
            this.id = (String) json.get("ID");
            JSONArray arr = json.getJSONArray("Marks");
            this.marks = new HashMap<>();
            for(int i = 0; i<arr.length();i++){
            JSONObject jsn = arr.getJSONObject(i);
            this.marks.put((String) jsn.get("Subject"), (Long) jsn.get("Mark"));
                    }
            this.name = (String) json.get("Name");
            this.std = (String) json.get("Std");
        }
    }
    class Teacher{
            ArrayList<String> classes;
            String dateOfJoining;
            String id;
            String name;
            Long salary;
            Teacher(JSONObject json) throws JSONException{
                this.classes = new ArrayList<>();
                JSONArray arr = json.getJSONArray("Classes Taking Care Of");
                for(int i = 0; i<arr.length();i++){
                    this.classes.add(arr.getString(i));
                }
            this.dateOfJoining = (String) json.get("Date Of Joining");
            this.id = (String) json.get("ID");
            this.name = (String) json.get("Name");
                this.salary = (Long) json.get("Salary");
            }
        }
        Teacher teacher;
    Student student;
    public static void main(String[] args) throws IOException, JSONException{
        
        String path = "/Users/cb-kiruthika/training/week2/day3/students-teachers.json";
        JSONObject jsn = new JSONObject(new JSONTokener(new FileReader(path)));
        JSONParsing obj = new JSONParsing(jsn);
    }
    JSONParsing(JSONObject jsn) throws JSONException{
        this.student = new Student((JSONObject) jsn.get("Student"));
        this.teacher = new Teacher((JSONObject) jsn.get("Teacher"));
    }
}

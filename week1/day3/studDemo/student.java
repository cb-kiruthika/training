package studDemo;
import java.util.*;
import java.io.*;

public class student{
	private int id = 0;
	private String name = "";
	private boolean gender = true;
	public subject subjects = new subject();

	public void setId(int id){
		this.id = id; 
	}
	public void setName(String name){
		this.name = name; 
	}
	public void setGender(String gender){
		if( (gender == "m")||(gender == "male")||(gender == "Male")||(gender == "M")){
			this.gender = false;
		}
		if( (gender == "f")||(gender == "female")||(gender == "Female")||(gender == "F")){
			this.gender = true;
		}
	}
	public int getId(){
		return this.id; 
	}
	public String getName(){
		return this.name; 
	}
	public String getGender(){
		if (this.gender){
			return "Female";
		}else{
			return "Male";
		} 
	}
}
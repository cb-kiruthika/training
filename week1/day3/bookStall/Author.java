package bookStall;
import java.util.*;
import java.io.*;

public class Author{
	private String name;
	private String email;
	private char gender;

	Author(String name, String email, char gender){
		this.name = name;
		this.email = email;
		this.gender = gender;
	}
	public String getName(){
		return this.name;
	}
	public String getEmail(){
		return this.email;
	}
	public void setEmail(String email){
		this.email = email;
	}
	public char getGender(){
		return this.gender;
	}
	public String toDisplay(){
		return (this.name + " (" + this.gender + ") at " + this.email);
	}
	public static void main(String[] args){
		Author a = new Author("R.K. Narayan", "rknarayan@gmail.com", 'M');
		System.out.println(a.toDisplay());
		a.setEmail("narayanrk@gmail.com");
		System.out.println(a.toDisplay());

	
	}
}
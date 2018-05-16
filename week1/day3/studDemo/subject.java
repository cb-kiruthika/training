package studDemo;
import java.util.*;
import java.io.*;

class subject{
	
	private String subject1;
	private String subject2;
	private String subject3;
	private float mark1;
	private float mark2;
	private float mark3;

	public void setSubject2(String subject2){
		this.subject2 = subject2;
	}
	public void setSubject1(String subject1){
		this.subject1 = subject1;
	}
	public void setSubject3(String subject3){
		this.subject3 = subject3;
	}
	public void setMark1(float mark){
		this.mark1 = mark;
	}
	public void setMark2(float mark){
		this.mark2 = mark;
	}
	public void setMark3(float mark){
		this.mark3 = mark;
	}

	public String getSubject1(){
		return this.subject1;
	}
	public String getSubject2(){
		return this.subject2;
	}
	public String getSubject3(){
		return this.subject3;
	}
	public float getMark1(){
		return this.mark1;
	}
	public float getMark2(){
		return this.mark2;
	}
	public float getMark3(){
		return this.mark3;
	}
}

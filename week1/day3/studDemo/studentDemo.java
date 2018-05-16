package studDemo;
import java.util.*;
import java.io.*;

public class studentDemo{
	public static void main(String[] args){
		student stud = new student();
		storeStudentData(stud);
		resultGenerator result = new resultGenerator();
		result.generateResult(stud);

	}
	private static void storeStudentData(student stud){
		Scanner in = new Scanner(System.in);
		System.out.println("Student id?");
		stud.setId(in.nextInt());
		System.out.println("Student name?");
		stud.setName(in.next());
		System.out.println("Student Gender?");
		stud.setGender(in.next());
		System.out.println("Student Subjects");
		System.out.println("Enter three Subjects");
		stud.subjects.setSubject1(in.next());
		stud.subjects.setSubject2(in.next());
		stud.subjects.setSubject3(in.next());
		System.out.println("Enter marks for " + stud.subjects.getSubject1());
		stud.subjects.setMark1(in.nextFloat());
		System.out.println("Enter marks for " + stud.subjects.getSubject2());
		stud.subjects.setMark2(in.nextFloat());
		System.out.println("Enter marks for " + stud.subjects.getSubject3());
		stud.subjects.setMark3(in.nextFloat());
	}
}
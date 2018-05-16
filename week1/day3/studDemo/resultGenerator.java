package studDemo;
import java.util.*;
import java.io.*;
import java.text.*;

class resultGenerator{
	public static void generateResult(student stud){

		DecimalFormat numberFormat = new DecimalFormat("#.00");
		System.out.println();
		System.out.println("REPORT CARD");
		System.out.println();
		System.out.println("Student id: " + stud.getId());
		System.out.println("Student name: " + stud.getName());
		System.out.println("Student gender: " + stud.getGender());
		System.out.println("Student mark details:");
		System.out.println(stud.subjects.getSubject1() + ":  " + stud.subjects.getMark1());
		System.out.println(stud.subjects.getSubject2() + ":  " + stud.subjects.getMark2());
		System.out.println(stud.subjects.getSubject3() + ":  " + stud.subjects.getMark3());
		float average = (stud.subjects.getMark1()+stud.subjects.getMark2()+stud.subjects.getMark3())/3;
		float total = (stud.subjects.getMark1()+stud.subjects.getMark2()+stud.subjects.getMark3());
		System.out.println("Total Marks: " + total);
		System.out.println("Average mark: " + numberFormat.format(average));
		System.out.println();

	}
}
package incomeTax;
import java.util.*;
import java.io.*;

public class TaxDemo{
	public static void main(String[] args){
		Scanner in = new Scanner(System.in);
		System.out.println("Enter employee name: ");
		String name = in.next();
		System.out.println("Gender (m/f): ");
		char gender = in.next().charAt(0);
		System.out.println("Enter taxable income: ");
		Double income = in.nextDouble();
		Employee employee = new Employee(name, gender, income);
		System.out.println(employee.toDisplay());
	}
}
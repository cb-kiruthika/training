package incomeTax;
import java.util.*;
import java.io.*;

public class Employee{
	private String name;
	private boolean gender;
	private Double income;
	private Double tax;

	Employee(String name, char gender, Double income){
		this.name = name;
		this.gender = (gender == 'f')? true : false;
		this.income = income;
		this.tax = TaxCalculator.calculateTax(this.income, this.gender);
	}

	public String toDisplay(){
		char gender = (this.gender)? 'F' : 'M';
		return (this.name + " | " + gender + " | " + this.income + " | " + this.tax);
	}

}
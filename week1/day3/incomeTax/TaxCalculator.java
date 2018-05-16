package incomeTax;
import java.util.*;
import java.io.*;

public class TaxCalculator{
	public static Double calculateTax(Double income, boolean gender){
		if (gender){
			if(income < 250000){
				return 0.0;
			}else if (income < 500000) {
				return (0.05*income);
			}else if (income < 1000000) {
				return (12500 + 0.2*income);
			}else{
				return (112500 + 0.3*income);
			}
		}else{
			if(income < 250000){
				return 0.0;
			}else if (income < 400000) {
				return (0.05*income);
			}else if (income < 800000) {
				return (15000 + 0.2*income);
			}else{
				return (150000 + 0.3*income);
			}

		}
	}
}
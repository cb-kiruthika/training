import java.util.*;
import java.io.*;
public class sumCharacters{
	public static void main(String[] args){
		Scanner in = new Scanner(System.in);
		System.out.println("String?");
		String input = in.next();
		int sum = 0;
		for(int i = 0; i<input.length();i++){
			sum = sum+(input.charAt(i) -'a'+1);
		}
		System.out.println("sum " + sum);
	}
}
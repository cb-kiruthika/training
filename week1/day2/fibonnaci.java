import java.util.*;
import java.io.*;
import java.lang.Math.*;
public class fibonnaci{
	public static void main(String[] args){
		Scanner in = new Scanner(System.in);
		System.out.println("Length of series?");
		int n = in.nextInt();
		System.out.println();
		System.out.print("0 1 ");
		int a = 0;
		int b = 1;

		for(int i=2;i<n;i++){
			int c = a + b;
			System.out.print(c + " ");
			a = b;
			b = c;

		}	

	}
}
import java.util.*;
import java.io.*;

public class pattern{
	public static void main(String[] args){
		Scanner in = new Scanner(System.in);
		int rows = in.nextInt();
		for(int i=1;i<=rows;i++){
			for(int j = 1;j <= rows+i; j++){
				if(j<=rows-i){
					System.out.print(" ");
					System.out.print(" ");
				}
				else if (j<=rows){
					System.out.print(" ");
					System.out.print(j+i-rows);
				}
				else if( j< rows+i ){
					System.out.print(" ");
					System.out.print(rows+i-j);
				}
				
			}
			System.out.println();
		}
	}
}
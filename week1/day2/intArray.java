import java.util.*;
import java.io.*;
import java.lang.Math.*;
public class intArray{
	public static void main(String[] args){
		Scanner in = new Scanner(System.in);
		System.out.println("Length?");
		int n = in.nextInt();
		int[] array = new int[n];
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		for(int i = 0; i<n; i++){
			array[i] = in.nextInt();
			if(array[i]<min){
				min = array[i];
			}
			if(array[i]>max){
				max = array[i];
			}
		}
		System.out.println("max : "+max+"\n min : "+min);
	}
}
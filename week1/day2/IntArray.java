
import java.util.*;
import java.io.*;
import java.lang.Math.*;
public class IntArray{
        public static void main(String[] args){
                Scanner in = new Scanner(System.in);
                System.out.println("Length?");
                int n = in.nextInt();
                int min = Integer.MAX_VALUE;
                int max = Integer.MIN_VALUE;
                for(int i = 0; i<n; i++){
                        int a = in.nextInt();
                        if(a<min){
                                min = a;
                        }
                        if(a>max){
                                max = a;
                        }
                }
                System.out.println("max : "+max+"\n min : "+min);
        }
}
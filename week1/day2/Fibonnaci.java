
import java.util.*;
import java.io.*;
import java.lang.Math.*;
public class Fibonnaci{
        public static void main(String[] args){
                Scanner in = new Scanner(System.in);
                System.out.println("Length of series?");
                System.out.println();
                int n = in.nextInt();
                if( n == 1 ){
                    System.out.println("0");
                }else if(n==2){
                    System.out.println("0 1");
                }else{
                    System.out.println("0 1");
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
}
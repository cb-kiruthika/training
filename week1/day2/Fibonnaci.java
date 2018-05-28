
import java.util.*;
import java.io.*;
import java.lang.Math.*;
public class Fibonnaci{
        public static void main(String[] args){
                Scanner in = new Scanner(System.in);
                System.out.println("Length of series?");
                System.out.println();

                int n = in.nextInt();
                int a = 0;
                int b = 1;
                if(n>=0){
    
                    for(int i=0;i<n;i++){
                            int c = a + b;
                            System.out.print(a + " ");
                            a = b;
                            b = c;
                    }        
                }else{
                    System.out.println("Enter valid positive length ");
                }
                
        }
}
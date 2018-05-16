
import java.util.*;
import java.io.*;
import java.lang.Math.*;
public class Interest{
        public static void main(String[] args){
                Scanner in = new Scanner(System.in);
                System.out.println("Principle?");
                double p = in.nextDouble();
                double r ;
                do{
                        System.out.println("Percentage Interest rate?");
                        r = in.nextDouble();
                }while((r<0)&&(r>100));
                
                System.out.println("No. of times compounded in a year?");
                double n = in.nextInt();
                System.out.println("No.of years?");
                double t = in.nextInt();

                double si = (p*t*r)/100;
                double ci = p*java.lang.Math.pow((1+r/(100*n)),(n*t));

                System.out.println("Simple Interest " + si);
                System.out.println("Compound Interest " + ci);

        }
}
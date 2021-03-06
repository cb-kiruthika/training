
import java.util.*;
import java.io.*;

public class RotateMatrix{
        public static void main(String[] args){

                Scanner in = new Scanner(System.in);
                do{
                        System.out.println("rows?");
                        int rows = in.nextInt();
                        System.out.println("columns?");
                        int cols = in.nextInt();

                        int[][] matrix ;
                        matrix = new int[rows][cols];
                        System.out.println("Inputs");
                        for ( int row= 0; row< rows;row++){
                                for ( int col=0;col< cols;col++){
                                        matrix[row][col] = in.nextInt();
                                }
                        }

                        int[][] output = new int[cols][rows];

                        
                        String rl;
                        do{     
                                System.out.println("right/left/quit?");
                                rl = in.next();
                                if(rl.equalsIgnoreCase("right")){

                                        System.out.println("Rotating to right..");
                                        output = rotate(matrix,rows,cols,false);
                                }else if (rl.equalsIgnoreCase( "left")){

                                        System.out.println("Rotating to left..");
                                        output = rotate(matrix,rows,cols,true);
                                }else{
                                        System.out.println("Enter valid Input");
                                }
                                
                                
                                System.out.println("Input");
                                System.out.println();
                                printMatrix(matrix,rows, cols);
                                        

                                System.out.println("Output");
                                System.out.println();
                                printMatrix(output, cols,rows);
                        }while(!rl.equalsIgnoreCase("quit"));
                        System.out.println("Another input matrix? (y/n)");
                }while(in.next().equalsIgnoreCase("y"));
        }

        public static int[][] rotate(int[][] input,int rows, int cols, boolean direction){

                int[][] output = new int[cols][rows];
                for ( int row= 0; row< rows;row++){
                        for ( int col=0;col< cols;col++){
                                if(direction){
                                        output[col][row] = input[row][cols - col-1];
                                }else{
                                        output[col][row] = input[rows-1-row][col];
                                }
                        }
                        System.out.println();
                }
                return output;
        }
        public static void printMatrix(int[][] matrix,int rows, int cols){
                for (  int row= 0; row< rows;row++){
                        for ( int col=0;col< cols;col++){
                                System.out.print(" ");
                                System.out.print(matrix[row][col]);
                        }
                        System.out.println();
                }
        }
}

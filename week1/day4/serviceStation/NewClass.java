
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

class Factorial implements Iterable{

    Integer lowerLimit;
    Integer upperLimit;

    Factorial(int ll, int ul) {
        this.lowerLimit = ll;
        this.upperLimit = ul;
    }

    @Override
    public Iterator iterator() {
        class FactorialIterator implements Iterator{
            Integer currentIndex = Factorial.this.lowerLimit;
            @Override
            public boolean hasNext() {
                return !(this.currentIndex > Factorial.this.upperLimit);
            }

            @Override
            public Object next() {
                return factorial(currentIndex++);
                
            }

            private long factorial(Integer integer) {
                long result = 1;
                for(int i = integer;i>1;i--){
                    result = result*i;
                }
                return result;
            }
            
        }
        Iterator factorialIterator = new FactorialIterator();
        return factorialIterator;
    }
    
    @Override
    public String toString(){
        String factorialString = "";
        Iterator it = this.iterator();
        while(it.hasNext()){
            factorialString = factorialString + it.next().toString()+", ";
        }
        
        return factorialString.substring(0, factorialString.length()-2);
    }
            
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        System.out.println("Enter lowerlimit");
        int ll = in.nextInt();
        System.out.println("Enter upperlimit");
        int ul = in.nextInt();
        Factorial factorial = new Factorial(ll,ul);
        System.out.println(factorial.toString());
    }

}

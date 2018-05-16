package animalKingdom;
import java.util.*;
import java.io.*;
public class AnimalKingdom{
	public static void main(String[] args){
		Scanner in = new Scanner(System.in);
		int n = 5;
		String flyable = "Animals ";
		String herbivore = "Animals ";

		Animal[] animals = new Animal[n];
		for(int i = 0; i < n; i++){
			System.out.println("Animal " + (i+1) + "? ");
			String a = in.next();
			if(a.equals("bat")){
				animals[i] = new bat();
			}else if(a.equals("cow")){
				animals[i] = new cow();
			}else if(a.equals("ostrich")){
				animals[i] = new ostrich();
			}else if(a.equals("parrot")){
				animals[i] = new parrot();
			}else if(a.equals("dog")){
				animals[i] = new dog();
			}
			animals[i].movement();
			animals[i].food();
			animals[i].isMammal();
			if(animals[i].canFly()){
				flyable = flyable + (i+1) + ", ";
			}
			if(animals[i].herbivore()){
				herbivore = herbivore + (i+1) + ", ";
			}
		}
		System.out.println(flyable + "can fly");
		System.out.println(herbivore + "are herbivores");

	}
}
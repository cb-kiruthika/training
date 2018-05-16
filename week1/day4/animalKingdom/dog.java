package animalKingdom;
public class dog extends mammal implements herbivore{
	public void food(){
		System.out.println("This animal is an omnivore");
	}
	public boolean herbivore(){
		return false;
	}
	
}
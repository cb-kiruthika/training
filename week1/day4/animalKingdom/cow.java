package animalKingdom;
public class cow extends mammal implements herbivore{
	public void food(){
		System.out.println("This animal is an herbivore");
	}
	public boolean herbivore(){
		return true;
	}
	
}
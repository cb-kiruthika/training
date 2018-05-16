package animalKingdom;
public class parrot extends bird implements herbivore{
	public void food(){
		System.out.println("This animal is an omnivore");
	}
	public boolean herbivore(){
		return false;
	}	
}
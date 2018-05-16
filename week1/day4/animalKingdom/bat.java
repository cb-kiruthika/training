package animalKingdom;
public class bat extends mammal implements herbivore{
	@Override
	public void movement(){
		System.out.println("This animal can fly");
	}
	public void food(){
		System.out.println("This animal is an omnivore");
	}
	@Override
	public boolean canFly(){
		return true;
	}
	public boolean herbivore(){
		return false;
	}
}
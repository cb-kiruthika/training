package animalKingdom;
public class ostrich extends bird implements herbivore{
	@Override
	public void movement(){
		System.out.println("This animal can fly");
	}
	public void food(){
		System.out.println("This animal is an omnivore");
	}
	@Override
	public boolean canFly(){
		return false;
	}
	public boolean herbivore(){
		return false;
	}

}
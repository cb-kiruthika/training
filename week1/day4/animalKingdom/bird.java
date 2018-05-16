package animalKingdom;
public abstract class bird extends Animal implements fly{
	public void movement(){
		System.out.println("This animal can fly");
	}
	public void isMammal(){
		System.out.println("This animal is not a mammal");
	}
	public boolean canFly(){
		return true;
	}
}
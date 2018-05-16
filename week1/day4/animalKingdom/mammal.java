package animalKingdom;
public abstract class mammal extends Animal implements fly{
	public void isMammal(){
		System.out.println("This animal is a mammal");
	}
	public void movement(){
		System.out.println("This animal cannot fly");
	}
	public boolean canFly(){
		return false;
	}
}
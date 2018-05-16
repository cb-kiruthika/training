package carsDemo;

public class MyOwnAutoShop{
	public static void main(String[] args){
		Sedan sedan = new Sedan(80, 2500000.0, "grey", 25);
		Ford ford_1 = new Ford(90, 3500000.0, "black", 2018, 100000);
		Ford ford_2 = new Ford(70, 1500000.0, "blue", 2017, 200000);
		Car car = new Car(100, 3000000.0, "silver");
		System.out.println("sedan: " + sedan.getSalePrice());
		System.out.println("ford_1: " + ford_1.getSalePrice());
		System.out.println("ford_2: " + ford_2.getSalePrice());
		System.out.println("car: " + car.getSalePrice());
	}
}
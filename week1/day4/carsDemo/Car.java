package carsDemo;
class Car{
	int speed;
	double regularPrice;
	String color;
	double getSalePrice(){
		return this.regularPrice;
	}

	Car(int sp, double rp, String col){
		this.speed = sp;
		this.regularPrice = rp;
		this.color = col;
	}
}
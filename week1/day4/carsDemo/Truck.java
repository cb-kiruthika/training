package carsDemo;
class Truck extends Car{
	int weight;
	double getSalePrice(){
		if(this.weight > 2000){
			return 0.9*super.getSalePrice();
		}else{
			return 0.8* super.getSalePrice();
		}
	}
	Truck(int sp, double rp, String col, int wt){
		super(sp,rp,col);
		this.weight = wt;
	}
}
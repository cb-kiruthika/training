package carsDemo;
class Ford extends Car{
	int year;
	int manufactureDiscount;
	double getSalePrice(){
		return (super.getSalePrice() - this.manufactureDiscount);
	}
	Ford(int sp, double rp, String col, int yr, int md){
		super(sp,rp,col);
		this.year = yr;
		this.manufactureDiscount = md;
	}

}
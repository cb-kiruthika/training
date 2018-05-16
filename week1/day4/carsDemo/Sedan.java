package carsDemo;
class Sedan extends Car{
	int length;
	double getSalePrice(){
		if(this.length > 20){
			return 0.95*super.getSalePrice();
		}else{
			return 0.9* super.getSalePrice();
		}
	}
	Sedan(int sp, double rp, String col, int ln){
		super(sp,rp,col);
		this.length  = ln;
	}
}
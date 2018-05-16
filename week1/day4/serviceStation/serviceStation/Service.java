package serviceStation;
class Service {
    String type;
    double serviceCharge;
    Service(String type){
        this.type = type;
        switch (this.type) {
            case "Car":
                this.serviceCharge = 1000;
                break;
            case "Bike":
                this.serviceCharge = 500;
                break;
            case "Bus":
                this.serviceCharge = 5000;
                break;
            default:
                System.out.println("Error in type");
                this.serviceCharge = 0;
                break;
        }
    }
}

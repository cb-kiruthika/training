package serviceStation;
class Vehicle{
    String brand;
    String color;
    Service service;
    Vehicle(String brand, String color,String type){
        this.brand = brand;
        this.color = color;
        this.service = new Service(type);
    }
    String getVehicleDetails(){
        return (color + " " + brand);
    }
    
}
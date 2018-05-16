package serviceStation;
class Invoice{
    String name_of_owner;
    Vehicle vehicle;
    double amount_total;
    Employee employee_assigned;
    Invoice(Customer c, Vehicle v, Employee e){
        this.name_of_owner = c.name;
        this.vehicle = v;
        this.employee_assigned = e;
        this.amount_total = v.service.serviceCharge;        
    }
    void printInvoice(){
        System.out.println("Owner name: "+this.name_of_owner);
        System.out.println("Vehicle: " + this.vehicle.getVehicleDetails());
        System.out.println("Total Amount: " + this.amount_total);
        System.out.println("Employee : " + this.employee_assigned.getEmployee());
    }
}
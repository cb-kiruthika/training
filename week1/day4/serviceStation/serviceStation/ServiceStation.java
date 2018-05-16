package serviceStation;
public class ServiceStation{
    String name;
    String address;
    int contact;
    Employee[] emplpyees = new Employee[100];
    Customer[] customers = new Customer[100];
    Vehicle[] vehicles = new Vehicle[100];
    
    public static void main(String[]args){
        ServiceStation ss = new ServiceStation();
        ss.printServiceCharges();
        ss.emplpyees[0] = new Employee("emp_1", 30, "9999988888", 4321);
        ss.emplpyees[1] = new Employee("emp_2", 35, "9777788888", 4121);
        ss.customers[0] = new Customer("cust_1", 25, "9111122222");
        ss.vehicles[0] = new Vehicle("Ford", "grey", "Car");
        ss.customers[1] = new Customer(ss.emplpyees[1]);
        ss.vehicles[1] = new Vehicle("Honda", "blue", "Bike");
        
        System.out.println();
        Invoice i;
        i = new Invoice(ss.customers[0],ss.vehicles[0],ss.emplpyees[1]);
        i.printInvoice();
        
        System.out.println();
        i = new Invoice(ss.customers[1],ss.vehicles[1],ss.emplpyees[0]);
        i.printInvoice();
    }
    
    void printServiceCharges(){
        String[] types = {"Bus", "Car", "Bike"};
        System.out.println("Service Charges: ");
        for(String type: types){
            System.out.println(type + ": " + (new Service(type).serviceCharge));
        }
    }
}
package serviceStation;
class Customer extends Person{
    Customer(String name, int age, String contact){
        super(name, age, contact);
    }
    Customer(Employee e){
        super(e.name, e.age, e.contact);
    }   
}
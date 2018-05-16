package serviceStation;
class Employee extends Person{
    int emp_id;
    Employee(String name, int age, String contact, int empId){
        super(name, age, contact);
        this.emp_id = empId;
    }
    String getEmployee(){
        return(this.name + "(" + this.emp_id + ")");
    }
}
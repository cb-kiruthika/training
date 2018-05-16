
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

class Employee {

    String name;
    Integer salary;
    Integer age;

    Employee(String name, int salary, int age) {
        this.name = name;
        this.age = age;
        this.salary = salary;
    }

    public static void display(List<Employee> employees) {
        System.out.println();
        System.out.println("Employee Detail");
        for (Employee e : employees) {
            System.out.println(e.name + " (" + e.age + ") - " + e.salary);
        }
    }

    public static void main(String[] args) {
        List<Employee> employees = new LinkedList<>();
        employees.add(new Employee("abcd", 25000, 26));
        employees.add(new Employee("trew", 55000, 56));
        employees.add(new Employee("dret", 105000, 36));
        employees.add(new Employee("kwt", 15000, 22));

        Scanner in = new Scanner(System.in);
        System.out.println("Sort by: name/age/salary");
        String srt = in.next();

        if (srt.equals("name")) {
            employees.sort(new Comparator<Employee>() {
                @Override
                public int compare(Employee e1, Employee e2) {
                    return e1.name.compareTo(e2.name);
                }
            });
        } else if (srt.equals("age")) {
            employees.sort(new Comparator<Employee>() {
                @Override
                public int compare(Employee e1, Employee e2) {
                    return e1.age - e2.age;
                }
            });
        } else {
            employees.sort(new Comparator<Employee>() {
                @Override
                public int compare(Employee e1, Employee e2) {
                    return e1.salary - e2.salary;
                }
            });
        }

        Employee.display(employees);

    }
}

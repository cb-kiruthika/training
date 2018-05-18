package PhoneDirectory;

public class Person {

    String name;
    String address;
    String mobile;
    String home;
    String work;

    Person(String[] person) {
        this.name = person[0];
        this.address = person[1];
        this.mobile = person[2];
        if (person.length > 3) {
            this.home = person[3];
            if (person.length > 4) {
                this.work = person[4];
            }
        }
    }

    public void displayPerson() {
        
        System.out.println();
        System.out.println("Name:    " + this.name);
        System.out.println("Address: " + this.address);
        System.out.println("Mobile:  " + this.mobile);
        if (this.home != null) {
            System.out.println("Home:    " + this.home);
            if (this.work != null) {
                System.out.println("Work:    "+this.work);
            }
        }
        
        System.out.println();
    }

}

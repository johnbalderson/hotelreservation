import model.Customer;

public class Tester {

    public static void main(String[] args) {
        Customer customer1 = new Customer("John", "Balderson", "xplatdev@gmail.com");
        System.out.println(customer1);
        Customer customer2 = new Customer("first", "last", "email");
        System.out.println(customer2);
    }
}

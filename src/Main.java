import java.io.IOException;

public class Main {

    public static void main (String[] args) throws IOException {

        Repository rep = new Repository();
        System.out.println(rep.getCustomers().stream().map(Customer::getFirstName).toList());

    }
}

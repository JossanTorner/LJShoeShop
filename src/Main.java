import java.io.IOException;
import java.util.Scanner;

public class Main {

    LJShoeShop shop;
    Customer loggedIn;

    public Main() throws IOException {
        shop = new LJShoeShop();
    }

    public String userPrompt(String prompt){
        Scanner in = new Scanner(System.in);
        System.out.println(prompt);
        System.out.print("> ");
        return in.nextLine();
    }

    public static void main (String[] args) throws IOException {

//        Repository rep = new Repository();
//        System.out.println(rep.getCustomers().stream().map(Customer::getFirstName).toList());

        Main main = new Main();


    }
}

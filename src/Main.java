import Customer.Customer;
//import ShoeShop.LJShoeShop;
////import ShoeShop.LJShoeShop;

import java.io.IOException;

public class Main {


        public static void main (String[]args) throws IOException {

            Repository.Repository rep = new Repository.Repository();
            rep.validateLogIn();
            rep.getProducts();
            // Main main = new Main();

        }
    }


// LJShoeShop shop;
//Customer loggedIn;
//
//    public Main() throws IOException {
////        LJShoeShop shop = new LJShoeShop();
////    }
//
////    public String userPrompt(String prompt){
////        Scanner in = new Scanner(System.in);
////        System.out.println(prompt);
////        System.out.print("> ");
////        return in.nextLine();
////    }


package ShoeShop;

import Customer.Customer;
import Repository.Repository;

import java.io.IOException;
import java.util.Scanner;

public class ConsoleApp {

    Scanner scanner = new Scanner(System.in);
    boolean running = true;
    UserState currentState = UserState.LOGIN;
    Repository repository;
    Customer loggedInCustomer = null;

    public ConsoleApp() throws IOException {
        repository = new Repository();
    }

    public void run(){
        while(running){
            switch(currentState){
                case LOGIN -> logInPrompt();
                case MAIN_MENU -> menuPrompt();
            }
        }
    }

    public void logInPrompt(){
        System.out.println("LOG IN");
        System.out.println("Username: ");
        String username = scanner.nextLine();
        System.out.println("Password: ");
        String password = scanner.nextLine();
        validateLogIn(username, password);
    }

    public void menuPrompt(){
        System.out.println("WELCOME TO LJ SHOES!");
        System.out.println(
                "[1] Shop" +
                "\n[2] Order history" +
                "\n[3] Shopping cart" +
                "\n[4] Log out" +
                "\n[5] Exit");
        System.out.println("Menu choice: ");
        handleMenuInput(scanner.nextLine());
    }

    public void handleMenuInput(String input){
        switch(input){
            case "1" -> {
                categoryPrompt();
            }
            case "2" ->{
                //visa order history
            }
            case "3" ->{
                //visa shopping cart
            }
            case "4" -> {
                loggedInCustomer = null;
                currentState = UserState.LOGIN;
            }
            case "5" -> {
                running = false;
            }
        }
    }

    public void categoryPrompt(){
        System.out.println("CATEGORIES");
        System.out.println(
                "[1] Sneakers" +
                "\n[2] Boots" +
                "\n[3] Sandals" +
                "\n[4] Slip-in's" +
                "\n[5] Heels" +
                "\n[6] Flip flops" +
                "\n[7] Back to menu");
        System.out.println("Menu choice: ");
        handleCategoryInput(scanner.nextLine());
    }


    public void handleCategoryInput(String input){
        switch(input){
            case "1" -> {
                //visa sneakers
            }
            case "2" -> {
                //visa boots
            }
            case "3" ->{
                //visa sandaler
            }
            case "4" -> {
                //visa slip ins
            }
            case "5" -> {
                //visa klackskor
            }
            case "6" ->{
                //visa flip flops
            }
            case "7" -> {
                currentState = UserState.MAIN_MENU;
            }
            default -> {
                System.out.println("Invalid input");
            }
        }
    }

    public void validateLogIn(String username, String password){
        loggedInCustomer = repository.login(username, password);
        if (loggedInCustomer == null){
            System.out.println("Invalid username or password");
        }
        else{
            currentState = UserState.MAIN_MENU;
        }
    }

    public void orderHistory(){
        System.out.println(loggedInCustomer.getFirstName() + " " + loggedInCustomer.getLastName() + " ORDER HISTORY: ");
        // här ska alla orders listas upp, man ska kunna välja order för att titta närmre på vilka varor som var i den
    }

    public void shoppingCart(){
        System.out.println(loggedInCustomer.getFirstName() + " " + loggedInCustomer.getLastName() + " SHOPPING CART");
        // här ska alla varor som nuvarande ligger i kundvagnen visas, man ska kunna välja vara för att ta bort eller lägga till mer av den?
    }

    public static void main(String[] args) throws IOException {
        ConsoleApp app = new ConsoleApp();
        app.run();
    }



}

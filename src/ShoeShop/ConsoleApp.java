package ShoeShop;

import Customer.Customer;
import Repository.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleApp {

    List<Category> LJcategories;
    List<Product> LJProducts;
    Scanner input = new Scanner(System.in);
    boolean running = true;
    UserState currentState = UserState.LOGIN;
    Repository repository;
    Customer loggedInCustomer = null;

    public ConsoleApp() throws IOException {
        repository = new Repository();
        LJcategories = repository.getCategories();
        System.out.println(LJcategories.stream().map(Category::getCategoryName).toList());
        LJProducts = repository.getProducts(LJcategories);
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
        String username = input.nextLine();
        System.out.println("Password: ");
        String password = input.nextLine();
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
        handleMenuInput(input.nextLine());
    }

    public void handleMenuInput(String input){
        switch(input){
            case "1" -> {
                categoriesPrompt();
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

    public void categoriesPrompt() {
        Category chosenCategory = null;
        int categoryChoice;

        System.out.println("Choose a category: ");
        int count = 1;
        for(int i = 0; i < LJcategories.size(); i++){
            System.out.println(count++ + " -> " + LJcategories.get(i).getCategoryName());
        }

        try{
            categoryChoice = Integer.parseInt(input.nextLine());
        }
        catch (Exception e){
            return;
        }

        for(int i = 0; i < LJcategories.size(); i++){
            if (categoryChoice > 0 && categoryChoice <= LJcategories.size()) {
                chosenCategory = LJcategories.get(categoryChoice - 1);
                break;
            }
        }
        handleCategoryChoice(chosenCategory);
    }

    public void handleCategoryChoice(Category chosenCategory){
        boolean found = false;
        Product selectedProduct = null;
        for(Category category:LJcategories){
            if(category.getCategoryName().equals(chosenCategory.getCategoryName())){
                for (int i = 0; i < category.getProductsInCategory().size(); i++) {
                    Product product = category.getProductsInCategory().get(i);
                    System.out.println((i + 1) + " -> " + product.getProductName() +
                            "\n" + product.getSpec().getBrand() +
                            "\n" + product.getSpec().getColor() +
                            "\n" + product.getSpec().getSize() +
                            "\n" + product.getSpec().getPrice());
                }
            }
        }
    }


    public void handleCategoryInput(String userInput) {
        boolean found = false;
        Product selectedProduct = null;

        System.out.println("Products: ");
        for (Category category : LJcategories) {
            if (category.getCategoryName().equals(userInput)) {
                for (int i = 0; i < category.getProductsInCategory().size(); i++) {
                    Product product = category.getProductsInCategory().get(i);
                    System.out.println((i + 1) + " -> " + product.getProductName() +
                            "\n" + product.getSpec().getBrand() +
                            "\n" + product.getSpec().getColor() +
                            "\n" + product.getSpec().getSize() +
                            "\n" + product.getSpec().getPrice());
                }
                //System.out.println(category.getProductsInCategory().stream().map(Product::getProductName).toList());
                found = true;
            }
            if (!found) {
                System.out.println("Invalid input");
                return;
            }
        }
        System.out.println("Make your choice...");

        int choice;
        try {
            choice = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice. Please enter a number.");
            return;
        }

        // Find the selected product
        for (Category category : LJcategories) {
            if (category.getCategoryName().equals(input)) {
                if (choice > 0 && choice <= category.getProductsInCategory().size()) {
                    selectedProduct = category.getProductsInCategory().get(choice - 1);
                } else {
                    System.out.println("Invalid selection.");
                    return;
                }
            }
        }

//        if (selectedProduct != null) {
//            addProductToCart(selectedProduct);
//        }
    }

    public void addProductToCart(String input){

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
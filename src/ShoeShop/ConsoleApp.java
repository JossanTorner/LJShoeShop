package ShoeShop;

import Customer.Customer;
import Repository.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class ConsoleApp {

    Repository repository;
    List<Category> LJcategories;
    List<Product> LJProducts;
    List<OutOfStockItem> outOfStock;
    Scanner input = new Scanner(System.in);

    boolean running = true;
    UserState currentState = UserState.LOGIN;
    Customer loggedInCustomer = null;

    public ConsoleApp() throws IOException {
        repository = new Repository();
        updateStore();
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
        System.out.print("Username:\n-> ");
        String username = input.nextLine();
        System.out.print("Password:\n-> ");
        String password = input.nextLine();
        validateLogIn(username, password);
    }

    public void menuPrompt(){
        System.out.println("WELCOME TO LJ SHOES!");
        System.out.println(
                "[1] Shop" +
                "\n[2] Order history" +
                "\n[3] Shopping cart" +
                "\n[4] Log out");
        System.out.print("-> ");
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
                showShoppingCart();
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
            System.out.println("[" + (count++) + "] " + LJcategories.get(i).getCategoryName());
        }

        System.out.print("-> ");
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
        Product selectedProduct = null;
        int choice;
        if(!chosenCategory.getProductsInCategory().isEmpty()){
            for(int i = 0; i < chosenCategory.getProductsInCategory().size(); i++){
                Product product = chosenCategory.getProductsInCategory().get(i);
                System.out.println("[" + (i + 1) + "] " + product.getProductName() +
                        "\nBrand: " + product.getSpec().getBrand() +
                        "\nColor: " + product.getSpec().getColor() +
                        "\nSize: " + product.getSpec().getSize() +
                        "\nPrice: " + product.getSpec().getPrice());
                System.out.println();
            }

            System.out.print("-> ");
            try{
                choice = Integer.parseInt(input.nextLine());
            }
            catch (NumberFormatException e) {
                throw new RuntimeException(e);
            }

            for(int i = 0; i<chosenCategory.getProductsInCategory().size(); i++){
                if (choice > 0 && choice <= chosenCategory.getProductsInCategory().size()) {
                    selectedProduct = chosenCategory.getProductsInCategory().get(choice-1);
                    System.out.println("Selected product: " + selectedProduct.getProductName());
                    break;
                }
            }

            if (selectedProduct != null) {
                addProductToCart(selectedProduct);
            }
        }
        else{
            System.out.println("No products found");
        }
    }

    public void showShoppingCart(){
        loggedInCustomer.setShoppingCart(repository.getShoppingCart(loggedInCustomer));
        loggedInCustomer.getShoppingCart().setItemsInCart(repository.loadShoppingCart(loggedInCustomer.getShoppingCart(), LJProducts));
        System.out.println("--YOUR SHOPPING CART--");
        if (loggedInCustomer.getShoppingCart().getItemsInCart().isEmpty()){
            System.out.println("Your shopping cart is empty!");
        }
        else{
            for(CartItem item: loggedInCustomer.getShoppingCart().getItemsInCart()){
                System.out.println(item.getProduct().getProductName() + " - qty: " + item.getQuantity());
            }
        }
        System.out.println("[1] Make order" + "\n[2] Back to menu");
        System.out.print("-> ");

        String choice = input.nextLine();
        switch (choice) {
            case "1" -> placeOrder(); // Call method to place the order
            case "2" -> currentState = UserState.MAIN_MENU; // Return to the main menu
            default -> System.out.println("Invalid choice. Returning to menu.");
        }

    }

    public void placeOrder(){

    }

    //denna method behöver anropa SP
    public void addProductToCart(Product product){
        boolean soldOut = false;
        for(OutOfStockItem item: outOfStock){
            if(item.getProduct().getId() == product.getId()){
                System.out.println("Product is out of stock!");
                soldOut = true;
                break;
            }
        }
        if(!soldOut){
            repository.addItemToCart(loggedInCustomer, product);
        }
    }

    public void validateLogIn(String username, String password){
        loggedInCustomer = repository.login(username, password);
        System.out.println("Customer id: " + loggedInCustomer.getId());
        if (loggedInCustomer == null){
            System.out.println("Invalid username or password");
        }
        else{
            currentState = UserState.MAIN_MENU;
            loggedInCustomer.setShoppingCart(repository.getShoppingCart(loggedInCustomer));
        }
    }

    public void orderHistory(){
        System.out.println(loggedInCustomer.getFirstName() + " " + loggedInCustomer.getLastName() + " ORDER HISTORY: ");
        // här ska alla orders listas upp, man ska kunna välja order för att titta närmre på vilka varor som var i den
    }

    public void updateStore() throws IOException {
        LJcategories = repository.getCategories();
        LJProducts = repository.getProducts();
        repository.putProductsInCategories(LJcategories, LJProducts);
        outOfStock = repository.getProductsOutOfStock(LJProducts);
    }

    public static void main(String[] args) throws IOException {
        ConsoleApp app = new ConsoleApp();
        app.run();
    }
}
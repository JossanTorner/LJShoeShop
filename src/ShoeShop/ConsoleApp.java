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
        Product selectedProduct = null;
        int choice;
        for(int i = 0; i < chosenCategory.getProductsInCategory().size(); i++){
            Product product = chosenCategory.getProductsInCategory().get(i);
            System.out.println((i + 1) + " -> " + product.getProductName() +
                    "\n" + product.getSpec().getBrand() +
                    "\n" + product.getSpec().getColor() +
                    "\n" + product.getSpec().getSize() +
                    "\n" + product.getSpec().getPrice());
        }

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

    public void showShoppingCart(){
        loggedInCustomer.setShoppingCart(repository.getShoppingCart(loggedInCustomer));
        loggedInCustomer.getShoppingCart().setItemsInCart(repository.loadShoppingCart(loggedInCustomer.getShoppingCart(), LJProducts));
        for(CartItem item: loggedInCustomer.getShoppingCart().getItemsInCart()){
            System.out.println(item.getProduct().getProductName() + " - Quantity: " + item.getQuantity());
        }
    }

//    public void handleProductChosen(Product product){
//
////        boolean found = false;
////        for(CartItem item: loggedInCustomer.getShoppingCart().getItemsInCart()){
////            if (product.equals(item.getProduct())){
////
////            }
////        }
////        if(!found){
////            loggedInCustomer.getShoppingCart().addToCart(new CartItem(product));
////        }
//    }

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
        //System.out.println(LJcategories.stream().map(Category::getCategoryName).toList());
//        for(Product product : LJProducts) {
//            System.out.println(product.getProductName() + " ID: " + product.getId());
//        }
    }

    public static void main(String[] args) throws IOException {
        ConsoleApp app = new ConsoleApp();
        app.run();
    }


//    public void handleCategoryInput(String userInput) {
//        boolean found = false;
//        Product selectedProduct = null;
//
//        System.out.println("Products: ");
//        for (Category category : LJcategories) {
//            if (category.getCategoryName().equals(userInput)) {
//                for (int i = 0; i < category.getProductsInCategory().size(); i++) {
//                    Product product = category.getProductsInCategory().get(i);
//                    System.out.println((i + 1) + " -> " + product.getProductName() +
//                            "\n" + product.getSpec().getBrand() +
//                            "\n" + product.getSpec().getColor() +
//                            "\n" + product.getSpec().getSize() +
//                            "\n" + product.getSpec().getPrice());
//                }
//                //System.out.println(category.getProductsInCategory().stream().map(Product::getProductName).toList());
//                found = true;
//            }
//            if (!found) {
//                System.out.println("Invalid input");
//                return;
//            }
//        }
//        System.out.println("Make your choice...");
//
//        int choice;
//        try {
//            choice = Integer.parseInt(input.nextLine());
//        } catch (NumberFormatException e) {
//            System.out.println("Invalid choice. Please enter a number.");
//            return;
//        }
//
//        // Find the selected product
//        for (Category category : LJcategories) {
//            if (category.getCategoryName().equals(input)) {
//                if (choice > 0 && choice <= category.getProductsInCategory().size()) {
//                    selectedProduct = category.getProductsInCategory().get(choice - 1);
//                } else {
//                    System.out.println("Invalid selection.");
//                    return;
//                }
//            }
//        }
//
//        if (selectedProduct != null) {
//            addProductToCart(selectedProduct);
//        }
//    }

}
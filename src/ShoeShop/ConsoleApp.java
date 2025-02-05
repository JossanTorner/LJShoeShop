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
   // LJShoeShop shop;

    boolean running = true;
    UserState currentState = UserState.LOGIN;
    Customer loggedInCustomer = null;


    public ConsoleApp() throws IOException {
//        shop = new LJShoeShop();
//        shop.updateStore();
        repository = new Repository();
        updateStore();

    }

    public void run() {
        while (running) {
            switch (currentState) {
                case LOGIN -> logInPrompt();
                case MAIN_MENU -> menuPrompt();
            }
        }
    }

    public void updateStore() {
        try {
            LJcategories = repository.getCategories();
            LJProducts = repository.getProducts();
            repository.putProductsInCategories(LJcategories, LJProducts);
            outOfStock = repository.getProductsOutOfStock(LJProducts);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateCustomerInfo() {
        loggedInCustomer.setOrderHistory(repository.getOrderHistory(loggedInCustomer));
        repository.loadOrders(loggedInCustomer, LJProducts);
    }

    public void validateLogIn(String username, String password) {
        loggedInCustomer = repository.login(username, password);
        if (loggedInCustomer == null) {
            System.out.println("Invalid username or password");
        } else {
            currentState = UserState.MAIN_MENU;
            loggedInCustomer.setShoppingCart(repository.getShoppingCart(loggedInCustomer));
        }
    }

    public void logInPrompt() {
        System.out.println("LOG IN");
        System.out.print("Username:\n-> ");
        String username = input.nextLine();
        System.out.print("Password:\n-> ");
        String password = input.nextLine();
        validateLogIn(username, password);
    }

    public void menuPrompt() {
        System.out.println("\nWELCOME TO LJ SHOES!");
        System.out.println(
                "[1] Shop" +
                        "\n[2] Order history" +
                        "\n[3] Shopping cart" +
                        "\n[4] Log out");
        System.out.print("-> ");
        handleMainMenuInput(input.nextLine());
    }

    public void handleMainMenuInput(String input) {
        switch (input) {
            case "1" -> {
                categoriesPrompt();
            }
            case "2" -> {
                orderHistory();
            }
            case "3" -> {
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
        System.out.println("Choose a category: ");
        getCategoryNames();

        int categoryChoice = takeMenuChoice();

        Category chosenCategory = getCategoryChoice(categoryChoice);
        validateCategoryChoice(chosenCategory);
    }

    public int takeMenuChoice(){
        System.out.println("-> ");
        int choice = 0;
        try {
            choice = Integer.parseInt(input.nextLine());
        }
        catch (Exception e) {
            System.out.println("Invalid choice. Returning to main menu.");
        }
        return choice;
    }

    public void validateCategoryChoice(Category category) {
        if(category == null) {
            System.out.println("Invalid category choice. Returning to menu.");
        }
        else{
            handleCategoryChoice(category);
        }
    }

    public Category getCategoryChoice(int choice){
        for (int i = 0; i < LJcategories.size(); i++) {
            if (choice > 0 && choice <= LJcategories.size()) {
                return LJcategories.get(choice - 1);
            }
        }
        return null;
    }

    public void getCategoryNames(){
        int count = 1;
        for (Category lJcategory : LJcategories) {
            System.out.println("[" + (count++) + "] " + lJcategory.getCategoryName());
        }
    }

    public void handleCategoryChoice(Category chosenCategory) {
        if (!chosenCategory.getProductsInCategory().isEmpty()) {
            getProductsForCategory(chosenCategory);
            handleProductChoice(chosenCategory);
        } else {
            System.out.println("No products found");
        }
    }

    public void getProductsForCategory(Category chosenCategory) {
        for (int i = 0; i < chosenCategory.getProductsInCategory().size(); i++) {
            Product product = chosenCategory.getProductsInCategory().get(i);
            System.out.println("\n[" + (i + 1) + "] " + product.getProductName() +
                    "\nBrand: " + product.getSpec().getBrand() +
                    "\nColor: " + product.getSpec().getColor() +
                    "\nSize: " + product.getSpec().getSize() +
                    "\nPrice: " + product.getSpec().getPrice());
        }
    }

    public void handleProductChoice(Category chosenCategory) {
        int productChoice = takeMenuChoice();

        if (productChoice > 0 && productChoice <= chosenCategory.getProductsInCategory().size()) {
            Product selectedProduct = chosenCategory.getProductsInCategory().get(productChoice - 1);
            System.out.println("Selected product: " + selectedProduct.getProductName());
             addProductToCart(selectedProduct);
        } else {
            System.out.println("Invalid choice. Returning to menu");
        }
    }

    public void addProductToCart(Product product) {
        OutOfStockItem outOfStockItem = findOutOfStockItem(product);
        if (outOfStockItem != null) {
            handleOutOfStockItems(outOfStockItem);
        } else {
            repository.addItemToCart(loggedInCustomer, product);
        }
    }

    public OutOfStockItem findOutOfStockItem(Product product) {
        for (OutOfStockItem item : outOfStock) {
            if (item.getProduct().getId() == product.getId()) {
                return item;
            }
        }
        return null;
    }

    public void handleOutOfStockItems(OutOfStockItem item) {
        System.out.println("Product is out of stock since " + item.getOutOfStockSince());
    }

    public void showShoppingCart() {
        System.out.println("--YOUR SHOPPING CART--");
        loadShoppingCartItems();
        if (loggedInCustomer.getShoppingCart().getItemsInCart().isEmpty()) {
            System.out.println("Your shopping cart is empty!");
        } else {
            showCartItems();
        }
    }


    public void loadShoppingCartItems() {
        updateStore();
        loggedInCustomer.setShoppingCart(repository.getShoppingCart(loggedInCustomer));
        loggedInCustomer.getShoppingCart().setItemsInCart(repository.loadShoppingCart(loggedInCustomer.getShoppingCart(), LJProducts));
    }


     public void showCartItems(){
         for (Item item : loggedInCustomer.getShoppingCart().getItemsInCart()) {
             System.out.println(item.getProduct().getProductName() + " - qty: " + item.getQuantity());
         }
         shoppingCartMenu();
     }


    public void shoppingCartMenu(){
        System.out.println("[1] Make order" + "\n[2] Clear shopping cart" + "\n[3] Back to menu");
        System.out.print("-> ");
        handleShoppingCartInput(input.nextLine());
    }

    public void handleShoppingCartInput(String choice){
        switch (choice) {
            case "1" -> placeOrder();
            case "2" -> repository.ClearShoppingCart(loggedInCustomer);
            case "3" -> currentState = UserState.MAIN_MENU;
            default -> System.out.println("Invalid choice. Returning to menu.");
        }
    }

    public void placeOrder() {
        updateStore();
        updateCustomerInfo();
        int orderCountBefore = getOrderCount();
        processOrder();
        if (hasOrderSucceeded(orderCountBefore)) {
            System.out.println("Order successful!");
        }
        else {
            handleFailedOrder();
        }
    }

    private void processOrder(){
        repository.placeOrder(loggedInCustomer.getShoppingCart(), loggedInCustomer);
        updateCustomerInfo();
    }

    private int getOrderCount(){
        return loggedInCustomer.getOrderHistory().size();
    }


    private boolean hasOrderSucceeded(int ordersBefore) {
        return getOrderCount() > ordersBefore;
    }

    private void handleFailedOrder(){
        for(Item item : loggedInCustomer.getShoppingCart().getItemsInCart()) {
            checkAvailability(item);
        }
    }

    public void checkAvailability(Item item){
        Product product = findProductById(item.getProduct().getId());
        if(product != null && product.getStockQuantity() < item.getQuantity()) {
            System.out.println("Adjust quantity for following product: " + item.getProduct().getProductName());
        }
    }

    public Product findProductById(int id){
        for(Product product : LJProducts){
            if (product.getId() == id){
                return product;
            }
        }
        return null;
    }

    public void orderHistory() {
        System.out.println(this.loggedInCustomer.getFirstName() + " " + this.loggedInCustomer.getLastName() + " ORDER HISTORY: ");
        updateCustomerInfo();
        for (Order order : loggedInCustomer.getOrderHistory()) {
            System.out.println("\nOrdernumber: " + " " + order.getCustomerOrderID() + " " + "Ordered: " + " " + order.getOrderDate());
            OrderedDetails(order);
        }
        if (loggedInCustomer.getOrderHistory() == null || loggedInCustomer.getOrderHistory().isEmpty()) {
            System.out.println("No order history found for this customer.");
        }
    }

    public void OrderedDetails(Order order) {
        for(Item item : order.getProducts()){
            System.out.println("Product: " + item.getProduct().getProductName() + " qty: " + item.getQuantity());
        }
    }
}
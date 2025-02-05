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
        handleMenuInput(input.nextLine());
    }

    public void handleMenuInput(String input) {
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
        Category chosenCategory = null;
        int categoryChoice;

        System.out.println("Choose a category: ");
        int count = 1;
        for (int i = 0; i < LJcategories.size(); i++) {
            System.out.println("[" + (count++) + "] " + LJcategories.get(i).getCategoryName());
        }

        System.out.print("-> ");
        try {
            categoryChoice = Integer.parseInt(input.nextLine());
        } catch (Exception e) {
            return;
        }

        for (int i = 0; i < LJcategories.size(); i++) {
            if (categoryChoice > 0 && categoryChoice <= LJcategories.size()) {
                chosenCategory = LJcategories.get(categoryChoice - 1);
                break;
            }
        }
        handleCategoryChoice(chosenCategory);
    }

    public void handleCategoryChoice(Category chosenCategory) {
        if (!chosenCategory.getProductsInCategory().isEmpty()) {
            for (int i = 0; i < chosenCategory.getProductsInCategory().size(); i++) {
                Product product = chosenCategory.getProductsInCategory().get(i);
                System.out.println("[" + (i + 1) + "] " + product.getProductName() +
                        "\nBrand: " + product.getSpec().getBrand() +
                        "\nColor: " + product.getSpec().getColor() +
                        "\nSize: " + product.getSpec().getSize() +
                        "\nPrice: " + product.getSpec().getPrice());
                System.out.println();
            }
            handleProductChoice(chosenCategory);

        } else {
            System.out.println("No products found");
        }
    }

    public void handleProductChoice(Category chosenCategory) {
        int choice;

        System.out.print("-> ");
        try {
            choice = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid product choice");
            return;
        }

        if (choice > 0 && choice <= chosenCategory.getProductsInCategory().size()) {
            Product selectedProduct = chosenCategory.getProductsInCategory().get(choice - 1);
            System.out.println("Selected product: " + selectedProduct.getProductName());
            addProductToCart(selectedProduct);
        } else {
            System.out.println("Invalid choice. Returning to menu");
        }
    }


    public void addProductToCart(Product product) {
        boolean soldOut = false;
        for (OutOfStockItem item : outOfStock) {
            if (item.getProduct().getId() == product.getId()) {
                System.out.println("Product is out of stock since " + item.getOutOfStockSince());
                soldOut = true;
                break;
            }
        }
        if (!soldOut) {
            repository.addItemToCart(loggedInCustomer, product);
        }
    }

    public void showShoppingCart() {
        updateStore();
        loggedInCustomer.setShoppingCart(repository.getShoppingCart(loggedInCustomer));
        loggedInCustomer.getShoppingCart().setItemsInCart(repository.loadShoppingCart(loggedInCustomer.getShoppingCart(), LJProducts));
        System.out.println("--YOUR SHOPPING CART--");
        if (loggedInCustomer.getShoppingCart().getItemsInCart().isEmpty()) {
            System.out.println("Your shopping cart is empty!");
        } else {
            for (Item item : loggedInCustomer.getShoppingCart().getItemsInCart()) {
                System.out.println(item.getProduct().getProductName() + " - qty: " + item.getQuantity());
            }
        }
        System.out.println("[1] Make order" + "\n[2] Clear shopping cart" + "\n[3] Back to menu");
        System.out.print("-> ");

        String choice = input.nextLine();
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
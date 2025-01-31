package Repository;

import Customer.Customer;
import Customer.LoginDetails;
import ShoeShop.*;
import ShoeShop.Product;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Repository {

    private Properties properties = new Properties();

    public Repository() throws IOException {
        properties.load(new FileInputStream("src/settings_logIn.properties"));
    }

    //hämtar customer som loggar in från databasen
    public Customer login(String username, String password) {
        String query = "SELECT Customer.id, Customer.firstName, Customer.lastName, Customer.sscr, " +
                "LoginDetails.username, LoginDetails.userPassword " +
                "FROM Customer " +
                "INNER JOIN LoginDetails ON LoginDetails.customerId = Customer.id " +
                "WHERE LoginDetails.username = ? AND LoginDetails.userPassword = ?";

        try (Connection connection = DriverManager.getConnection(
                properties.getProperty("connectionString"),
                properties.getProperty("username"),
                properties.getProperty("password"))) {

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                long sscr = resultSet.getLong("sscr");
                String userUsername = resultSet.getString("username");
                String userPassword = resultSet.getString("userPassword");
                return new Customer(id, firstName, lastName, sscr, new LoginDetails(userUsername, userPassword));

            }
            return null;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid connection to database while logging in", e);
        }
    }

    //laddar programmet med kategorier från databasen
    public List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();

        String query = "SELECT * FROM Category";

        try (Connection connection = DriverManager.getConnection(
                properties.getProperty("connectionString"),
                properties.getProperty("username"),
                properties.getProperty("password"))) {

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String categoryName = resultSet.getString("categoryName");
                Category category = new Category(categoryName, id);
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid connection to database while collection categories", e);
        }
        return categories;
    }


    //returnerar en lista med alla produkter + lägger in dem i tillhörande kategorier
    public List<Product> getProducts(List<Category> categories) {
        List<Product> products = new ArrayList<>();

        String query = "SELECT Product.id as productId, Product.productName, Specification.price, Specification.shoeSize, Specification.color, Specification.brand, Category.id as categoryId" +
                " from Product" +
                " inner join Specification on Specification.id = Product.specId" +
                " inner join ProductInCategory on ProductInCategory.productId = Product.id" +
                " inner join Category on Category.id = ProductInCategory.categoryId" +
                " where Category.id = ProductInCategory.categoryId";

        try (Connection connection = DriverManager.getConnection(
                properties.getProperty("connectionString"),
                properties.getProperty("username"),
                properties.getProperty("password"))) {

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int productId = resultSet.getInt("productId");
                String productName = resultSet.getString("productName");
                double price = resultSet.getDouble("price");
                int shoeSize = resultSet.getInt("shoeSize");
                String color = resultSet.getString("color");
                String brand = resultSet.getString("brand");
                int categoryId = resultSet.getInt("categoryId");
                Product product = new Product(productId, productName, new Specification(price, shoeSize, color, brand));
                products.add(product);

                for (Category category : categories) {
                    if (category.getCategoryID() == categoryId) {
                        category.addProductToCategory(product);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Databasfel vid inläsning av produkter", e);
        }
        return products;
    }

    //laddar en shoppingcart med produkter som ligger i den i databasen
    public List<CartItem> loadShoppingCart(ShoppingCart cart, List<Product> products) {
        List<CartItem> items = new ArrayList<>();
        String query = "SELECT CartItem.productId, cartItem.quantity FROM CartItem where CartItem.cartId = ?";

        try (Connection connection = DriverManager.getConnection(
                properties.getProperty("connectionString"),
                properties.getProperty("username"),
                properties.getProperty("password"))) {

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, cart.getId());

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int productId = resultSet.getInt("productId");
                int quantity = resultSet.getInt("quantity");

                for (Product product : products) {
                    if (product.getId() == productId) {
                        CartItem item = new CartItem(product);
                        item.setQuantity(quantity);
                        items.add(item);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return items;
    }

    //hämtar shoppingcart tillhörande customer
    public ShoppingCart getShoppingCart(Customer customer) {
        ShoppingCart shoppingCart = new ShoppingCart();
        String query = "SELECT * FROM ShoppingCart WHERE ShoppingCart.customerId = ?";

        try (Connection connection = DriverManager.getConnection(
                properties.getProperty("connectionString"),
                properties.getProperty("username"),
                properties.getProperty("password"))) {

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, customer.getId());

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int shoppingCartId = resultSet.getInt("id");
                shoppingCart.setId(shoppingCartId);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return shoppingCart;
    }

    public List<Order> getOrderHistory(Customer customer) {
        List<Order> orderHistory = new ArrayList<>();
        String query = "SELECT CustomerOrder.id, CustomerOrder.dateOfOrder, CustomerOrder.customerId " +
                "FROM CustomerOrder " +
                "INNER JOIN Customer on Customer.id = CustomerOrder.customerId WHERE Customer.id = ? ";

        try (Connection connection = DriverManager.getConnection(
                properties.getProperty("connectionString"),
                properties.getProperty("username"),
                properties.getProperty("password"))) {

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, customer.getId());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int orderID = resultSet.getInt("id");
                String dateOfOrder = resultSet.getString("dateOfOrder");
                Order fetchedOrder = new Order (orderID, dateOfOrder);
                orderHistory.add(fetchedOrder);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error while getting order history");
        }

        if (orderHistory.isEmpty()) {
            System.out.println("No orders found.");
        }

        return orderHistory;
    }


    public void getOrderDetails() {

    }

}


//    public void validateLogIn() {
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Användarnamn: ");
//        String username = scanner.nextLine();
//        System.out.print("Lösenord: ");
//        String password = scanner.nextLine();
//        Customer loggedInCustomer = login(username, password);
//
//        if (loggedInCustomer != null) {
//            System.out.println("Successful login!" + " Welcome " +
//                    loggedInCustomer.getFirstName() + " " +
//                    loggedInCustomer.getLastName());
//
//        } else {
//            System.out.println("Invalid username or password");
//        }
//    }

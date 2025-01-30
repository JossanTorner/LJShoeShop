package Repository;

import Customer.Customer;
import Customer.LoginDetails;
import ShoeShop.Product;
import ShoeShop.Specification;
import ShoeShop.Category;
import ShoeShop.Product;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class Repository {

    private Properties properties = new Properties();

    public Repository() throws IOException {

        properties.load(new FileInputStream("src/settings_logIn.properties"));
    }

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

    public void validateLogIn() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Användarnamn: ");
        String username = scanner.nextLine();
        System.out.print("Lösenord: ");
        String password = scanner.nextLine();
        Customer loggedInCustomer = login(username, password);

        if (loggedInCustomer != null) {
            System.out.println("Successful login!" + " Welcome " +
                    loggedInCustomer.getFirstName() + " " +
                    loggedInCustomer.getLastName());

        } else {
            System.out.println("Invalid username or password");
        }
    }

    public List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM Category ";

        try (Connection connection = DriverManager.getConnection(
                properties.getProperty("connectionString"),
                properties.getProperty("username"),
                properties.getProperty("password"))) {

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String categoryname = resultSet.getString("categoryName");
                Category category = new Category(categoryname, id);
                categories.add(category);
            }
            CreateCategory(categories);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid connection to database while collection categories", e);
        }

        return categories;
    }

    public void CreateCategory(List<Category> categories) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose a category: ");
        for(Category category : categories) {
            System.out.println(category.getCategoryID() + " " + category.getCategoryName());
        }
        int input = scanner.nextInt();
        for (Category categoryFound : categories) {
            if (categoryFound.equals(input)) {
                System.out.println("Category" + categoryFound.getCategoryName());
            }
        }
    }

    public List<Product> getProducts(){
        List<Product> products = new ArrayList<>();

        String query = "SELECT Product.id, Product.productName, Specification.price, Specification.shoeSize, Specification.color, Specification.brand " +
                "from Product " +
                "inner join " +
                "Specification on Specification.id = Product.specId";

        try (Connection connection = DriverManager.getConnection(
                properties.getProperty("connectionString"),
                properties.getProperty("username"),
                properties.getProperty("password"))) {

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String productName = resultSet.getString("productName");
                double price = resultSet.getDouble("price");
                int shoeSize = resultSet.getInt("shoeSize");
                String color = resultSet.getString("color");
                String brand = resultSet.getString("brand");
                products.add(new Product(id, productName, new Specification(price, shoeSize, color, brand)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Databasfel vid inloggning", e);
        }
        return products;
    }
}



//    public List<Customer> getCustomers() {
//
//        String query = "SELECT Customer.id, Customer.firstName, Customer.lastName, Customer.sscr, LoginDetails.username, LoginDetails.userPassword " +
//                "FROM Customer " +
//                "inner join LoginDetails " +
//                "on LoginDetails.customerId = Customer.id";
//        List<Customer> customers = new ArrayList<Customer>();
//
//        try (Connection connection = DriverManager.getConnection(properties.getProperty("connectionString"), properties.getProperty("username"), properties.getProperty("password"))) {
//            Statement statement = connection.createStatement();
//            ResultSet resultSet = statement.executeQuery(query);
//            while (resultSet.next()) {
//                int id = resultSet.getInt("id");
//                String firstName = resultSet.getString("firstName");
//                String lastName = resultSet.getString("lastName");
//                long sscr = resultSet.getLong("sscr");
//                String username = resultSet.getString("username");
//                String password = resultSet.getString("userPassword");
//                Customer customer = new Customer(id, firstName, lastName, sscr, new LoginDetails(username, password));
//                customers.add(customer);
//            }
//
//            statement.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return customers;
//    }




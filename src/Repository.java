import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

    public List<Customer> getCustomers() {

        String query = "SELECT Customer.id, Customer.firstName, Customer.lastName, Customer.sscr, LoginDetails.username, LoginDetails.userPassword " +
                "FROM Customer " +
                "inner join LoginDetails " +
                "on LoginDetails.customerId = Customer.id";
        List<Customer> customers = new ArrayList<Customer>();

        try (Connection connection = DriverManager.getConnection(properties.getProperty("connectionString"), properties.getProperty("username"), properties.getProperty("password"))) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                long sscr = resultSet.getLong("sscr");
                String username = resultSet.getString("username");
                String password = resultSet.getString("userPassword");
                Customer customer = new Customer(id, firstName, lastName, sscr, new LoginDetails(username, password));
                customers.add(customer);
            }

            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

}



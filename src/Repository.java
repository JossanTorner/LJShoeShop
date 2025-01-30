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

        String query = "SELECT id, firstName, lastName, sscr FROM Customer";
        List<Customer> customers = new ArrayList<Customer>();

        try (Connection connection = DriverManager.getConnection(properties.getProperty("connectionString"), properties.getProperty("username"), properties.getProperty("password"))) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                long sscr = resultSet.getLong("sscr");
                Customer customer = new Customer(id, firstName, lastName, sscr);
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



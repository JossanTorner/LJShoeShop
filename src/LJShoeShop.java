import java.io.IOException;
import java.util.List;

public class LJShoeShop {

    Repository repository;
    List<Customer> customers;

    // ladda LJShoeShop med customers, produkter, osv, via repository
    public LJShoeShop() throws IOException {
        repository = new Repository();
        customers = repository.getCustomers();
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public Customer findCustomer(String username, String password) {
        for(Customer customer : customers) {
            if (username.equals(customer.getLoginDetails().getUsername()) && password.equals(customer.getLoginDetails().getPassword())){
                return customer;
            }
        }
        return null;
    }
}

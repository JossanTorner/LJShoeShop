package Customer;

import ShoeShop.Order;
import ShoeShop.ShoppingCart;

public class Customer {

    private int id;
    private String firstName;
    private String lastName;
    private long sscr;
    LoginDetails loginDetails;
    ShoppingCart shoppingCart;
    Order orderHistory;

    public Customer(int id, String firstName, String lastName, long sscr, LoginDetails loginDetails) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.sscr = sscr;
        this.loginDetails = loginDetails;
        shoppingCart = new ShoppingCart();
        orderHistory = new Order();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getSscr() {
        return sscr;
    }

    public void setSscr(long sscr) {
        this.sscr = sscr;
    }

    public LoginDetails getLoginDetails() {
        return loginDetails;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public Order getOrderHistory() { return orderHistory;}

}

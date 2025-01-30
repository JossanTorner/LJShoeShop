package ShoeShop;

import Customer.Customer;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {

    int id;
    Customer customer;
    List<CartItem> cartItemList;

    public ShoppingCart(int id, Customer customer) {
        this.id = id;
        this.customer = customer;
        this.cartItemList = new ArrayList<CartItem>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void addToCart(CartItem cartItem) {
        this.cartItemList.add(cartItem);
    }

    public void removeFromCart(CartItem cartItem) {
        this.cartItemList.remove(cartItem);
    }
}

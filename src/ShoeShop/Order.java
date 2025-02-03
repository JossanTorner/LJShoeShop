package ShoeShop;

import Customer.Customer;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private int customerOrderID;
    private String orderDate;
    private List <Item> products;

    public Order(){}

    public Order(int customerOrderID, String dateOfOrder) {
        this.customerOrderID = customerOrderID;
        this.orderDate = dateOfOrder;
        products = new ArrayList<>();
    }

    public int getCustomerOrderID() {
        return customerOrderID;
    }

    public void setCustomerOrderID(int customerOrderID) {
        this.customerOrderID = customerOrderID;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public List<Item> getProducts() { return products; }

    public void addItemToOrder(Item item){
        products.add(item);
    }

}

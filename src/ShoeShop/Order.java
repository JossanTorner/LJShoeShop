package ShoeShop;

import Customer.Customer;

public class Order {
    private int customerOrderID;
    private String orderDate;


    public Order(){}

    public Order(int orderID, String dateOfOrder) {
        this.customerOrderID = customerOrderID;
        this.orderDate = dateOfOrder;

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
}

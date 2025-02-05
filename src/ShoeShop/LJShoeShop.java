//package ShoeShop;
//
//import Customer.Customer;
//import Repository.Repository;
//
//import java.io.IOException;
//import java.util.List;
//
//public class LJShoeShop {
//    List<Category> LJcategories;
//    List<Product> LJProducts;
//    Repository repository;
//    List<OutOfStockItem> outOfStock;
//    Customer loggedInCustomer;
//
//    public LJShoeShop() throws IOException {
//        this.repository = new Repository();
//    }
//
//    public void updateStore() {
//        try {
//            LJcategories = repository.getCategories();
//            LJProducts = repository.getProducts();
//            repository.putProductsInCategories(LJcategories, LJProducts);
//            outOfStock = repository.getProductsOutOfStock(LJProducts);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void updateCustomerInfo(Customer customer) {
//        customer.setOrderHistory(repository.getOrderHistory(loggedInCustomer));
//        repository.loadOrders(customer, LJProducts);
//    }
//
//}

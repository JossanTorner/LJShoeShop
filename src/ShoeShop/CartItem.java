package ShoeShop;

public class CartItem {

    Product product;
    int quantity = 1;

    public CartItem(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void addToQuantity() {
        quantity++;
    }
}

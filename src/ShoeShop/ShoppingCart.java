package ShoeShop;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {

    int id;
    List<CartItem> cartItemList;

    public ShoppingCart() {
        this.cartItemList = new ArrayList<CartItem>();
    }

    public void addToCart(CartItem cartItem) {
        this.cartItemList.add(cartItem);
    }

    public void removeFromCart(CartItem cartItem) {
        this.cartItemList.remove(cartItem);
    }

    public List<CartItem> getItemsInCart() {
        return cartItemList;
    }

    public void setItemsInCart(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}

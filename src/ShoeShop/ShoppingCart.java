package ShoeShop;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {

    int id;
    List<Item> cartItemList;

    public ShoppingCart() {
        this.cartItemList = new ArrayList<Item>();
    }

    public void addToCart(Item cartItem) {
        this.cartItemList.add(cartItem);
    }

    public void removeFromCart(Item cartItem) {
        this.cartItemList.remove(cartItem);
    }

    public List<Item> getItemsInCart() {
        return cartItemList;
    }

    public void setItemsInCart(List<Item> cartItemList) {
        this.cartItemList = cartItemList;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}

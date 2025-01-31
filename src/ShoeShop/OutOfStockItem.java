package ShoeShop;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class OutOfStockItem {

    Product product;
    private Date outOfStockSince;

    public OutOfStockItem(Product product, Date outOfStockSince) {
        this.product = product;
        this.outOfStockSince = outOfStockSince;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Date getOutOfStockSince() {
        return outOfStockSince;
    }

    public void setOutOfStockSince(Date outOfStockSince) {
        this.outOfStockSince = outOfStockSince;
    }
}

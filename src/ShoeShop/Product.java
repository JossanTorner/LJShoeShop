package ShoeShop;

public class Product {

    private int id;
    private String productName;
    private Specification spec;
    private int stockQuantity;

    public Product(int id, String productName, int stockQuantity, Specification spec) {
        this.id = id;
        this.productName = productName;
        this.spec = spec;
        this.stockQuantity = stockQuantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Specification getSpec() {
        return spec;
    }

    public void setSpec(Specification spec) {
        this.spec = spec;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public int getStockQuantity(){
        return stockQuantity;
    }
}

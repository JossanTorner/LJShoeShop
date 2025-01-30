package ShoeShop;

public class Product {

    private int id;
    private String productName;
    private Specification spec;

    public Product(int id, String productName, Specification spec) {
        this.id = id;
        this.productName = productName;
        this.spec = spec;
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
}

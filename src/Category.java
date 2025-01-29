import java.util.List;

public class Category {

    private String categoryName;
    private int categoryID;
    private List<Product> productsInCategory;

    public Category(String categoryName, int categoryID) {
        this.categoryName = categoryName;
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public List<Product> getProductsInCategory() {
        return productsInCategory;
    }

    public void setProductsInCategory(List<Product> productsInCategory) {
        this.productsInCategory = productsInCategory;
    }
}

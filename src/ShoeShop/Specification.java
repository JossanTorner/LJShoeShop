package ShoeShop;

public class Specification {

    double price;
    int size;
    String color;
    String brand;

    public Specification(double price, int size, String color, String brand) {
        this.price = price;
        this.size = size;
        this.color = color;
        this.brand = brand;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}

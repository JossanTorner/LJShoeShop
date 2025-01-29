public class Customer {

    int id;
    String firstName;
    String lastName;
    long sscr;

    public Customer(int id, String firstName, String lastName, long sscr) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.sscr = sscr;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getSscr() {
        return sscr;
    }

    public void setSscr(long sscr) {
        this.sscr = sscr;
    }
}

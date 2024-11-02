import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class User {
    private String username;
    private ShoppingCart cart;
    private Set<Book> pastPurchases;
    private Bookstore bookstore; // Reference to the bookstore

    /**
     * User class constructor
     * @param username
     * @param bookstore
     */
    public User(String username, Bookstore bookstore) {
        this.username = username;
        this.cart = new ShoppingCart();
        this.pastPurchases = new HashSet<>();
        this.bookstore = bookstore; // Initialize bookstore reference
    }

    /**
     * User can add book to their cart
     * @param book
     * @param quantity
     */
    public void addToCart(Book book, int quantity) {
        cart.addItem(book, quantity);
    }

    /**
     * User can remove book from their cart
     * @param book
     * @param quantity
     */
    public void removeFromCart(Book book, int quantity) {
        cart.removeItem(book, quantity);
    }

    /**
     * User checks out
     * @return true if the purchase was successful, false if there was insufficient inventory
     */
    public boolean checkoutProcess() {
        Checkout checkout = new Checkout(cart);
        boolean purchaseSuccessful = checkout.processPurchase();
        if (purchaseSuccessful) {
            pastPurchases.addAll(cart.getItems().keySet());
        }
        return purchaseSuccessful;
    }

    /**
     * User can search for books by title
     * @param title
     * @return List of matching books
     */
    public List<Book> searchBooksByTitle(String title) {
        return bookstore.searchByTitle(title);
    }

    public List<Book> getRecommendations(List<User> users) {
        // Implement Jaccard distance
        return null; 
    }

    /**
     * Gets the items in the user's cart
     * @return the shopping cart associated with the user
     */
    public ShoppingCart getCart() {
        return cart;
    }
    
    /**
     * Gets the user's name
     * @return the username of the user
     */
    public String getUsername() {
        return username;
    }
}

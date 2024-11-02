import java.util.HashMap;
import java.util.Map;

public class ShoppingCart {
    private Map<Book, Integer> items;

    public ShoppingCart() {
        this.items = new HashMap<>();
    }

    /**
     * Adds the quantity of the book object the user adds to the cart
     * @param book
     * @param quantity
     */
    public void addItem(Book book, int quantity) {
        items.put(book, items.getOrDefault(book, 0) + quantity);
    }

    /**
     * Removes the quantity of the book object the user has in the cart
     * @param book
     * @param quantity
     */
    public void removeItem(Book book, int quantity) {
        if (items.containsKey(book)) {
            int currentQuantity = items.get(book);
            if (quantity >= currentQuantity) {
                items.remove(book);
            } else {
                items.put(book, currentQuantity - quantity);
            }
        }
    }

    /**
     * Gets the items that are in the cart
     * @return
     */
    public Map<Book, Integer> getItems() {
        return new HashMap<>(items);
    }

    /**
     * Clears the cart of all items
     */
    public void clearCart() {
        items.clear();
    }
}

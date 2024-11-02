import java.util.Map;

public class Checkout {
    private ShoppingCart cart;

    public Checkout(ShoppingCart cart) {
        this.cart = cart;
    }

    /**
     * The process purchase class gets the items from the cart and completes the purchase
     * @return boolean indicating is purchase was successful or failed
     */
    public boolean processPurchase() {
        Map<Book, Integer> itemsToPurchase = cart.getItems();

        for (Map.Entry<Book, Integer> entry : itemsToPurchase.entrySet()) {
            Book book = entry.getKey();
            int quantity = entry.getValue();
            if (book.getInventory() < quantity) {
                System.out.println("Not enough inventory for: " + book.getTitle());
                return false; 
            }
        }

        for (Map.Entry<Book, Integer> entry : itemsToPurchase.entrySet()) {
            Book book = entry.getKey();
            int quantity = entry.getValue();
            book.setInventory(book.getInventory() - quantity);
        }
        
        cart.clearCart(); 
        System.out.println("Purchase successful!");
        return true;
    }
}

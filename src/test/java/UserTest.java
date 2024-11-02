import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

public class UserTest {
    private Bookstore bookstore;
    private User user;
    private Book book1;
    private Book book2;

    @Before
    public void setUp() {
        // Set up the bookstore and sample books for testing
        bookstore = new Bookstore();
        book1 = new Book("1234567890", "Book One", "Author A", "Publisher A", "Description A", "url1", 10);
        book2 = new Book("0987654321", "Book Two", "Author B", "Publisher B", "Description B", "url2", 5);
        
        bookstore.getBooks().add(book1);
        bookstore.getBooks().add(book2);
        
        user = new User("testUser", bookstore);
    }

    @Test
    public void testAddToCart() {
        user.addToCart(book1, 2);
        assertEquals(2, user.getCart().getItems().get(book1).intValue());
    }

    @Test
    public void testRemoveFromCart() {
        user.addToCart(book1, 2);
        user.removeFromCart(book1, 1);
        assertEquals(1, user.getCart().getItems().get(book1).intValue());

        // Remove the last item
        user.removeFromCart(book1, 1);
        assertFalse(user.getCart().getItems().containsKey(book1));
    }

    @Test
    public void testCheckoutProcess() {
        user.addToCart(book1, 2);
        assertTrue(user.checkoutProcess());
        assertEquals(8, book1.getInventory());
    }

    @Test
    public void testGetCart() {
        user.addToCart(book1, 2);
        assertEquals(2, user.getCart().getItems().get(book1).intValue());
    }

    @Test
    public void testGetUsername() {
        assertEquals("testUser", user.getUsername());
    }

    // @Test
    // public void testSearchBooksByTitle() {
    //     List<Book> results = user.searchBooksByTitle("Book One");
    //     assertEquals(1, results.size());
    //     assertEquals(book1, results.get(0));
        
    //     // Search for a title that does not exist
    //     results = user.searchBooksByTitle("Non-existent Book");
    //     assertTrue(results.isEmpty());
    // }

    @Test
    public void testGetRecommendations() {
        // This test will need to be implemented once the recommendation logic is complete
        List<Book> recommendations = user.getRecommendations(List.of(user)); // Just passing itself for now
        assertNull(recommendations); // Expecting null until implemented
    }
}

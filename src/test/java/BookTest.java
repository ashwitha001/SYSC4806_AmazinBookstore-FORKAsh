import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BookTest {
    private Book book;

    @Before
    public void setUp() {
        book = new Book("123-456-789", "Test Title", "Test Author", "Test Publisher", "Test Description", "http://example.com/image.jpg", 10);
    }

    @Test
    public void testGetAuthor() {
        assertEquals("Test Author", book.getAuthor());
    }

    @Test
    public void testGetDescription() {
        assertEquals("Test Description", book.getDescription());
    }

    @Test
    public void testGetInventory() {
        assertEquals(10, book.getInventory());
    }

    @Test
    public void testGetIsbn() {
        assertEquals("123-456-789", book.getIsbn());
    }

    @Test
    public void testGetPublisher() {
        assertEquals("Test Publisher", book.getPublisher());
    }

    @Test
    public void testGetTitle() {
        assertEquals("Test Title", book.getTitle());
    }

    @Test
    public void testGetpictureURL() {
        assertEquals("http://example.com/image.jpg", book.getpictureURL());
    }

    @Test
    public void testSetAuthor() {
        book.setAuthor("New Author");
        assertEquals("New Author", book.getAuthor());
    }

    @Test
    public void testSetDescription() {
        book.setDescription("New Description");
        assertEquals("New Description", book.getDescription());
    }

    @Test
    public void testSetInventory() {
        book.setInventory(15);
        assertEquals(15, book.getInventory());
    }

    @Test
    public void testSetIsbn() {
        book.setIsbn("987-654-321");
        assertEquals("987-654-321", book.getIsbn());
    }

    @Test
    public void testSetPublisher() {
        book.setPublisher("New Publisher");
        assertEquals("New Publisher", book.getPublisher());
    }

    @Test
    public void testSetTitle() {
        book.setTitle("New Title");
        assertEquals("New Title", book.getTitle());
    }

    @Test
    public void testSetpictureURL() {
        book.setpictureURL("http://example.com/newimage.jpg");
        assertEquals("http://example.com/newimage.jpg", book.getpictureURL());
    }
}

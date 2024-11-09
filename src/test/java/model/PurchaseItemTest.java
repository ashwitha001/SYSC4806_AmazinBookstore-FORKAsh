package model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PurchaseItemTest {

    private PurchaseItem purchaseItem;
    private Book book;
    private Checkout checkout;

    @Before
    public void setUp() {
        book = new Book(
            "Test Title",
            "Test Description",
            "Test Author",
            "Test Publisher",
            "http://example.com/test.jpg",
            10.00,
            10
        );
        checkout = new Checkout();
        purchaseItem = new PurchaseItem(book, 2, checkout);
    }

    @Test
    public void getId() {
        Long id = 1L;
        purchaseItem.setId(id);
        assertEquals(id, purchaseItem.getId());
    }

    @Test
    public void setId() {
        Long id = 5L;
        purchaseItem.setId(id);
        assertEquals(id, purchaseItem.getId());
    }

    @Test
    public void getBook() {
        assertEquals(book, purchaseItem.getBook());
    }

    @Test
    public void setBook() {
        Book newBook = new Book(
            "New Title", 
            "New Description", 
            "New Author",  
            "New Publisher", 
            "http://newurl.com", 
            25.99, 
            15);
        purchaseItem.setBook(newBook);
        assertEquals(newBook, purchaseItem.getBook());
    }

    @Test
    public void getQuantity() {
        assertEquals((Integer) 2, purchaseItem.getQuantity());
    }

    @Test
    public void setQuantity() {
        Integer newQuantity = 5;
        purchaseItem.setQuantity(newQuantity);
        assertEquals(newQuantity, purchaseItem.getQuantity());
    }

    @Test
    public void getPurchase() {
        assertEquals(checkout, purchaseItem.getPurchase());
    }

    @Test
    public void setPurchase() {
        Checkout newCheckout = new Checkout();
        purchaseItem.setPurchase(newCheckout);
        assertEquals(newCheckout, purchaseItem.getPurchase());
    }
}

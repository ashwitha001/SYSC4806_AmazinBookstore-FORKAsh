package model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class CartTest {

    private Cart cart;
    private User user;
    private CartItem cartItem;

    @Before
    public void setUp() {
        user = new User("testUser");
        cart = new Cart(user);

        Book book = new Book(
            "Test Title",
            "Test Description",
            "Test Author",
            "Test Publisher",
            "http://example.com/test.jpg",
            10.00,
            100
        );
        cartItem = new CartItem(book, 2, cart);
    }

    @Test
    public void getId() {
        cart.setId(1L);
        assertEquals(Long.valueOf(1L), cart.getId());
    }

    @Test
    public void setId() {
        cart.setId(2L);
        assertEquals(Long.valueOf(2L), cart.getId());
    }

    @Test
    public void getUser() {
        assertEquals(user, cart.getUser());
        assertEquals("testUser", cart.getUser().getUsername()); 
    }

    @Test
    public void setUser() {
        User newUser = new User("newUser");
        cart.setUser(newUser);
        assertEquals(newUser, cart.getUser());
        assertEquals("newUser", cart.getUser().getUsername());
    }

    @Test
    public void getItems() {
        cart.addItem(cartItem);
        List<CartItem> items = cart.getItems();
        assertEquals(1, items.size());
        assertTrue(items.contains(cartItem));
    }

    @Test
    public void setItems() {
        List<CartItem> newItems = new ArrayList<>();
        newItems.add(cartItem);

        cart.setItems(newItems);
        assertEquals(newItems, cart.getItems());
        assertEquals(cart, cartItem.getCart());
    }

    @Test
    public void addItem() {
        cart.addItem(cartItem);
        assertTrue(cart.getItems().contains(cartItem));
        assertEquals(cart, cartItem.getCart()); 
    }

    @Test
    public void removeItem() {
        cart.addItem(cartItem);
        cart.removeItem(cartItem);
        assertFalse(cart.getItems().contains(cartItem));
        assertNull(cartItem.getCart()); 
    }
}

package controller;

import com.bookstore.controller.PurchaseController;
import com.bookstore.dto.CartItemDTO;
import com.bookstore.dto.PurchaseDTO;
import com.bookstore.model.*;
import com.bookstore.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PurchaseControllerTest {

    @Mock
    private CheckoutRepository checkoutRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private Principal principal;

    @InjectMocks
    private PurchaseController purchaseController;

    private User customer;
    private Book book;
    private CartItemDTO cartItem;
    private Checkout checkout;
    private PurchaseItem purchaseItem;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        book = new Book();
        book.setId(1);
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setIsbn("1234567890");
        book.setPrice(29.99);
        book.setInventory(10);

        customer = new User();
        customer.setId(1L);
        customer.setUsername("customer");
        customer.setRole(Role.CUSTOMER);

        cartItem = new CartItemDTO();
        cartItem.setBookId(1L);
        cartItem.setQuantity(2);
        cartItem.setTitle("Test Book");
        cartItem.setAuthor("Test Author");
        cartItem.setIsbn("1234567890");
        cartItem.setPurchasePrice(29.99);

        purchaseItem = new PurchaseItem(book, 2, null);

        checkout = new Checkout();
        checkout.setId(1L);
        checkout.setUser(customer);
        checkout.setPurchaseDate(LocalDateTime.now());
        checkout.setItems(Collections.singletonList(purchaseItem));

        when(principal.getName()).thenReturn("customer");
    }

    @Test
    void tearDown() throws Exception {
        if (autoCloseable != null) {
            autoCloseable.close();
        }
    }

    /**
     * Tests successful checkout process with sufficient inventory
     */
    @Test
    void testSuccessfulCheckout() {
        List<CartItemDTO> cartItems = Collections.singletonList(cartItem);
        when(userRepository.findByUsername("customer")).thenReturn(Optional.of(customer));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(checkoutRepository.save(any(Checkout.class))).thenReturn(checkout);

        ResponseEntity<String> response = purchaseController.checkout(cartItems, principal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Checkout successful.", response.getBody());
        verify(bookRepository).save(book);
        assertEquals(8, book.getInventory());
    }

    /**
     * Tests checkout with insufficient inventory
     */
    @Test
    void testCheckoutInsufficientInventory() {
        cartItem.setQuantity(15);
        List<CartItemDTO> cartItems = Collections.singletonList(cartItem);
        when(userRepository.findByUsername("customer")).thenReturn(Optional.of(customer));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        ResponseEntity<String> response = purchaseController.checkout(cartItems, principal);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Not enough inventory"));
    }

    /**
     * Tests checkout with non-existent book
     */
    @Test
    void testCheckoutNonexistentBook() {
        List<CartItemDTO> cartItems = Collections.singletonList(cartItem);
        when(userRepository.findByUsername("customer")).thenReturn(Optional.of(customer));
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<String> response = purchaseController.checkout(cartItems, principal);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Book with ID 1 not found"));
    }

    /**
     * Tests checkout with invalid user
     */
    @Test
    void testCheckoutInvalidUser() {
        List<CartItemDTO> cartItems = Collections.singletonList(cartItem);
        when(userRepository.findByUsername("customer")).thenReturn(Optional.empty());

        ResponseEntity<String> response = purchaseController.checkout(cartItems, principal);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("User not found"));
    }

    /**
     * Tests viewing purchase history for user with purchases
     */
    @Test
    void testViewPurchaseHistory() {
        when(userRepository.findByUsername("customer")).thenReturn(Optional.of(customer));
        when(checkoutRepository.findByUser(customer)).thenReturn(Collections.singletonList(checkout));

        ResponseEntity<List<PurchaseDTO>> response = purchaseController.getPurchaseHistory(principal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    /**
     * Tests viewing purchase history for user with no purchases
     */
    @Test
    void testViewEmptyPurchaseHistory() {
        when(userRepository.findByUsername("customer")).thenReturn(Optional.of(customer));
        when(checkoutRepository.findByUser(customer)).thenReturn(new ArrayList<>());

        ResponseEntity<List<PurchaseDTO>> response = purchaseController.getPurchaseHistory(principal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    /**
     * Tests viewing purchase history for non-existent user
     */
    @Test
    void testViewPurchaseHistoryInvalidUser() {
        when(userRepository.findByUsername("customer")).thenReturn(Optional.empty());

        ResponseEntity<List<PurchaseDTO>> response = purchaseController.getPurchaseHistory(principal);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
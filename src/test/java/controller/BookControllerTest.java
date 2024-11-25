package controller;

import com.bookstore.controller.BookController;
import com.bookstore.model.*;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.CheckoutRepository;
import com.bookstore.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BookControllerTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private CheckoutRepository checkoutRepository;

    @InjectMocks
    private BookController bookController;

    private Book book1;
    private Book book2;
    private User admin;
    private User customer;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        book1 = new Book();
        book1.setId(1);
        book1.setIsbn("1234567890");
        book1.setTitle("Test Book 1");
        book1.setDescription("Description 1");
        book1.setAuthor("Author 1");
        book1.setPublisher("Publisher 1");
        book1.setPictureURL("url1");
        book1.setPrice(29.99);
        book1.setInventory(10);

        book2 = new Book();
        book2.setId(2);
        book2.setIsbn("0987654321");
        book2.setTitle("Test Book 2");
        book2.setDescription("Description 2");
        book2.setAuthor("Author 2");
        book2.setPublisher("Publisher 2");
        book2.setPictureURL("url2");
        book2.setPrice(19.99);
        book2.setInventory(5);

        admin = new User("admin", Role.ADMIN);
        customer = new User("customer", Role.CUSTOMER);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void tearDown() throws Exception {
        if (autoCloseable != null) {
            autoCloseable.close();
        }
    }

    /**
     * Tests retrieving all books from the repository
     */
    @Test
    void testGetAllBooks() {
        List<Book> books = Arrays.asList(book1, book2);
        when(bookRepository.findAll()).thenReturn(books);

        List<Book> result = bookController.getAllBooks();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Test Book 1", result.get(0).getTitle());
        assertEquals("Test Book 2", result.get(1).getTitle());
    }

    /**
     * Tests retrieving a book by ID when the book exists
     */
    @Test
    void testGetExistingBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));

        ResponseEntity<Book> response = bookController.getBookById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(book1.getTitle(), response.getBody().getTitle());
    }

    /**
     * Tests retrieving a book by ID when the book doesn't exist
     */
    @Test
    void testGetNonexistentBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Book> response = bookController.getBookById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Tests searching books by title with matching results
     */
    @Test
    void testSearchBooksByTitle() {
        List<Book> matchingBooks = Arrays.asList(book1, book2);
        when(bookRepository.findByTitleContainingIgnoreCase("Test")).thenReturn(matchingBooks);

        List<Book> result = bookController.searchBooks("Test");

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(book -> book.getTitle().equals("Test Book 1")));
    }

    /**
     * Tests searching books with no matching results
     */
    @Test
    void testSearchBooksNoMatches() {
        when(bookRepository.findByTitleContainingIgnoreCase("Nonexistent")).thenReturn(new ArrayList<>());

        List<Book> result = bookController.searchBooks("Nonexistent");

        assertTrue(result.isEmpty());
    }

    /**
     * Tests searching books by ISBN
     */
    @Test
    void testSearchByIsbn() {
        List<Book> matchingBooks = Arrays.asList(book1);
        when(bookRepository.findByIsbnContainingIgnoreCase("123")).thenReturn(matchingBooks);

        ResponseEntity<List<Book>> response = bookController.searchBooksByIsbn("123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("1234567890", response.getBody().get(0).getIsbn());
    }

    /**
     * Tests searching books by author
     */
    @Test
    void testSearchByAuthor() {
        List<Book> matchingBooks = Arrays.asList(book1);
        when(bookRepository.findByAuthorContainingIgnoreCase("Author 1")).thenReturn(matchingBooks);

        List<Book> result = bookController.searchBooksByAuthor("Author 1");

        assertEquals(1, result.size());
        assertEquals("Author 1", result.get(0).getAuthor());
    }

    /**
     * Tests uploading a new book as admin
     */
    @Test
    void testUploadBookAsAdmin() {
        when(authentication.getName()).thenReturn("admin");
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(admin));
        when(bookRepository.save(any(Book.class))).thenReturn(book1);

        ResponseEntity<?> response = bookController.uploadBook(book1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Tests uploading a book with duplicate ISBN
     */
    @Test
    void testUploadDuplicateBook() {
        when(authentication.getName()).thenReturn("admin");
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(admin));
        when(bookRepository.save(any(Book.class)))
                .thenThrow(new DataIntegrityViolationException("Duplicate ISBN"));

        ResponseEntity<?> response = bookController.uploadBook(book1);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    /**
     * Tests updating an existing book as admin
     */
    @Test
    void testUpdateExistingBook() {
        when(authentication.getName()).thenReturn("admin");
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(admin));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        when(bookRepository.save(any(Book.class))).thenReturn(book1);

        Book updatedBook = new Book();
        updatedBook.setId(1);
        updatedBook.setTitle("Updated Title");
        updatedBook.setIsbn("1234567890");

        ResponseEntity<?> response = bookController.editBook(1L, updatedBook);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Tests updating a non-existent book
     */
    @Test
    void testUpdateNonexistentBook() {
        when(authentication.getName()).thenReturn("admin");
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(admin));
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = bookController.editBook(1L, book1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Tests deleting an existing book as admin
     */
    @Test
    void testDeleteExistingBook() {
        when(authentication.getName()).thenReturn("admin");
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(admin));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));

        ResponseEntity<?> response = bookController.deleteBook(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(bookRepository).delete(book1);
    }

    /**
     * Tests deleting a non-existent book
     */
    @Test
    void testDeleteNonexistentBook() {
        when(authentication.getName()).thenReturn("admin");
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(admin));
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = bookController.deleteBook(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Tests getting recommendations when user has purchase history and similar users exist
     */
    @Test
    void testGetRecommendationsWithSimilarUsers() {
        // Create test users
        User user1 = new User("user1", Role.CUSTOMER);
        user1.setId(1L);
        User user2 = new User("user2", Role.CUSTOMER);
        user2.setId(2L);

        // Create test books
        Book book1 = new Book();
        book1.setId(1);
        Book book2 = new Book();
        book2.setId(2);
        Book book3 = new Book();
        book3.setId(3);

        // Create purchase histories
        Checkout user1Checkout = new Checkout();
        user1Checkout.setUser(user1);
        user1Checkout.setItems(Arrays.asList(
                new PurchaseItem(book1, 1, null),
                new PurchaseItem(book2, 1, null)
        ));

        Checkout user2Checkout = new Checkout();
        user2Checkout.setUser(user2);
        user2Checkout.setItems(Arrays.asList(
                new PurchaseItem(book2, 1, null),
                new PurchaseItem(book3, 1, null)
        ));

        // Mock authentication
        when(authentication.getName()).thenReturn("user1");
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user1));

        // Mock repository calls
        when(checkoutRepository.findByUser(user1)).thenReturn(Arrays.asList(user1Checkout));
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));
        when(checkoutRepository.findByUser(user2)).thenReturn(Arrays.asList(user2Checkout));
        when(bookRepository.findAllById(any())).thenReturn(Arrays.asList(book3));

        // Test recommendation endpoint
        ResponseEntity<List<Book>> response = bookController.getRecommendedBooks(authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(book3.getId(), response.getBody().get(0).getId());
    }

    /**
     * Tests getting recommendations when user has no purchase history
     */
    @Test
    void testGetRecommendationsWithNoPurchaseHistory() {
        User user = new User("user", Role.CUSTOMER);
        when(authentication.getName()).thenReturn("user");
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        when(checkoutRepository.findByUser(user)).thenReturn(new ArrayList<>());

        ResponseEntity<List<Book>> response = bookController.getRecommendedBooks(authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).isEmpty());
    }

    /**
     * Tests getting recommendations when no similar users exist
     */
    @Test
    void testGetRecommendationsWithNoSimilarUsers() {
        User user = new User("user", Role.CUSTOMER);
        Checkout checkout = new Checkout();
        checkout.setUser(user);
        checkout.setItems(List.of(new PurchaseItem(book1, 1, null)));

        when(authentication.getName()).thenReturn("user");
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        when(checkoutRepository.findByUser(user)).thenReturn(List.of(checkout));
        when(userRepository.findAll()).thenReturn(List.of(user));

        ResponseEntity<List<Book>> response = bookController.getRecommendedBooks(authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).isEmpty());
    }

    /**
     * Tests getting recommendations with invalid user
     */
    @Test
    void testGetRecommendationsWithInvalidUser() {
        when(authentication.getName()).thenReturn("nonexistent");
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                bookController.getRecommendedBooks(authentication));
    }
}
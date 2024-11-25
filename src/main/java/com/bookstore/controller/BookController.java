package com.bookstore.controller;

import com.bookstore.model.Book;
import com.bookstore.model.Role;
import com.bookstore.model.User;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing books and handling book-related operations in the bookstore.
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Retrieves all books from the database.
     */
    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Finds a specific book by its unique identifier.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookRepository.findById(id)
                .map(book -> ResponseEntity.ok().body(book))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Searches for books by matching title keywords.
     */
    @GetMapping("/search")
    public List<Book> searchBooks(@RequestParam String keyword) {
        return bookRepository.findByTitleContainingIgnoreCase(keyword);
    }

    /**
     * Searches for books by ISBN pattern.
     */
    @GetMapping("/search/isbn")
    public ResponseEntity<List<Book>> searchBooksByIsbn(@RequestParam String isbn) {
        List<Book> books = bookRepository.findByIsbnContainingIgnoreCase(isbn);
        return ResponseEntity.ok(books);
    }

    /**
     * Searches for books by author name.
     */
    @GetMapping("/search/author")
    public List<Book> searchBooksByAuthor(@RequestParam String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author);
    }

    /**
     * Searches for books by publisher name.
     */
    @GetMapping("/search/publisher")
    public List<Book> searchBooksByPublisher(@RequestParam String publisher) {
        return bookRepository.findByPublisherContainingIgnoreCase(publisher);
    }

    /**
     * Filters books within a specified price range.
     */
    @GetMapping("/filter/price")
    public List<Book> filterBooksByPrice(@RequestParam Double minPrice, @RequestParam Double maxPrice) {
        return bookRepository.findByPriceBetween(minPrice, maxPrice);
    }

    /**
     * Filters books by minimum inventory level.
     */
    @GetMapping("/filter/inventory")
    public List<Book> filterBooksByInventory(@RequestParam int minInventory) {
        return bookRepository.findByInventoryGreaterThan(minInventory);
    }

    /**
     * Adds a new book to the inventory (admin access required).
     */
    @PostMapping
    public ResponseEntity<?> uploadBook(@RequestBody Book book) {
        // get current user from Security Context
        if (isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied. Admin role required.");

        try {
            Book savedBook = bookRepository.save(book);
            return ResponseEntity.ok(savedBook);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().toLowerCase().contains("isbn")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("A book with ISBN " + book.getIsbn() + " already exists.");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error saving book: " + e.getMessage());
        }
    }

    /**
     * Updates an existing book's information (admin access required).
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> editBook(@PathVariable Long id, @RequestBody Book bookDetails) {
        // get current user from Security Context
        if (isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied. Admin role required.");

        Book existingBook = bookRepository.findById(id).orElse(null);
        if (existingBook == null) {
            return ResponseEntity.notFound().build();
        }

        // only check ISBN if it's being changed
        if (!existingBook.getIsbn().equals(bookDetails.getIsbn())) {
            List<Book> booksWithSimilarIsbn = bookRepository.findByIsbnContainingIgnoreCase(bookDetails.getIsbn());
            // check for exact match within the similar results
            boolean exactMatchExists = booksWithSimilarIsbn.stream()
                    .anyMatch(book -> book.getIsbn().equalsIgnoreCase(bookDetails.getIsbn()) &&
                            book.getId() != existingBook.getId());

            if (exactMatchExists) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("A book with ISBN " + bookDetails.getIsbn() + " already exists.");
            }
        }

        try {
            existingBook.setIsbn(bookDetails.getIsbn());
            existingBook.setTitle(bookDetails.getTitle());
            existingBook.setDescription(bookDetails.getDescription());
            existingBook.setAuthor(bookDetails.getAuthor());
            existingBook.setPublisher(bookDetails.getPublisher());
            existingBook.setPictureURL(bookDetails.getPictureURL());
            existingBook.setPrice(bookDetails.getPrice());
            existingBook.setInventory(bookDetails.getInventory());

            Book updatedBook = bookRepository.save(existingBook);
            return ResponseEntity.ok(updatedBook);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().toLowerCase().contains("isbn")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("A book with ISBN " + bookDetails.getIsbn() + " already exists.");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error updating book: " + e.getMessage());
        }
    }

    /**
     * Removes a book from the inventory (admin access required).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        if (isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied. Admin role required.");
        return bookRepository.findById(id).map(book -> {
            bookRepository.delete(book);
            return ResponseEntity.ok().body("Book deleted successfully.");
        }).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Checks if the current user has admin role.
     */
    private boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getRole() != Role.ADMIN;
    }
}
package com.bookstore.controller;

import com.bookstore.model.Book;
import com.bookstore.model.Role;
import com.bookstore.model.User;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling book-related API endpoints.
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    // Get all books
    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Get book by ID
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookRepository.findById(id)
                .map(book -> ResponseEntity.ok().body(book))
                .orElse(ResponseEntity.notFound().build());
    }

    // Search by title
    @GetMapping("/search")
    public List<Book> searchBooks(@RequestParam String keyword) {
        return bookRepository.findByTitleContainingIgnoreCase(keyword);
    }

    // Search by author
    @GetMapping("/search/author")
    public List<Book> searchBooksByAuthor(@RequestParam String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author);
    }

    // Search by publisher
    @GetMapping("/search/publisher")
    public List<Book> searchBooksByPublisher(@RequestParam String publisher) {
        return bookRepository.findByPublisherContainingIgnoreCase(publisher);
    }

    // Filter by price range
    @GetMapping("/filter/price")
    public List<Book> filterBooksByPrice(@RequestParam Double minPrice, @RequestParam Double maxPrice) {
        return bookRepository.findByPriceBetween(minPrice, maxPrice);
    }

    // Filter by inventory
    @GetMapping("/filter/inventory")
    public List<Book> filterBooksByInventory(@RequestParam int minInventory) {
        return bookRepository.findByInventoryGreaterThan(minInventory);
    }

    // Upload a new book (Admin only)
    @PostMapping
    public ResponseEntity<?> uploadBook(@RequestBody Book book, @RequestParam Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null || user.getRole() != Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied.");
        }
        try {
            Book savedBook = bookRepository.save(book);
            return ResponseEntity.ok(savedBook);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error saving book: " + e.getMessage());
        }
    }

    // Edit an existing book (Admin only)
    @PutMapping("/{id}")
    public ResponseEntity<?> editBook(@PathVariable Long id, @RequestBody Book bookDetails, @RequestParam Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null || user.getRole() != Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied.");
        }

        return bookRepository.findById(id).map(book -> {
            book.setIsbn(bookDetails.getIsbn());
            book.setTitle(bookDetails.getTitle());
            book.setDescription(bookDetails.getDescription());
            book.setAuthor(bookDetails.getAuthor());
            book.setPublisher(bookDetails.getPublisher());
            book.setPictureURL(bookDetails.getPictureURL());
            book.setPrice(bookDetails.getPrice());
            book.setInventory(bookDetails.getInventory());
            bookRepository.save(book);
            return ResponseEntity.ok(book);
        }).orElse(ResponseEntity.notFound().build());
    }

    // Delete a book (Admin only)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id, @RequestParam Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null || user.getRole() != Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied.");
        }

        return bookRepository.findById(id).map(book -> {
            bookRepository.delete(book);
            return ResponseEntity.ok().body("Book deleted successfully.");
        }).orElse(ResponseEntity.notFound().build());
    }
}

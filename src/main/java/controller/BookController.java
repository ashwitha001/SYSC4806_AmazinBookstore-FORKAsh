package controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import model.Book;
import repository.BookRepository;

@RestController
@RequestMapping("/api/books")
public class BookController {
    
    @Autowired
    private BookRepository bookRepository;

    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

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

    @PostMapping
    public Book uploadBook(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    @PutMapping("/{id}")
    public Book editBook(@PathVariable Long id, @RequestBody Book bookDetails) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
        book.setIsbn(bookDetails.getIsbn());
        book.setTitle(bookDetails.getTitle());
        book.setDescription(bookDetails.getDescription());
        book.setAuthor(bookDetails.getAuthor());
        book.setPublisher(bookDetails.getPublisher());
        book.setPictureURL(bookDetails.getPictureURL());
        book.setPrice(bookDetails.getPrice());
        book.setInventory(bookDetails.getInventory());

        return bookRepository.save(book);
    }

    // Add an endpoint for deleting books
    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
    }
}

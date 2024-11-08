package com.bookstore.controller;

import com.bookstore.model.*;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.CheckoutRepository;
import com.bookstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controller for handling purchase-related API endpoints.
 */
@RestController
@RequestMapping("/api/purchase")
public class PurchaseController {

    @Autowired
    private CheckoutRepository purchaseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    // DTO class to receive cart items from frontend
    public static class CartItemDTO {
        private Long bookId;
        private int quantity;

        // Constructors
        public CartItemDTO() {}

        public CartItemDTO(Long bookId, int quantity) {
            this.bookId = bookId;
            this.quantity = quantity;
        }

        // Getters and Setters
        public Long getBookId() {
            return bookId;
        }

        public void setBookId(Long bookId) {
            this.bookId = bookId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }

    /**
     * Handles the checkout process.
     * Deducts purchased quantities from inventory and creates purchase records.
     *
     * @param userId    The ID of the user making the purchase.
     * @param cartItems The list of items in the cart.
     * @return ResponseEntity with status and message.
     */
    @PostMapping("/checkout")
    @Transactional
    public ResponseEntity<String> checkout(@RequestParam Long userId, @RequestBody List<CartItemDTO> cartItems) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null || user.getRole() != Role.CUSTOMER) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found or invalid role.");
        }

        List<PurchaseItem> purchaseItems = new ArrayList<>();
        for (CartItemDTO cartItemDTO : cartItems) {
            Optional<Book> optionalBook = bookRepository.findById(cartItemDTO.getBookId());
            if (optionalBook.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Book with ID " + cartItemDTO.getBookId() + " not found.");
            }

            Book book = optionalBook.get();

            // Check if enough inventory is available
            if (book.getInventory() < cartItemDTO.getQuantity()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Not enough inventory for book: " + book.getTitle());
            }

            // Deduct the purchased quantity from the inventory
            book.setInventory(book.getInventory() - cartItemDTO.getQuantity());
            bookRepository.save(book);

            PurchaseItem purchaseItem = new PurchaseItem(book, cartItemDTO.getQuantity(), null);
            purchaseItems.add(purchaseItem);
        }

        // Create a new checkout record
        Checkout purchase = new Checkout();
        purchase.setUser(user);
        purchase.setPurchaseDate(LocalDateTime.now());
        purchase.setItems(purchaseItems);

        // Associate purchase items with the checkout
        for (PurchaseItem item : purchaseItems) {
            item.setPurchase(purchase);
        }

        // Save the purchase and purchase items
        purchaseRepository.save(purchase);

        return ResponseEntity.ok("Checkout successful.");
    }

    /**
     * Placeholder for recommendations endpoint.
     *
     * @param userId The ID of the user requesting recommendations.
     * @return A list of recommended books.
     */
    @GetMapping("/recommendations")
    public ResponseEntity<List<Book>> getRecommendations(@RequestParam Long userId) {
        // Implement recommendation logic here
        return ResponseEntity.ok(new ArrayList<>()); // Placeholder
    }
}

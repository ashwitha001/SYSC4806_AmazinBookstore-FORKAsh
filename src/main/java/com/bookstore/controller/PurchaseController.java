package com.bookstore.controller;

import com.bookstore.dto.CartItemDTO;
import com.bookstore.dto.PurchaseDTO;
import com.bookstore.model.*;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.CheckoutRepository;
import com.bookstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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

    /**
     * Handles the checkout process.
     * Deducts purchased quantities from inventory and creates purchase records.
     *
     * @param principal The authentication principal.
     * @param cartItems The list of items in the cart.
     * @return ResponseEntity with status and message.
     */
    @PostMapping("/checkout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> checkout(@RequestBody List<CartItemDTO> cartItems, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsernameIgnoreCase(username).orElse(null);

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

            // Create PurchaseItem with book details
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

        purchaseRepository.save(purchase);

        return ResponseEntity.ok("Checkout successful.");
    }
    
    /**
     * Retrieves the purchase history for the authenticated user.
     * Fetches all checkout records associated with the user and converts them to DTOs
     * containing purchase details like ID, date, and items purchased.
     *
     * @param principal The authenticated user's principal containing user details
     * @return ResponseEntity containing a list of PurchaseDTO objects representing the user's purchase history
     */
    @GetMapping("/history")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<PurchaseDTO>> getPurchaseHistory(Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsernameIgnoreCase(username).orElse(null);
        List<Checkout> checkouts = purchaseRepository.findByUser(user);
        
        // Map the purchase entities to DTOs
        List<PurchaseDTO> purchaseHistory = checkouts.stream()
                .map(purchase -> new PurchaseDTO(
                        purchase.getId(),
                        purchase.getPurchaseDate(),
                        purchase.getItems().stream()
                                .map(item -> new CartItemDTO(item.getBookId(), item.getQuantity()))
                                .toList()
                ))
                .toList();

        return ResponseEntity.ok(purchaseHistory);
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

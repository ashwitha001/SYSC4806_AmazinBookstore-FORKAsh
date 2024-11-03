package controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import model.Book;
import model.Checkout;
import model.User;
import repository.BookRepository;
import repository.CheckoutRepository;
import repository.UserRepository;

@RestController
@RequestMapping("/api/purchase")
public class PurchaseController {
    @Autowired
    private CheckoutRepository purchaseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;

    @PostMapping("/checkout")
    public void checkout(@RequestParam Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        Checkout purchase = new Checkout();
        purchase.setUser(user);
        purchase.setPurchaseDate(LocalDateTime.now());

        purchaseRepository.save(purchase);
    }

    @GetMapping("/recommendations")
    public List<Book> getRecommendations(@RequestParam Long userId) {
        
        return new ArrayList<>(); // Placeholder for recommended books
    }
}

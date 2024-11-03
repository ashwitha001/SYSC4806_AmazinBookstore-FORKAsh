package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import model.Book;
import model.Cart;
import model.CartItem;
import repository.BookRepository;
import repository.CartRepository;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private BookRepository bookRepository;

    @PostMapping("/add")
    public void addToCart(@RequestBody CartItem cartItem, @RequestParam Long userId) {
        Cart cart = cartRepository.findById(userId).orElse(new Cart());
        Book book = bookRepository.findById(cartItem.getBook().getIsbn()).orElse(null);

        cart.getItems().add(cartItem);
        cartRepository.save(cart);
    }

    // Need to add more endpoints for viewing and removing items
}

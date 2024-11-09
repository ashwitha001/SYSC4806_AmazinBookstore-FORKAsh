package com.bookstore.controller;

import com.bookstore.model.Cart;
import com.bookstore.model.CartItem;
import com.bookstore.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller handling shopping cart operations for the bookstore.
 */
@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartRepository cartRepository;

    /**
     * Adds an item to the user's shopping cart.
     */
    @PostMapping("/add")
    public void addToCart(@RequestBody CartItem cartItem, @RequestParam Long userId) {
        Cart cart = cartRepository.findById(userId).orElse(new Cart());
        cart.getItems().add(cartItem);
        cartRepository.save(cart);
    }

    /**
     * Removes an item from the user's shopping cart.
     */
    @PostMapping("/remove")
    public void removeFromCart(@RequestBody CartItem cartItem, @RequestParam Long userId) {
        Cart cart = cartRepository.findById(userId).orElse(new Cart());
        cart.getItems().remove(cartItem);
        cartRepository.save(cart);
    }

    /**
     * Removes all items from the user's shopping cart.
     */
    @PostMapping("/clear")
    public void clearCart(@RequestParam Long userId) {
        Cart cart = cartRepository.findById(userId).orElse(new Cart());
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
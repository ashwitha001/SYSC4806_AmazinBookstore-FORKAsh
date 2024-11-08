package com.bookstore.dto;

public class CartItemDTO {
    private Long bookId;
    private int quantity;

    // Constructors, Getters, and Setters
    public CartItemDTO() {}

    public CartItemDTO(Long bookId, int quantity) {
        this.bookId = bookId;
        this.quantity = quantity;
    }

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

package com.bookstore.dto;

public class CartItemDTO {
    private String bookId;
    private int quantity;

    /** The title of the book. */
    private String title;

    /** The author of the book. */
    private String author;

    /** The ISBN of the book. */
    private String isbn;

    /** The purchase price of the book. */
    private Double purchasePrice;

    /**
     * Default constructor.
     */
    public CartItemDTO() {}

    /**
     * Parameterized constructor to initialize all fields.
     *
     * @param bookId        the ID of the book
     * @param quantity      the quantity of the book in the cart
     * @param title         the title of the book
     * @param author        the author of the book
     * @param isbn          the ISBN of the book
     * @param purchasePrice the purchase price of the book
     */
    public CartItemDTO(String bookId, int quantity, String title, String author, String isbn, Double purchasePrice) {
        this.bookId = bookId;
        this.quantity = quantity;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.purchasePrice = purchasePrice;
    }

    /**
     * Gets the book ID.
     *
     * @return the book ID
     */
    public String getBookId() {
        return bookId;
    }

    /**
     * Sets the book ID.
     *
     * @param bookId the book ID to set
     */
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    /**
     * Gets the quantity of the book in the cart.
     *
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the book in the cart.
     *
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Gets the title of the book.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the book.
     *
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the author of the book.
     *
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the author of the book.
     *
     * @param author the author to set
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Gets the ISBN of the book.
     *
     * @return the ISBN
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Sets the ISBN of the book.
     *
     * @param isbn the ISBN to set
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Gets the purchase price of the book.
     *
     * @return the purchase price
     */
    public Double getPurchasePrice() {
        return purchasePrice;
    }

    /**
     * Sets the purchase price of the book.
     *
     * @param purchasePrice the purchase price to set
     */
    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }
}

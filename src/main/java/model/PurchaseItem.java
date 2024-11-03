package model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class PurchaseItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Book book;

    private Integer quantity;

    @ManyToOne
    private Checkout purchase;

    
    /**
     * Default constructor for PurchaseItem
     */
    public PurchaseItem() {}

    /**
     * Constructs a new PurchaseItem with the specified book, quantity, and checkout record.
     * @param book The book being purchased.
     * @param quantity The quantity of the book purchased.
     * @param purchase The checkout transaction this item is part of.
     */
    public PurchaseItem(Book book, Integer quantity, Checkout purchase) {
        this.book = book;
        this.quantity = quantity;
        this.purchase = purchase;
    }

    /**
     * Gets the unique identifier of this purchase item.
     * @return the id of the purchase item.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of this purchase item.
     * @param id the new id of the purchase item.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the book associated with this purchase item.
     * @return the book of this purchase item.
     */
    public Book getBook() {
        return book;
    }

    /**
     * Sets the book for this purchase item.
     * @param book the new book to associate with this purchase item.
     */
    public void setBook(Book book) {
        this.book = book;
    }

    /**
     * Gets the quantity of the book in this purchase item.
     * @return the quantity of the book.
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity for this purchase item.
     * @param quantity the new quantity of the book in this purchase item.
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * Gets the checkout transaction this purchase item is part of.
     * @return the checkout transaction associated with this item.
     */
    public Checkout getPurchase() {
        return purchase;
    }

    /**
     * Sets the checkout transaction for this purchase item.
     * @param purchase the new checkout transaction to associate with this item.
     */
    public void setPurchase(Checkout purchase) {
        this.purchase = purchase;
    }
}

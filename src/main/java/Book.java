public class Book {
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private String description;
    private String pictureURL; 
    private int inventory; // Available stock

    /**
     * Book class constructor
     * @param isbn - ID of the book
     * @param title - title of the book
     * @param author - author of the book
     * @param publisher - publisher of the book
     * @param description - book description
     * @param pictureURL - picture of the book
     * @param inventory - available stock 
     */
    public Book(String isbn, String title, String author, String publisher, String description, String pictureURL, int inventory) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.description = description;
        this.pictureURL = pictureURL;
        this.inventory = inventory;
    }

    /**
     * Gets the ISBN of the book.
     * @return the ISBN of the book.
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Gets the title of the book.
     * @return the title of the book.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the author of the book.
     * @return the author of the book.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Gets the publisher of the book.
     * @return the publisher of the book.
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * Gets the description of the book.
     * @return the description of the book.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the picture URL of the book.
     * @return the picture URL of the book.
     */
    public String getpictureURL() {
        return pictureURL;
    }

    /**
     * Gets the available inventory of the book.
     * @return the available inventory of the book.
     */
    public int getInventory() {
        return inventory;
    }

    /**
     * Sets the ISBN of the book.
     * @param isbn the new ISBN of the book.
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Sets the title of the book.
     * @param title the new title of the book.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets the author of the book.
     * @param author the new author of the book.
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Sets the publisher of the book.
     * @param publisher the new publisher of the book.
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    /**
     * Sets the description of the book.
     * @param description the new description of the book.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the picture URL of the book.
     * @param pictureURL the new picture URL of the book.
     */
    public void setpictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    /**
     * Sets the inventory count of the book.
     * @param inventory the new inventory count of the book.
     */
    public void setInventory(int inventory) {
        this.inventory = inventory;
    }
}

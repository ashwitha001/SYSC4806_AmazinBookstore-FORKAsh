public class Book {
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private String description;
    private String pictureURL; 
    private int inventory; // Available stock

    public Book(String isbn, String title, String author, String publisher, String description, String pictureURL, int inventory) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.description = description;
        this.pictureURL = pictureURL;
        this.inventory = inventory;
    }

    /*
     * Getters
     */
    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getDescription() {
        return description;
    }

    public String getpictureURL() {
        return pictureURL;
    }

    public int getInventory() {
        return inventory;
    }

    /*
     * Setters
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setpictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }
}

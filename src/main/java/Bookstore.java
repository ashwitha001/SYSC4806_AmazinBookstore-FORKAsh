import java.util.ArrayList;
import java.util.List;

public class Bookstore {
    private List<Book> books;
    private List<User> users;

    /**
     * Bookstore class constructor
     */
    public Bookstore() {
        this.books = new ArrayList<>();
        this.users = new ArrayList<>();
    }

    /**
     * Searches for books by their title.
     * @param title the title of the book to search for
     * @return a list of books that contain the specified title
     */
    public List<Book> searchByTitle(String title) {
        List<Book> results = new ArrayList<>();
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(title.toLowerCase())) {
                results.add(book);
            }
        }
        return results;
    }

    /**
     * Retrieves the list of all books in the bookstore.
     * @return a new list containing all books in the bookstore.
     */
    public List<Book> getBooks() {
        return new ArrayList<>(books); 
    }
}

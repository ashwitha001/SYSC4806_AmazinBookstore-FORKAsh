import java.util.ArrayList;
import java.util.List;

public class Bookstore {
    private List<Book> books;
    private List<User> users;

    public Bookstore() {
        this.books = new ArrayList<>();
        this.users = new ArrayList<>();
    }

    public List<Book> searchByTitle(String title) {
        List<Book> results = new ArrayList<>();
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(title.toLowerCase())) {
                results.add(book);
            }
        }
        return results;
    }

    // New method to get the list of books in the bookstore
    public List<Book> getBooks() {
        return new ArrayList<>(books); 
    }
}

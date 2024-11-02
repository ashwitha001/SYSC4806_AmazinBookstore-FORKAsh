import java.util.Scanner;
import java.util.List;

public class BookstoreApp {
    private Bookstore bookstore;
    private Scanner scanner;

    public BookstoreApp() {
        this.bookstore = new Bookstore();
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        BookstoreApp app = new BookstoreApp();
        app.run();
    }

    public void run() {
        while (true) {
            System.out.println("Welcome to the AmazinBookstore!");
            System.out.println("1. Owner Login");
            System.out.println("2. User Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            if (choice == 1) {
                ownerMenu();
            } else if (choice == 2) {
                userMenu();
            } else if (choice == 3) {
                System.out.println("Goodbye!");
                break;
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void ownerMenu() {
        while (true) {
            System.out.println("\nOwner Menu:");
            System.out.println("1. Add a Book");
            System.out.println("2. Edit Book");
            System.out.println("3. View All Books");
            System.out.println("4. Back to Main Menu");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            if (choice == 1) {
                addBook();
            } else if (choice == 2) {
                editBook();
            } else if (choice == 3) {
                viewAllBooks();
            } else if (choice == 4) {
                break;
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void userMenu() {
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        User user = new User(username, bookstore);
        
        while (true) {
            System.out.println("\nUser Menu:");
            System.out.println("1. Search Books by Title");
            System.out.println("2. View All Books");
            System.out.println("3. Add to Cart");
            System.out.println("4. Remove from Cart");
            System.out.println("5. Checkout");
            System.out.println("6. Back to Main Menu");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            if (choice == 1) {
                searchBooks(user);
            } else if (choice == 2) {
                viewAllBooks();
            } else if (choice == 3) {
                addToCart(user);
            } else if (choice == 4) {
                removeFromCart(user);
            } else if (choice == 5) {
                if (user.checkoutProcess()) {
                    System.out.println("Thank you for your purchase, " + user.getUsername() + "!");
                } else {
                    System.out.println("Checkout failed due to insufficient inventory.");
                }
            } else if (choice == 6) {
                break;
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void addBook() {
        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine();
        System.out.print("Enter Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Author: ");
        String author = scanner.nextLine();
        System.out.print("Enter Publisher: ");
        String publisher = scanner.nextLine();
        System.out.print("Enter Description: ");
        String description = scanner.nextLine();
        System.out.print("Enter Picture URL: ");
        String pictureURL = scanner.nextLine();
        System.out.print("Enter Inventory: ");
        int inventory = scanner.nextInt();
        scanner.nextLine(); // consume newline

        Book book = new Book(isbn, title, author, publisher, description, pictureURL, inventory);
        bookstore.getBooks().add(book); // add to the bookstore's book list
        System.out.println("Book added successfully!");
    }

    private void editBook() {
        System.out.print("Enter ISBN of the book to edit: ");
        String isbn = scanner.nextLine();
        for (Book book : bookstore.getBooks()) {
            if (book.getIsbn().equals(isbn)) {
                System.out.print("Enter new Title (or press enter to keep current): ");
                String title = scanner.nextLine();
                if (!title.isEmpty()) book.setTitle(title);

                System.out.print("Enter new Author (or press enter to keep current): ");
                String author = scanner.nextLine();
                if (!author.isEmpty()) book.setAuthor(author);

                System.out.print("Enter new Publisher (or press enter to keep current): ");
                String publisher = scanner.nextLine();
                if (!publisher.isEmpty()) book.setPublisher(publisher);

                System.out.print("Enter new Description (or press enter to keep current): ");
                String description = scanner.nextLine();
                if (!description.isEmpty()) book.setDescription(description);

                System.out.print("Enter new Picture URL (or press enter to keep current): ");
                String pictureURL = scanner.nextLine();
                if (!pictureURL.isEmpty()) book.setpictureURL(pictureURL);

                System.out.print("Enter new Inventory (or press enter to keep current): ");
                String inventoryInput = scanner.nextLine();
                if (!inventoryInput.isEmpty()) book.setInventory(Integer.parseInt(inventoryInput));

                System.out.println("Book updated successfully!");
                return;
            }
        }
        System.out.println("Book not found.");
    }

    private void viewAllBooks() {
        System.out.println("\nList of Books:");
        for (Book book : bookstore.getBooks()) {
            System.out.println("ISBN: " + book.getIsbn() + ", Title: " + book.getTitle() + ", Author: " + book.getAuthor() + ", Inventory: " + book.getInventory());
        }
    }

    private void searchBooks(User user) {
        System.out.print("Enter title to search: ");
        String title = scanner.nextLine();
        List<Book> results = user.searchBooksByTitle(title);
        
        System.out.println("\nSearch Results:");
        for (Book book : results) {
            System.out.println("ISBN: " + book.getIsbn() + ", Title: " + book.getTitle() + ", Author: " + book.getAuthor() + ", Inventory: " + book.getInventory());
        }
    }

    private void addToCart(User user) {
        System.out.print("Enter ISBN of the book to add: ");
        String isbn = scanner.nextLine();
        for (Book book : bookstore.getBooks()) {
            if (book.getIsbn().equals(isbn)) {
                System.out.print("Enter quantity: ");
                int quantity = scanner.nextInt();
                scanner.nextLine(); // consume newline
                user.addToCart(book, quantity);
                System.out.println("Added to cart!");
                return;
            }
        }
        System.out.println("Book not found.");
    }

    private void removeFromCart(User user) {
        System.out.print("Enter ISBN of the book to remove: ");
        String isbn = scanner.nextLine();
        for (Book book : bookstore.getBooks()) {
            if (book.getIsbn().equals(isbn)) {
                System.out.print("Enter quantity: ");
                int quantity = scanner.nextInt();
                scanner.nextLine(); // consume newline
                user.removeFromCart(book, quantity);
                System.out.println("Removed from cart!");
                return;
            }
        }
        System.out.println("Book not found.");
    }
}

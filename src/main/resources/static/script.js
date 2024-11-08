const apiUrl = '/api';
let cart = [];

let currentUserId = 1; // Default to customer user ID
let currentUserRole = 'customer'; // Default role

const customerUserId = 1; // ID of the customer user
const adminUserId = 2;    // ID of the admin user

document.addEventListener('DOMContentLoaded', () => {
    // Existing event listeners
    document.getElementById('searchType').addEventListener('change', updateSearchInputs);
    document.getElementById('searchBtn').addEventListener('click', searchBooks);
    document.getElementById('viewCartBtn').addEventListener('click', viewCart);
    document.getElementById('roleSwitchBtn').addEventListener('click', switchRole);

    // Load initial content based on role
    updateRoleView();
});

function switchRole() {
    if (currentUserRole === 'customer') {
        currentUserId = adminUserId;
        currentUserRole = 'admin';
        document.getElementById('roleSwitchBtn').textContent = 'Switch to Customer View';
    } else {
        currentUserId = customerUserId;
        currentUserRole = 'customer';
        document.getElementById('roleSwitchBtn').textContent = 'Switch to Admin View';
    }
    updateRoleView();
}

function updateRoleView() {
    const cartButton = document.getElementById('viewCartBtn');

    if (currentUserRole === 'admin') {
        // Hide customer-specific elements
        cartButton.style.display = 'none';
        loadAdminView();
    } else {
        // Show customer-specific elements
        cartButton.style.display = 'inline-block';
        loadBooks();
    }
}

function loadBooks() {
    fetch(`${apiUrl}/books`)
        .then(response => response.json())
        .then(books => {
            displayBooks(books);
        })
        .catch(error => {
            console.error('Error fetching books:', error);
        });
}

function updateSearchInputs() {
    const searchType = document.getElementById('searchType').value;
    const searchInput = document.getElementById('searchInput');
    const minValue = document.getElementById('minValue');
    const maxValue = document.getElementById('maxValue');

    // Reset input fields
    searchInput.style.display = 'none';
    minValue.style.display = 'none';
    maxValue.style.display = 'none';

    if (searchType === 'title' || searchType === 'author' || searchType === 'publisher') {
        searchInput.placeholder = `Enter ${searchType} keyword...`;
        searchInput.style.display = 'inline-block';
    } else if (searchType === 'price' || searchType === 'inventory') {
        minValue.style.display = 'inline-block';
        if (searchType === 'price') {
            maxValue.style.display = 'inline-block';
            minValue.placeholder = 'Min price';
            maxValue.placeholder = 'Max price';
        } else {
            minValue.placeholder = 'Min inventory';
        }
    }
}

function searchBooks() {
    const searchType = document.getElementById('searchType').value;
    const searchInput = document.getElementById('searchInput').value.trim();
    const minValue = document.getElementById('minValue').value;
    const maxValue = document.getElementById('maxValue').value;
    let endpoint;

    if (searchType === 'title') {
        endpoint = `${apiUrl}/books/search?keyword=${encodeURIComponent(searchInput)}`;
    } else if (searchType === 'author') {
        endpoint = `${apiUrl}/books/search/author?author=${encodeURIComponent(searchInput)}`;
    } else if (searchType === 'publisher') {
        endpoint = `${apiUrl}/books/search/publisher?publisher=${encodeURIComponent(searchInput)}`;
    } else if (searchType === 'price') {
        endpoint = `${apiUrl}/books/filter/price?minPrice=${encodeURIComponent(minValue)}&maxPrice=${encodeURIComponent(maxValue)}`;
    } else if (searchType === 'inventory') {
        endpoint = `${apiUrl}/books/filter/inventory?minInventory=${encodeURIComponent(minValue)}`;
    } else {
        alert('Invalid search type selected.');
        return;
    }

    fetch(endpoint)
        .then(response => {
            if (!response.ok) {
                throw new Error('Search failed');
            }
            return response.json();
        })
        .then(books => {
            displayBooks(books);
        })
        .catch(error => {
            console.error('Error:', error);
            alert('An error occurred while searching for books.');
        });
}

function displayBooks(books) {
    const content = document.getElementById('content');
    content.innerHTML = '';

    if (books.length === 0) {
        content.innerHTML = '<p>No books found.</p>';
        return;
    }

    books.forEach(book => {
        const bookDiv = document.createElement('div');
        bookDiv.className = 'book';

        const author = book.author || 'Unknown Author';
        const price = book.price !== undefined ? `$${book.price.toFixed(2)}` : 'Price not available';
        const inventoryText = book.inventory > 0 ? `In Stock: ${book.inventory}` : 'Out of Stock';

        // Only show "Add to Cart" button for customers
        const addToCartButton = currentUserRole === 'customer' && book.inventory > 0
            ? `<button class="addToCartBtn" data-id="${book.id}">Add to Cart</button>`
            : '';

        // Only show edit and delete buttons for admins
        const adminButtons = currentUserRole === 'admin'
            ? `
                <button class="editBookBtn" data-id="${book.id}">Edit</button>
                <button class="deleteBookBtn" data-id="${book.id}">Delete</button>
              `
            : '';

        bookDiv.innerHTML = `
            <h2>${book.title}</h2>
            <p><strong>Author:</strong> ${author}</p>
            <p>${book.description}</p>
            <p><strong>Price:</strong> ${price}</p>
            <p><strong>Inventory:</strong> ${inventoryText}</p>
            ${addToCartButton}
            ${adminButtons}
        `;

        content.appendChild(bookDiv);
    });

    // Attach event listeners based on role
    if (currentUserRole === 'customer') {
        document.querySelectorAll('.addToCartBtn').forEach(button => {
            button.addEventListener('click', addToCart);
        });
    }

    if (currentUserRole === 'admin') {
        document.querySelectorAll('.editBookBtn').forEach(button => {
            button.addEventListener('click', editBook);
        });
        document.querySelectorAll('.deleteBookBtn').forEach(button => {
            button.addEventListener('click', deleteBook);
        });
    }
}

function loadAdminView() {
    fetch(`${apiUrl}/books`)
        .then(response => response.json())
        .then(books => {
            displayBooks(books);
            // Add a button to upload a new book
            const content = document.getElementById('content');
            const uploadButton = document.createElement('button');
            uploadButton.textContent = 'Add New Book';
            uploadButton.addEventListener('click', showAddBookForm);
            content.prepend(uploadButton);
        })
        .catch(error => {
            console.error('Error fetching books:', error);
        });
}

function showAddBookForm() {
    const content = document.getElementById('content');
    content.innerHTML = `
        <h2>Add New Book</h2>
        <form id="addBookForm">
            <label>ISBN: <input type="text" name="isbn" required></label><br>
            <label>Title: <input type="text" name="title" required></label><br>
            <label>Author: <input type="text" name="author" required></label><br>
            <label>Description: <textarea name="description"></textarea></label><br>
            <label>Publisher: <input type="text" name="publisher"></label><br>
            <label>Picture URL: <input type="text" name="pictureURL"></label><br>
            <label>Price: <input type="number" step="0.01" name="price" required></label><br>
            <label>Inventory: <input type="number" name="inventory" required></label><br>
            <button type="submit">Add Book</button>
        </form>
    `;

    document.getElementById('addBookForm').addEventListener('submit', submitNewBook);
}

function submitNewBook(event) {
    event.preventDefault();
    const formData = new FormData(event.target);
    const bookData = {};
    formData.forEach((value, key) => {
        bookData[key] = key === 'price' || key === 'inventory' ? Number(value) : value;
    });

    fetch(`${apiUrl}/books?userId=${currentUserId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(bookData)
    })
        .then(response => {
            if (response.ok) {
                alert('Book added successfully!');
                loadAdminView();
            } else {
                return response.text().then(text => { throw new Error(text); });
            }
        })
        .catch(error => {
            console.error('Error adding book:', error);
            alert('An error occurred while adding the book: ' + error.message);
        });
}

function editBook(event) {
    const bookId = event.target.getAttribute('data-id');
    fetch(`${apiUrl}/books/${bookId}`)
        .then(response => response.json())
        .then(book => {
            const content = document.getElementById('content');
            content.innerHTML = `
                <h2>Edit Book</h2>
                <form id="editBookForm">
                    <input type="hidden" name="id" value="${book.id}">
                    <label>ISBN: <input type="text" name="isbn" value="${book.isbn}" required></label><br>
                    <label>Title: <input type="text" name="title" value="${book.title}" required></label><br>
                    <label>Author: <input type="text" name="author" value="${book.author}" required></label><br>
                    <label>Description: <textarea name="description">${book.description}</textarea></label><br>
                    <label>Publisher: <input type="text" name="publisher" value="${book.publisher}"></label><br>
                    <label>Picture URL: <input type="text" name="pictureURL" value="${book.pictureURL}"></label><br>
                    <label>Price: <input type="number" step="0.01" name="price" value="${book.price}" required></label><br>
                    <label>Inventory: <input type="number" name="inventory" value="${book.inventory}" required></label><br>
                    <button type="submit">Update Book</button>
                </form>
            `;

            document.getElementById('editBookForm').addEventListener('submit', submitEditBook);
        })
        .catch(error => {
            console.error('Error fetching book data:', error);
            alert('An error occurred while fetching the book details.');
        });
}

function submitEditBook(event) {
    event.preventDefault();
    const formData = new FormData(event.target);
    const bookData = {};
    formData.forEach((value, key) => {
        bookData[key] = key === 'price' || key === 'inventory' ? Number(value) : value;
    });
    const bookId = bookData.id;

    fetch(`${apiUrl}/books/${bookId}?userId=${currentUserId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(bookData)
    })
        .then(response => {
            if (response.ok) {
                alert('Book updated successfully!');
                loadAdminView();
            } else {
                return response.text().then(text => { throw new Error(text); });
            }
        })
        .catch(error => {
            console.error('Error updating book:', error);
            alert('An error occurred while updating the book: ' + error.message);
        });
}

function deleteBook(event) {
    const bookId = event.target.getAttribute('data-id');
    if (confirm('Are you sure you want to delete this book?')) {
        fetch(`${apiUrl}/books/${bookId}?userId=${currentUserId}`, {
            method: 'DELETE'
        })
            .then(response => {
                if (response.ok) {
                    alert('Book deleted successfully!');
                    loadAdminView();
                } else {
                    return response.text().then(text => { throw new Error(text); });
                }
            })
            .catch(error => {
                console.error('Error deleting book:', error);
                alert('An error occurred while deleting the book: ' + error.message);
            });
    }
}

function addToCart(event) {
    const bookId = event.target.getAttribute('data-id');

    fetch(`${apiUrl}/books/${bookId}`)
        .then(response => response.json())
        .then(book => {
            const existingItem = cart.find(item => item.bookId === bookId);
            const currentQuantityInCart = existingItem ? existingItem.quantity : 0;

            if (currentQuantityInCart < book.inventory) {
                if (existingItem) {
                    existingItem.quantity += 1;
                } else {
                    cart.push({ bookId, quantity: 1 });
                }
                updateCartCount();
            } else {
                alert('Cannot add more items than available in inventory.');
            }
        })
        .catch(error => {
            console.error('Error fetching book data:', error);
            alert('An error occurred while adding the item to the cart.');
        });
}

function updateCartCount() {
    document.getElementById('cartCount').textContent = cart.reduce((total, item) => total + item.quantity, 0);
}

function viewCart() {
    const content = document.getElementById('content');
    content.innerHTML = '<h2>Your Cart</h2>';

    if (cart.length === 0) {
        content.innerHTML += '<p>Your cart is empty.</p>';
        return;
    }

    const cartItemsContainer = document.createElement('div');
    content.appendChild(cartItemsContainer);

    const fetchPromises = cart.map(item => {
        return fetch(`${apiUrl}/books/${item.bookId}`)
            .then(response => response.json())
            .then(book => {
                const itemDiv = document.createElement('div');
                itemDiv.className = 'book';

                const price = book.price || 0;

                itemDiv.innerHTML = `
                    <h2>${book.title}</h2>
                    <p><strong>Quantity:</strong> ${item.quantity}</p>
                    <p><strong>Total Price:</strong> $${(price * item.quantity).toFixed(2)}</p>
                `;

                cartItemsContainer.appendChild(itemDiv);
            });
    });

    Promise.all(fetchPromises).then(() => {
        const checkoutBtn = document.createElement('button');
        checkoutBtn.textContent = 'Checkout';
        checkoutBtn.addEventListener('click', checkout);
        content.appendChild(checkoutBtn);
    });
}

function checkout() {
    // Validate cart quantities against inventory
    const validationPromises = cart.map(cartItem => {
        return fetch(`${apiUrl}/books/${cartItem.bookId}`)
            .then(response => response.json())
            .then(book => {
                if (cartItem.quantity > book.inventory) {
                    throw new Error(`Not enough inventory for ${book.title}. Available: ${book.inventory}, In Cart: ${cartItem.quantity}`);
                }
            });
    });

    Promise.all(validationPromises)
        .then(() => {
            return fetch(`${apiUrl}/purchase/checkout?userId=${currentUserId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(cart)
            });
        })
        .then(response => {
            if (response.ok) {
                alert('Checkout successful!');
                cart = [];
                updateCartCount();
                loadBooks();
            } else {
                return response.text().then(text => { throw new Error(text); });
            }
        })
        .catch(error => {
            console.error('Error during checkout:', error);
            alert(error.message);
        });
}

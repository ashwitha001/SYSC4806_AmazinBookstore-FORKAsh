const apiUrl = '/api';
let cart = [];

document.addEventListener('DOMContentLoaded', () => {
    loadBooks();

    document.getElementById('searchType').addEventListener('change', updateSearchInputs);
    document.getElementById('searchBtn').addEventListener('click', searchBooks);
    document.getElementById('viewCartBtn').addEventListener('click', viewCart);
});

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

        bookDiv.innerHTML = `
            <h2>${book.title}</h2>
            <p><strong>Author:</strong> ${author}</p>
            <p>${book.description}</p>
            <p><strong>Price:</strong> ${price}</p>
            <button class="addToCartBtn" data-id="${book.id}">Add to Cart</button>
        `;

        content.appendChild(bookDiv);
    });

    document.querySelectorAll('.addToCartBtn').forEach(button => {
        button.addEventListener('click', addToCart);
    });
}

function addToCart(event) {
    const bookId = event.target.getAttribute('data-id');
    const existingItem = cart.find(item => item.bookId === bookId);
    if (existingItem) {
        existingItem.quantity += 1;
    } else {
        cart.push({ bookId, quantity: 1 });
    }
    updateCartCount();
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
    const userId = 1; // Placeholder for user ID
    fetch(`${apiUrl}/purchase/checkout?userId=${userId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(cart)
    })
        .then(response => {
            if (response.ok) {
                alert('Checkout successful!');
                cart = [];
                updateCartCount();
                loadBooks();
            } else {
                alert('Checkout failed.');
            }
        })
        .catch(error => {
            console.error('Error during checkout:', error);
            alert('An error occurred during checkout.');
        });
}

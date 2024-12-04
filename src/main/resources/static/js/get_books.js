document.addEventListener("DOMContentLoaded", () => {
    document.body.addEventListener('click', function (event) {
        console.log("Clicked element:", event.target);

        if (event.target && event.target.classList.contains('delete-btn')) {
            const deleteButton = event.target;
            const bookId = deleteButton.getAttribute('data-book-id');

            if (bookId) {
                if (confirm('Are you sure you want to delete this book?')) {
                    const xhr = new XMLHttpRequest();

                    xhr.open('DELETE', `/delete-book/${bookId}`, true);

                    xhr.onload = function () {
                        console.log("Request finished with status:", xhr.status);
                        let response;

                        try {
                            response = JSON.parse(xhr.responseText);
                            console.log("Response parsed:", response);

                            if (response.success) {
                                // Set flag in sessionStorage to indicate success
                                sessionStorage.setItem('delete_book_success', 'true');
                                location.reload(); // Reload the page after successful deletion
                            } else {
                                showAlert('Error', response.message, 'error'); // Show error alert box
                            }
                        } catch (error) {
                            showAlert('Error', "Invalid server response. Please try again.", 'error');
                        }
                    };

                    // Send the request
                    xhr.send();
                }
            } else {
                console.error("Book ID not found on delete button.");
            }
        }
    });

    // Show success or error alert box after reload
    if (sessionStorage.getItem('delete_book_success') === 'true') {
        showAlert('Success', 'The book has been successfully deleted.', 'success');
        sessionStorage.removeItem('delete_book_success'); // Reset the flag after showing the alert
    }
});

// Function to show alert box (success or error)
function showAlert(title, message, type) {
    const alertBox = document.createElement('div');
    alertBox.classList.add('alertbox', type); // Add type class for success or error

    const titleElement = document.createElement('strong');
    titleElement.textContent = title; // Title for the alert box
    alertBox.appendChild(titleElement);

    const messageElement = document.createElement('p');
    messageElement.textContent = message; // The actual message
    alertBox.appendChild(messageElement);

    document.body.appendChild(alertBox);

    // Show the alert box immediately
    setTimeout(() => {
        alertBox.classList.add('show');
    }, 10); // A slight delay to ensure animation

    // Hide the alert box after 5 seconds
    setTimeout(() => {
        alertBox.classList.remove('show');
        alertBox.remove();
    }, 5000); // Alert stays for 5 seconds
}

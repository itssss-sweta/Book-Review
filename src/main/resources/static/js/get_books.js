document.addEventListener("DOMContentLoaded", () => {
    document.body.addEventListener('click', (event) => {
        console.log("Clicked element:", event.target);

        // Check if the clicked element is a delete button
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

        // Check if the clicked element is an update button
        if (event.target && event.target.classList.contains('update-btn')) {
            const updateButton = event.target;
            const bookId = updateButton.getAttribute('data-book-id');

            if (bookId) {
                window.location.href = `modifyBook/${bookId}`;
            } else {
                window.location.href = 'modifyBook';
            }
        }
    });

    // Show success or error alert box after reload
    if (sessionStorage.getItem('delete_book_success') === 'true') {
        showAlert('Success', 'The book has been successfully deleted.', 'success');
        sessionStorage.removeItem('delete_book_success'); 
    }
});

// Function to show alert box (success or error)
function showAlert(title, message, type) {
    const alertBox = document.createElement('div');
    alertBox.classList.add('alertbox', type);

    const titleElement = document.createElement('strong');
    titleElement.textContent = title; 
    alertBox.appendChild(titleElement);

    const messageElement = document.createElement('p');
    messageElement.textContent = message;
    alertBox.appendChild(messageElement);

    document.body.appendChild(alertBox);

    setTimeout(() => {
        alertBox.classList.add('show');
    }, 10); 

    setTimeout(() => {
        alertBox.classList.remove('show');
        alertBox.remove();
    }, 5000); 
}

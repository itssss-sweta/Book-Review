document.addEventListener("DOMContentLoaded", () => {
    setupUpdateButtons();
    setupDeleteButtons();
    handleSuccessMessages();
});

// Set up event listeners for update buttons
function setupUpdateButtons() {
    document.querySelectorAll('.btn.update').forEach(button => {
        button.addEventListener('click', () => {
            const genreId = button.getAttribute('data-genre-id'); // Ensure genreId is defined first
            const genreInput = document.getElementById('genre-input-' + genreId);
            const action = button.getAttribute('update-action');

            if (action === 'Done') updateGenre(genreId, genreInput.value);
            else toggleUpdate(genreId);
        });
    });
}


// Set up event listeners for delete/cancel buttons
function setupDeleteButtons() {
    document.querySelectorAll('.btn.delete').forEach(button => {
        button.addEventListener('click', () => {
            const genreId = button.getAttribute('data-genre-id');
            const action = button.getAttribute('data-action');
            if (action === 'Cancel') revertToInitial(genreId,"");
            else deleteGenre(genreId);
        });
    });
}

// Handle success messages on reload
function handleSuccessMessages() {
    if (sessionStorage.getItem('update_genre_success') === 'true') {
        showAlert('Success', 'The genre has been successfully updated.', 'success');
        sessionStorage.removeItem('update_genre_success');
    } else if (sessionStorage.getItem('delete_genre_success') === 'true') {
        showAlert('Success', 'The genre has been successfully deleted.', 'success');
        sessionStorage.removeItem('delete_genre_success');
    }
}


// Toggle between view and edit modes
function toggleUpdate(genreId) {
    const genreName = document.getElementById('genre-name-' + genreId);
    const genreInput = document.getElementById('genre-input-' + genreId);
    const updateBtn = document.querySelector('.btn.update[data-genre-id="' + genreId + '"]');
    const deleteBtn = document.querySelector('.btn.delete[data-genre-id="' + genreId + '"]');

    if (genreInput.style.display === 'none') {
        genreInput.style.display = 'inline-block';
        genreInput.value = genreName.textContent; 
        genreName.style.display = 'none';
        deleteBtn.textContent = 'Cancel';
        deleteBtn.setAttribute('data-action', 'Cancel');
        updateBtn.setAttribute('update-action', 'Done');
        updateBtn.textContent = 'Done';
    } else {
        genreName.textContent = genreInput.value;
        genreInput.style.display = 'none';
        genreName.style.display = 'inline-block';
        deleteBtn.textContent = 'Delete';
        deleteBtn.setAttribute('data-action', 'Delete');
        updateBtn.setAttribute('update-action', 'Update');
        updateBtn.textContent = 'Update';
    }
}

// Cancel editing and revert to view mode
function revertToInitial(genreId,updatedGenreName) {
    const genreName = document.getElementById('genre-name-' + genreId);
    const genreInput = document.getElementById('genre-input-' + genreId);
    const updateBtn = document.querySelector('.btn.update[data-genre-id="' + genreId + '"]');
    const deleteBtn = document.querySelector('.btn.delete[data-genre-id="' + genreId + '"]');

    if (updatedGenreName && updatedGenreName.trim() !== "") {
        genreName.textContent = updatedGenreName;  
    }
    genreInput.style.display = 'none';
    genreName.style.display = 'inline-block';
    deleteBtn.textContent = 'Delete';
    deleteBtn.setAttribute('data-action', 'Delete');
    updateBtn.setAttribute('update-action', 'Delete');
    updateBtn.textContent = 'Update';
}

// Send update request to server
function updateGenre(genreId, updatedGenreName) {
    if (updatedGenreName.trim() !== "") {
            const xhr = new XMLHttpRequest();
            xhr.open('PUT', `/update-genre/${genreId}`, true);
            xhr.setRequestHeader('Content-Type', 'application/json');

            xhr.onload = function () {
                try {
                    const response = JSON.parse(xhr.responseText);
                    if (response.success) {
                        console.log("update");
                        sessionStorage.setItem('update_genre_success', 'true');
                        location.reload();
                        revertToInitial(genreId,updatedGenreName);
                    } else {
                        showAlert('Error', response.message, 'error');
                    }
                } catch {
                    showAlert('Error', "Invalid server response. Please try again.", 'error');
                }
            };

            xhr.onerror = () => showAlert('Error', "There was an error with the request.", 'error');
            xhr.send(JSON.stringify({ name: updatedGenreName}));
    } else {
        alert('Please enter a valid genre name.');
    }
}

// Send delete request to server
function deleteGenre(genreId) {
    if (confirm('Are you sure you want to delete this genre?')) {
        const xhr = new XMLHttpRequest();
        xhr.open('DELETE', `/delete-genre/${genreId}`, true);

        xhr.onload = function () {
            try {
                const response = JSON.parse(xhr.responseText);
                if (response.success) {
                    sessionStorage.setItem('delete_genre_success', 'true');
                    location.reload();
                } else {
                    showAlert('Error', response.message, 'error');
                }
            } catch {
                showAlert('Error', "Invalid server response. Please try again.", 'error');
            }
        };

        xhr.onerror = () => showAlert('Error', "Request failed. Check your connection.", 'error');
        xhr.send();
    }
}

// Display success or error alerts
function showAlert(title, message, type) {
    const alertBox = document.createElement('div');
    alertBox.classList.add('alertbox', type);
    alertBox.setAttribute('role', 'alert');
    alertBox.setAttribute('aria-live', 'assertive');

    const titleElement = document.createElement('strong');
    titleElement.textContent = title;
    alertBox.appendChild(titleElement);

    const messageElement = document.createElement('p');
    messageElement.textContent = message;
    alertBox.appendChild(messageElement);

    document.body.appendChild(alertBox);

    setTimeout(() => alertBox.classList.add('show'), 10);
    setTimeout(() => {
        alertBox.classList.remove('show');
        alertBox.remove();
    }, 5000);
}

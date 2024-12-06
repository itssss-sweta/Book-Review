document.addEventListener("DOMContentLoaded", () => {
    document.body.addEventListener('click', function (event) {
        console.log("Clicked element:", event.target);

        if (event.target && event.target.classList.contains('delete')) {
            const deleteButton = event.target;
            const genreId = deleteButton.getAttribute('data-genre-id');

            if (genreId) {
                if (confirm('Are you sure you want to delete this genre?')) {
                    const xhr = new XMLHttpRequest();

                    xhr.open('DELETE', `/delete-genre/${genreId}`, true);

                    xhr.onload = function () {
                        console.log("Request finished with status:", xhr.status);
                        let response;

                        try {
                            response = JSON.parse(xhr.responseText);
                            console.log("Response parsed:", response);

                            if (response.success) {
                                sessionStorage.setItem('delete_genre_success', 'true');
                                location.reload();
                            } else {
                                showAlert('Error', response.message, 'error'); 
                            }
                        } catch (error) {
                            showAlert('Error', "Invalid server response. Please try again.", 'error');
                        }
                    };
                    
                    // Send the request
                    xhr.send();
                }
            } else {
                console.error("Genre ID not found on delete button.");
                showAlert('Error', "IGenre ID not found. Please try again.", 'error');
            }
        }
    });

    // Show success or error alert box after reload
    if (sessionStorage.getItem('delete_genre_success') === 'true') {
        showAlert('Success', 'The genre has been successfully deleted.', 'success');
        sessionStorage.removeItem('delete_genre_success'); 
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

document.addEventListener("DOMContentLoaded", function() {
    const updateButtons = document.querySelectorAll('.btn.update');
    updateButtons.forEach(button => {
        button.addEventListener('click', function() {
            const genreId = button.getAttribute('data-genre-id');
            toggleUpdate(genreId);
        });
    });
});


function toggleUpdate(genreId) {
    const genreName = document.getElementById('genre-name-' + genreId);
    const genreInput = document.getElementById('genre-input-' + genreId);
    const updateBtn = document.querySelector('.btn.update[data-genre-id="' + genreId + '"]');
    const deleteBtn = document.querySelector('.btn.delete[data-genre-id="' + genreId + '"]');
    
    if (genreInput.style.display === 'none') {
        genreInput.style.display = 'inline-block';  
        genreName.style.display = 'none'; 
        deleteBtn.style.display = 'none';  
        updateBtn.textContent = 'Done';  
    } else {
        genreName.textContent = genreInput.value; 
        genreInput.style.display = 'none';  
        genreName.style.display = 'inline-block';  
        deleteBtn.style.display = 'inline-block';  
        updateBtn.textContent = 'Update';  

        alert('Update Successful');
    }
}
const selectedGenres = new Set(); // Using a Set to store unique genre IDs

function updateSelectedGenres() {
    const genreDropdown = document.getElementById("genres");
    const selectedGenreId = genreDropdown.value;
    const selectedGenreText = genreDropdown.options[genreDropdown.selectedIndex].text;

    if (selectedGenreId && !selectedGenres.has(selectedGenreId)) {
        // Add the selected genre ID to the Set
        selectedGenres.add(selectedGenreId);

        // Create and display the selected genre element
        const selectedGenresContainer = document.getElementById("selected-genres");
        const genreElement = document.createElement("div");
        genreElement.classList.add("selected-genre");
        genreElement.innerHTML = `
            <span>${selectedGenreText}</span>
            <span class="remove" onclick="removeGenre('${selectedGenreId}')">x</span>
        `;
        genreElement.dataset.genreId = selectedGenreId;
        selectedGenresContainer.appendChild(genreElement);
    }

    // Update the hidden input field with all selected genre IDs
    updateHiddenInput();
}

function removeGenre(genreId) {
    // Remove the genre from the Set
    selectedGenres.delete(genreId);

    // Remove the genre element from the display
    const selectedGenresContainer = document.getElementById("selected-genres");
    const genreElements = selectedGenresContainer.getElementsByClassName("selected-genre");
    for (const genreElement of genreElements) {
        if (genreElement.dataset.genreId === genreId) {
            selectedGenresContainer.removeChild(genreElement);
            break;
        }
    }

    // Update the hidden input field after removal
    updateHiddenInput();
}

function updateHiddenInput() {
    // Join the Set values (genre IDs) into a comma-separated string
    const hiddenInput = document.getElementById("selected-genre-ids");
    hiddenInput.value = Array.from(selectedGenres).join(",");
}


function previewImage(event) {
    const file = event.target.files[0];
    const preview = document.getElementById('image-preview');
    const placeholder = document.getElementById('image-placeholder');
    const container = document.querySelector('.image-upload-container');
    const note = document.getElementById('file-format-note'); // Error message element
    
    // Check if a file is selected and its format
    if (file) {
        // Check if the file is a .jpg or .jpeg image
        if (file.type === 'image/jpeg' || file.name.endsWith('.jpg')) {
            const reader = new FileReader();
            reader.onload = function (e) {
                preview.src = e.target.result;
                preview.style.display = 'block';
                preview.style.opacity = '1';
                placeholder.classList.add('hidden');
                container.classList.add('image-selected');
                note.style.display = 'none'; // Hide the error note if the format is valid
            };
            reader.readAsDataURL(file);
        } else {
            // Display error message for unsupported file types
            note.style.display = 'block';
            preview.style.display = 'none'; // Hide the preview if the format is invalid
            placeholder.classList.remove('hidden'); // Show the placeholder
            container.classList.remove('image-selected'); // Remove selected image styles
        }
    }
}


document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("bookForm");
    if (form) {
        form.addEventListener("submit", function (e) {
            e.preventDefault(); // Prevent default form submission
            console.log("POSTING");

            // Clear any existing response messages
            const responseMessageDiv = document.getElementById("responseMessage");
            if (responseMessageDiv) responseMessageDiv.innerHTML = "";

            // Create an XMLHttpRequest object
            const xhr = new XMLHttpRequest();
            xhr.open("POST", "/post-book", true);

            // Set up a handler for the response
            xhr.onload = function () {
                const contentType = xhr.getResponseHeader("Content-Type");
                let response;

                try {
                    response = JSON.parse(xhr.responseText);
                    console.log("Response parsed:", response);
            
                    if (response.success) {
                        document.getElementById('responseMessage').innerHTML = `<div class="success">${response.message}</div>`;
                        window.location.href = "/dashboard";
                    } else {
                        document.getElementById('responseMessage').innerHTML = `<div class="error">${response.message}</div>`;
                    }
                } catch (error) {
                    console.error("Failed to parse JSON response:",error);
                    document.getElementById('responseMessage').innerHTML = `<div class="error">Invalid server response. Please try again.</div>`;
                }
            };

            // Handle network errors
            xhr.onerror = function () {
                responseMessageDiv.innerHTML = `<div class="error">Network error occurred. Please try again later.</div>`;
            };

            // Collect the form data
            const formData = new FormData(form);
            xhr.send(formData); // Send the request with the form data
        });
    } else {
        console.error("Form with ID 'bookForm' not found");
    }
});

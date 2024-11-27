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

    if (file) {
        const reader = new FileReader();
        reader.onload = function (e) {
            preview.src = e.target.result;
            preview.style.display = 'block';
            preview.style.opacity = '1';
            placeholder.classList.add('hidden');
            container.classList.add('image-selected');
        };
        reader.readAsDataURL(file);
    }
}

document.addEventListener("DOMContentLoaded", function () {
    const alertMessage = /*[[${alert}]]*/ ''; // Get alert message from model

    if (alertMessage) {
        alert(alertMessage); // Show an alert based on the message
    }
});

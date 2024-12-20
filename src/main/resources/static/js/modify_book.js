const selectedGenres = new Set(); 

function updateSelectedGenres() {
    const genreDropdown = document.getElementById("genres");
    const selectedGenreId = genreDropdown.value;
    const selectedGenreText = genreDropdown.options[genreDropdown.selectedIndex].text;

    if (selectedGenreId && !selectedGenres.has(selectedGenreId)) {
        selectedGenres.add(selectedGenreId);

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
    const hiddenInput = document.getElementById("selected-genre-ids");
    hiddenInput.value = Array.from(selectedGenres).join(",");
}


function previewImage(event) {
    const file = event.target.files[0];
    const preview = document.getElementById('image-preview');
    const placeholder = document.getElementById('image-placeholder');
    const container = document.querySelector('.image-upload-container');
    const note = document.getElementById('file-format-note');
    
    if (file) {
        if (file.type === 'image/jpeg' || file.name.endsWith('.jpg')) {
            const reader = new FileReader();
            reader.onload = function (e) {
                preview.src = e.target.result;
                preview.style.display = 'block';
                preview.style.opacity = '1';
                placeholder.classList.add('hidden');
                container.classList.add('image-selected');
                note.style.display = 'none';
            };
            reader.readAsDataURL(file);
        } else {
            note.style.display = 'block';
            preview.style.display = 'none'; 
            placeholder.classList.remove('hidden'); 
            container.classList.remove('image-selected'); 
        }
    }
}


document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("bookForm");
    if (form) {
        form.addEventListener("submit", function (e) {
            e.preventDefault(); 
            console.log("POSTING");

            const xhr = new XMLHttpRequest();
            xhr.open("POST", "/post-book", true);

            xhr.onload = function () {
                const contentType = xhr.getResponseHeader("Content-Type");
                let response;

                try {
                    response = JSON.parse(xhr.responseText);
                    console.log("Response parsed:", response);
            
                    if (response.success) {
                        alert(response.message);
                        window.location.href = "/dashboard";
                    } else {
                        alert(response.message);
                    }
                } catch (error) {
                    alert("Invalid server response. Please try again.");
                }
            };

            xhr.onerror = function () {
               alert("Network error occurred. Please try again later.");
            };

            const formData = new FormData(form);
            xhr.send(formData); 
        });
    } else {
        console.error("Form with ID 'bookForm' not found");
    }
});

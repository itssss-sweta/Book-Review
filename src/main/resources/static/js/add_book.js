function updateSelectedGenres() {
    var genreSelect = document.getElementById('genres');
    var selectedGenre = genreSelect.options[genreSelect.selectedIndex].text;

    // Create a new div for the selected genre
    var newGenre = document.createElement('div');
    newGenre.className = 'selected-genre';
    newGenre.innerHTML = selectedGenre + '<span class="remove" onclick="removeGenre(this)">x</span>';

    // Append the new genre div to the selected-genres container
    document.getElementById('selected-genres').appendChild(newGenre);
}

function removeGenre(genreElement) {
    genreElement.parentElement.remove();
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


document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("genreForm");
    if (form) {
        form.addEventListener("submit", function (e) {
            e.preventDefault();
            const xhr = new XMLHttpRequest();
            xhr.open("POST", "/post-genre", true);
            xhr.setRequestHeader("Content-Type", "application/json"); 

            xhr.onload = function () {
                console.log("Request finished with status:", xhr.status);
                const contentType = xhr.getResponseHeader("Content-Type");
                let response;

                try {
                    response = JSON.parse(xhr.responseText);
                    console.log("Response parsed:", response);
            
                    alert(response.message);
                    if (response.success) {
                        window.location.href = "/dashboard";
                    }
                } catch (error) {
                    alert("Invalid server response. Please try again.");
                }
            };

            xhr.onerror = function () {
               alert("Network error occurred. Please try again later.");
            };

            const formData = new FormData(form);
            const jsonObject = {};
            formData.forEach((value, key) => {
                jsonObject[key] = value;
            });

            xhr.send(JSON.stringify(jsonObject));
        });
    } else {
        console.error("Form with ID 'genreForm' not found");
    }
});
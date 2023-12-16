const urlParams = new URLSearchParams(window.location.search);
// Использовать функцию для получения токена из хранилища:
const token = urlParams.get('token');
console.log(token);

document.getElementById("uploadForm").addEventListener("submit", function (event) {
    event.preventDefault();
    const form = event.target;
    const formData = new FormData(form);

    // Заменить значение кнопки на "Loading..."
    const uploadButton = form.querySelector('input[type="submit"]');
    uploadButton.value = "Loading...";

    fetch(`http://localhost:8080/upload`, {
        method: "POST",
        body: formData,
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
        .then(response => {
            if (response.ok) {
                return response.text();
            } else if (response.status === 401) {
                throw new Error("Unauthorized");
            } else {
                throw new Error("Network response was not ok.");
            }
        })
        .then(videoId => {
            window.location.href = `fourth.html?token=${token}&id=${videoId}`;
        })
        .catch(error => {
            if (error.message === "Unauthorized") {
                window.location.href = "first.html";
            } else {
                console.error(error);
            }
        });
});
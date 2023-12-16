const urlParams = new URLSearchParams(window.location.search);
const mode = urlParams.get('mode'); // Получаем режим из URL-параметров

document.getElementById("submitBtn").addEventListener("click", function () {
    const login = document.getElementById("loginInput").value;
    const password = document.getElementById("passwordInput").value;

    let data = {
        "login": login,
        "password": password
    };

    const jsonData = JSON.stringify(data);

    if (mode === "registration") {
        // Отправляем POST-запрос на сервер для регистрации
        postData("http://localhost:8080/register", jsonData);
    } else if (mode === "login") {
        // Отправляем POST-запрос на сервер для авторизации
        postData("http://localhost:8080/login", jsonData);
    }
});

function postData(url, data) {
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: data
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else if (response.status === 400 || response.status === 409) {
                throw new Error('Bad Request');
            } else {
                throw new Error('Unexpected error');
            }
        })
        .then(result => {
            // Получаем токен из результата
            const token = result.token;
            console.log(token);
            
            // Переходим на страницу third.html без передачи токена в URL-параметрах
            window.location.href = `third.html?token=${token}`;
        })
        .catch(error => {
            if (error.message === 'Bad Request') {
                window.location.href = 'first.html';
            } else {
                console.error(error);
            }
        });
}
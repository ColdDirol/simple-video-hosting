document.getElementById("registrationBtn").addEventListener("click", function () {
    // При нажатии кнопки "Регистрация" переходим на страницу second.html с режимом регистрации
    window.location.href = "second.html?mode=registration";
});

document.getElementById("loginBtn").addEventListener("click", function () {
    // При нажатии кнопки "Авторизация" переходим на страницу second.html с режимом авторизации
    window.location.href = "second.html?mode=login";
});
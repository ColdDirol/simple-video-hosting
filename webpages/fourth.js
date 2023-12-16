const videoPlayer = document.getElementById("videoPlayer");

const urlParams = new URLSearchParams(window.location.search);
// Использовать функцию для получения токена из хранилища:
const token = urlParams.get('token');
const videoId = urlParams.get("id"); // Получаем id из URL-параметров

if (!videoId || !token) {
    alert("Invalid video ID or user TOKEN");
} else {
    const videoUrl = `http://localhost:8080/stream/${videoId}`;
    const sourceElement = document.createElement("source");
    sourceElement.src = videoUrl;
    sourceElement.type = "video/mp4";
    videoPlayer.appendChild(sourceElement);
    videoPlayer.load();
}

videoPlayer.addEventListener("error", function () {
    const errorCode = videoPlayer.error.code;
    if (errorCode === MediaError.MEDIA_ERR_SRC_NOT_SUPPORTED) {
        // Обработка ошибок (Not found и Unauthorizaed)
        window.location.href = `first.html`;
    } else {
        console.error("Video playback error:", videoPlayer.error);
    }
});
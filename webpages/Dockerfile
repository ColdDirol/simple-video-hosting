# Используем официальный образ nginx
FROM nginx

# Копируем статические файлы
COPY first.html /usr/share/nginx/html/first.html
COPY second.html /usr/share/nginx/html/second.html
COPY third.html /usr/share/nginx/html/third.html
COPY fourth.html /usr/share/nginx/html/fourth.html

# Копируем JS файлы
COPY first.js /usr/share/nginx/html/first.js
COPY second.js /usr/share/nginx/html/second.js
COPY third.js /usr/share/nginx/html/third.js
COPY fourth.js /usr/share/nginx/html/fourth.js

# Копируем нашу конфигурацию nginx
COPY default.conf /etc/nginx/conf.d/default.conf

# Еxpose порт 80
EXPOSE 80

# Запускаем nginx
CMD ["nginx", "-g", "daemon off;"]
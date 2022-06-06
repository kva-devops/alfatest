# Приложение Alfabank

## О проекте 
#### Описание
REST-сервис для определения разницы в курсах валют за определенный промежуток времени. 
В зависимости от того стал ли курс по отношению к базовой валюте (по умолчанию - USD) за сегодня выше или ниже
вчерашнего - приложение показывает GIF-картинку, соответствующей тематики.
Сервис использует в своей работе следующие API:
 - https://docs.openexchangerates.org/docs/api-introduction
 - https://developers.giphy.com/docs/api#quick-start-guide
 

#### Технологии
 > Java 11+, Gradle, Spring (Boot, Cloud), Lombok, Junit, Log4j, Docker, REST

## Предварительная настройка файла конфигурации
1. Скачать файлы репозитория и перейти в корень проекта
2. Открыть файл настроек: `src/main/resources/application.properties`
3. Задать следующие параметры:
`rates.date.today=2022-06-06` - сегодняшняя дата
`rates.date.yesterday=2022-06-05` - вчерашняя дата
`rates.base=USD` - базовая валюта относительно которой будет происходить сравнение 
4. Список всех доступных валют можно посмотреть здесь: https://openexchangerates.org/api/currencies.json

## Сборка
1. Находясь в корне проекта, выполнить сборку проекта: `gradle clean build`

## Запуск локально
1. Находясь в корне проекта, выполнить запуск проекта: `java -jar build/libs/alfabank-0.0.1-SNAPSHOT.jar`
2. Перейти на страницу: `localhost:8080/profit/currencies/RUB/analyze`, где `RUB` можно заменить на любую валюту из списка

## Запуск через Docker Compose
1. Находясь в корне проекта, собрать docker-образ командой: `docker build -t alfabank .`
2. В файле docker-compose.yml настроить коммутацию портов (по умолчанию 8181:8080)
3. Запустить контейнер командой `docker-compose up`
4. Перейти на страницу: `localhost:8181/profit/currencies/RUB/analyze`, где `RUB` можно заменить на любую валюту из списка

## Контакты
Кутявин Владимир Анатольевич

email: vldmrqst@gmail.com

telegram: @kutiavinvladimir
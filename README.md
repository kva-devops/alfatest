# Application "Alfabank"

## About project 
#### Desc
REST service for determining the difference in exchange rates for a certain period.
Depending on whether the exchange rate against the base currency (by default - USD) has become higher or lower today
yesterday - the application shows a GIF-picture corresponding to the topic.
Сервис использует в своей работе следующие API:
Service uses this API in its work:
 - https://docs.openexchangerates.org/docs/api-introduction
 - https://developers.giphy.com/docs/api#quick-start-guide
 

#### Technologies
 > Java 11+, Gradle, Spring (Boot, Cloud), Lombok, Junit, Log4j, Docker, REST

## Init
1. Downloads repository files and go to the root of the project.
2. Open properties file: `src/main/resources/application.properties`
3. Set the following options:
`rates.date.today=2022-06-06` - today's date
`rates.date.yesterday=2022-06-05` - yesterday's date
`rates.base=USD` - the base currency against which the comparison will take place 
4. A list of all available currencies you can be found here: https://openexchangerates.org/api/currencies.json

## Build
1. From the root of the project, build it: `gradle clean build`

## Local work
1. From the root of the project, run the project: `java -jar build/libs/alfabank-0.0.1-SNAPSHOT.jar`
2. Go to the page: `localhost:8080/profit/currencies/RUB/analyze`, where `RUB` can be replaced with any currency from the list

## Docker Compose work
1. Being in the root of the project, build the docker image with the command: `docker build -t alfabank .`
2. Set up port switching in the docker-compose.yml file (default 8181:8080)
3. Start the container with the command `docker-compose up`
4. Go to the page: `localhost:8181/profit/currencies/RUB/analyze`, where `RUB` can be replaced by any currency from the list

## Contact
Kutiavin Vladimir

telegram: @kutiavinvladimir
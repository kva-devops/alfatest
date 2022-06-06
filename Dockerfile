FROM openjdk
WORKDIR alfabank
ADD build/libs/alfabank-0.0.1-SNAPSHOT.jar alfa.jar
ENTRYPOINT java -jar alfa.jar

FROM openjdk:17-jdk-alpine3.14

WORKDIR /src
COPY . /src

RUN apk update
RUN apk add dos2unix
RUN dos2unix gradlew
RUN chmod +x gradlew

RUN ./gradlew buildFatJar

WORKDIR /run

RUN cp /src/build/libs/rbiconcept-test-task-all.jar /run/server.jar

EXPOSE 8080

ENV MINIO_ENDPOINT=""
CMD java -jar /run/server.jar
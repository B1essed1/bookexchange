FROM openjdk:16-jdk-alpine
ARG JAR_FILE=target/*.jar

RUN mkdir /code
WORKDIR /code
COPY . /code
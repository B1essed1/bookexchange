FROM openjdk:16-alpine
ADD bookexchange.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]


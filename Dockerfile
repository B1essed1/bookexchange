FROM openjdk:16-alpine
ADD target/bookexchange.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]


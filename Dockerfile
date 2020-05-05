FROM openjdk:8-jdk-alpine
RUN addgroup -S restapp && adduser -S buyify -G restapp
USER buyify:restapp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

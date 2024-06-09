FROM openjdk:17
ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} repairs-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/repairs-0.0.1-SNAPSHOT.jar"]
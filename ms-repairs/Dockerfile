FROM openjdk:17
ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} ms-repairs-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/ms-repairs-0.0.1-SNAPSHOT.jar"]
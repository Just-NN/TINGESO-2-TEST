FROM openjdk:17
ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} ms-vehicles-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/ms-vehicles-0.0.1-SNAPSHOT.jar"]
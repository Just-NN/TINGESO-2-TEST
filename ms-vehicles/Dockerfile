FROM openjdk:17
ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} vehicles-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/vehicles-0.0.1-SNAPSHOT.jar"]
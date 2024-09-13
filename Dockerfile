#TODO: generate Dockerfile from gradle
FROM openjdk:17-jdk-slim
WORKDIR /friday
COPY build/libs .
EXPOSE 8090
CMD ["java", "-jar", "friday-0.0.1-SNAPSHOT.jar"]
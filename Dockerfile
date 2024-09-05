#TODO: generate Dockerfile from gradle
FROM openjdk:17-jdk-slim
WORKDIR /friday
COPY /build/libs /friday/
EXPOSE 8080
CMD ["java", "-jar", "friday-0.0.1-SNAPSHOT.jar"]
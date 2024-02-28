#start with base image containing jre
FROM openjdk:21-jdk-slim

LABEL maintainer="suleware@gmail.com"

COPY target/accounts-0.0.1-SNAPSHOT.jar accounts-0.0.1-SNAPSHOT.jar


ENTRYPOINT [ "java", "-jar", "accounts-0.0.1-SNAPSHOT.jar" ]
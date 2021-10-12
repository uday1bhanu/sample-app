FROM openjdk:11-jdk
LABEL maintainer="Uday"

COPY /build/libs/sample-app-0.0.1-SNAPSHOT.jar /

EXPOSE 8080

ENV JAVA_OPTIONS ""
CMD ["sh", "-c", "java ${JAVA_OPTIONS} -jar /sample-app-0.0.1-SNAPSHOT.jar"]
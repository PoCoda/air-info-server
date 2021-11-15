FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY target/air-info-server-0.0.1.war app.war
ENTRYPOINT ["java","-jar","/app.war"]

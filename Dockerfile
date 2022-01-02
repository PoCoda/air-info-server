FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY target/air-info-server-0.0.1-SNAPSHOT.war air-info-server.war
ENTRYPOINT ["java","-jar","/air-info-server.war"]

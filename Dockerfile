FROM eclipse-temurin:17-jre-focal
MAINTAINER Grigoriy

ADD ./target/keycloak_example-0.0.1-SNAPSHOT.jar /app/

CMD ["java", "-Xmx200m", "-jar", "/app/keycloak_example-0.0.1-SNAPSHOT.jar"]
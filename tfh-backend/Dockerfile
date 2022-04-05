FROM maven:3.8.4-openjdk-11 AS builder
ADD ./pom.xml pom.xml
ADD ./src src/
RUN mvn clean install -DskipTests

FROM openjdk:11-jre-slim
COPY --from=builder target/tfh-backend-0.0.1-SNAPSHOT.jar /src/app.jar
EXPOSE 8081
CMD ["java", "-jar", "/src/app.jar"]

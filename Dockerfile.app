FROM maven:3.10.1-eclipse-temurin-21 AS build
WORKDIR /workspace

# copy only what is necessary for a reproducible build
COPY pom.xml mvnw .
COPY .mvn .mvn
COPY src src

RUN mvn -B -DskipTests package

FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY --from=build /workspace/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]

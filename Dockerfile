FROM eclipse-temurin:17-jdk AS build
WORKDIR /workspace

COPY reservation-app/mvnw ./mvnw
COPY reservation-app/.mvn ./.mvn
RUN chmod +x ./mvnw

COPY reservation-app/pom.xml ./pom.xml
RUN ./mvnw -B -DskipTests dependency:go-offline

COPY reservation-app/src ./src
RUN ./mvnw -B -DskipTests clean package

FROM eclipse-temurin:17-jre AS runtime
WORKDIR /app

COPY --from=build /workspace/target/*.jar app.jar

EXPOSE 10000
ENTRYPOINT ["java", "-Dserver.port=10000", "-jar", "/app/app.jar"]

FROM maven:3-amazoncorretto-21 AS backend-build

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src src

RUN mvn package

FROM amazoncorretto:21-alpine

WORKDIR /app

COPY --from=backend-build /app/target/ligatenisaziemnego-0.0.1-SNAPSHOT.jar /app/backend.jar

EXPOSE 8080

CMD ["java", "-jar", "backend.jar"]
# Etapa 1: Build
FROM maven:3.9.11-eclipse-temurin-21 AS build

WORKDIR /app

# Copia apenas o pom para cache de dependências
COPY pom.xml .

RUN mvn dependency:go-offline -B

# Copia o código fonte
COPY src ./src

# Build
RUN mvn clean package -DskipTests

# Etapa 2: Runtime
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8058

ENTRYPOINT ["java", "-jar", "app.jar"]

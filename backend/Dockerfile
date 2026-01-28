# Etapa 1: Build da aplicação
FROM maven:3.9.11-eclipse-temurin-21 AS build

WORKDIR /app

# Copia os arquivos do Maven
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
COPY mvnw.cmd .

# Baixa as dependências
RUN mvn dependency:go-offline -B

# Copia o código fonte
COPY src ./src

# Compila a aplicação
RUN mvn clean package -DskipTests

# Etapa 2: Imagem final
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copia o JAR da etapa de build
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta da aplicação
EXPOSE 8058

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
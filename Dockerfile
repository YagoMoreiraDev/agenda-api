# Etapa de build
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa final
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Copiar a pasta de certificados
COPY certs /app/certs

# Expor a porta da aplicação
EXPOSE 7011

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]

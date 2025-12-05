# ------------------------------
# ETAPA 1: Construcción (Build)
# ------------------------------
# Usamos una imagen de Maven con Java 17 para compilar el proyecto
FROM maven:3.9-eclipse-temurin-17 AS build

# Establecemos el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos el archivo pom.xml y descargamos las dependencias
# (Esto optimiza el caché de Docker para no descargar todo cada vez que cambias código)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiamos el código fuente
COPY src ./src

# Compilamos el proyecto y creamos el JAR (saltando los tests para agilizar)
RUN mvn clean package -DskipTests

# ------------------------------
# ETAPA 2: Ejecución (Run)
# ------------------------------
# Usamos una imagen ligera de Java 17 para ejecutar la aplicación
FROM openjdk:17-jdk-slim

# Directorio de trabajo
WORKDIR /app

# Copiamos el JAR generado en la etapa anterior
COPY --from=build /app/target/fonda1-0.0.1-SNAPSHOT.jar app.jar

# Creamos el directorio para subida de archivos (según tu FileStorageService)
RUN mkdir -p /app/uploads

# Exponemos el puerto configurado en application.properties
EXPOSE 7073

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]
# Etapa 1: Build
FROM gradle:8.11-jdk21 AS build

WORKDIR /app

# Copiar archivos de configuración de Gradle
COPY build.gradle settings.gradle ./
COPY gradle gradle

# Descargar dependencias (se cachea si no cambian)
RUN gradle dependencies --no-daemon || true

# Copiar código fuente
COPY src src

# Construir la aplicación
RUN gradle build -x test --no-daemon

# Etapa 2: Runtime
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copiar el JAR generado desde la etapa de build
COPY --from=build /app/build/libs/*.jar app.jar

# Exponer el puerto de la aplicación
EXPOSE 8099

# Variables de entorno por defecto (se pueden sobrescribir)
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Comando de inicio
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

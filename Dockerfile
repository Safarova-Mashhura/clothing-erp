# Stage 1: Build the application
FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR /app
COPY pom.xml .
# Download dependencies first for caching layers
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests -B

# Stage 2: Create the runtime container
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/clothing-erp-1.0.0-SNAPSHOT.jar app.jar

# Add security: run app as non-root user inside container
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

EXPOSE 8080
ENV DB_HOST=db
ENV DB_PORT=5432
ENV DB_NAME=clothing_erp_db
ENV DB_USER=postgres
ENV DB_PASS=password

ENTRYPOINT ["java", "-jar", "app.jar"]

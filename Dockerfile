FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copy maven configuration
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Build dependency layer
RUN chmod +x ./mvnw && ./mvnw dependency:go-offline

# Copy source code
COPY src src

# Build the application
RUN ./mvnw package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the built artifact from the build stage
COPY --from=build /app/target/*.jar app.jar

# Set the entrypoint
ENTRYPOINT ["java", "-jar", "app.jar"]

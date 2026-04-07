FROM maven:3.9.6-eclipse-temurin-17

WORKDIR /app
COPY . .

RUN mvn clean package -DskipTests

# Debug to confirm jar exists
RUN ls -la target

# Use exact jar name (safe)
CMD ["java", "-jar", "target/vedrithm-backend-1.0.0.jar"]
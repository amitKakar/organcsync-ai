# OrganSync AI Scoring Service - Multi-stage Docker Build
FROM openjdk:21-jdk-slim as builder

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Make Maven wrapper executable
RUN chmod +x mvnw

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build the application
RUN ./mvnw clean package -DskipTests

# Production stage
FROM openjdk:21-jre-slim

# Create app user
RUN addgroup --system appgroup && adduser --system appuser --ingroup appgroup

# Set working directory
WORKDIR /app

# Install curl for health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Copy JAR file from builder stage
COPY --from=builder /app/target/organsync-ai-scoring-*.jar app.jar

# Change ownership to app user
RUN chown -R appuser:appgroup /app

# Create logs directory
RUN mkdir -p /app/logs && chown -R appuser:appgroup /app/logs

# Switch to app user
USER appuser

# Expose port
EXPOSE 8086

# Set JVM options
ENV JAVA_OPTS="-Xms512m -Xmx1g -XX:+UseG1GC -XX:+UseStringDeduplication"

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3   CMD curl -f http://localhost:8086/ai-scoring/actuator/health || exit 1

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

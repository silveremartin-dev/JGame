# JGame Server Dockerfile
# Authors: Google Gemini (Antigravity), Silvere Martin-Michiellot

FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app
COPY pom.xml .
COPY jgame-core/pom.xml jgame-core/
COPY jgame-persistence/pom.xml jgame-persistence/
COPY jgame-server/pom.xml jgame-server/
COPY jgame-games/pom.xml jgame-games/

# Download dependencies first (cached layer)
RUN --mount=type=cache,target=/root/.m2 \
    mvn dependency:go-offline -B

# Copy source and build
COPY . .
RUN --mount=type=cache,target=/root/.m2 \
    mvn package -DskipTests -pl jgame-core,jgame-persistence,jgame-server -am

# Runtime image
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy JARs
COPY --from=builder /app/jgame-server/target/*.jar app.jar
COPY --from=builder /app/jgame-core/target/*.jar libs/
COPY --from=builder /app/jgame-persistence/target/*.jar libs/

# Create data directory
RUN mkdir -p /app/data /app/plugins

EXPOSE 8080

ENV JAVA_OPTS="-Xmx512m"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

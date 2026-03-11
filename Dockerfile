FROM node:20-slim as frontend-builder
WORKDIR /project/client
COPY client/package.json client/package-lock.json ./
RUN npm ci
COPY client/ ./
RUN npm run build

FROM maven:3.9-eclipse-temurin-17 AS backend-builder
WORKDIR /project
COPY pom.xml ./
RUN mvn dependency:go-offline
COPY src ./src
COPY --from=frontend-builder /project/client/dist ./src/main/resources/public
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre
RUN apt-get update && apt-get install -y dumb-init && rm -rf /var/lib/apt/lists/*
RUN addgroup --system juser && adduser --system --shell /bin/false --ingroup juser juser
COPY --from=backend-builder /project/target/spaceship-0.0.1.jar /app/
WORKDIR /app
RUN chown -R juser:juser /app
USER juser
ENTRYPOINT ["dumb-init", "java", "-jar", "spaceship-0.0.1.jar"]
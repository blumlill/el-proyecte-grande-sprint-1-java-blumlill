FROM node:20-slim as frontend-build
COPY . /project
WORKDIR /project/client
RUN npm ci
RUN npm run build

FROM maven:3.9.1-eclipse-temurin-17-alpine@sha256:2bda441f6ad8c4d3185ac7331be3f14528c49c51435e11acf94dce5402a98497 AS backend-builder
COPY --from=frontend-build /project/src /project/src
COPY --from=frontend-build /project/client/dist /project/src/main/resources/public
COPY --from=frontend-build /project/pom.xml /project/pom.xml
WORKDIR /project
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17.0.7_7-jre-alpine@sha256:7cbe01fd3d515407f1fda1e68068831aa6ae4b6930d76cdaa43736dc810bbd1b
RUN apk add dumb-init
RUN addgroup --system juser && adduser -S -s /bin/false -G juser juser
COPY --from=backend-builder /project/target/spaceship-0.0.1.jar /app/
WORKDIR /app
RUN chown -R juser:juser /app
USER juser
ENTRYPOINT ["dumb-init", "java", "-jar", "spaceship-0.0.1.jar"]
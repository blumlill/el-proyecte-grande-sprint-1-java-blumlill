# Agent Information for Minuend Spaceship Game

This file provides useful information for AI agents working on this repository.

## Project Overview
Minuend Spaceship Game is a browser-based strategy game where the user acts as a space station commander. The goal is to upgrade the station, build ships, and collect resources from planets.

## Tech Stack
- **Backend:** Java 17, Spring Boot 3.0.6, Spring Security, Spring Data JPA.
- **Frontend:** React 18, React Router 6.
- **Database:** PostgreSQL.
- **Build Tools:** Maven (backend), NPM (frontend).
- **Containerization:** Docker & Docker Compose.

## Project Structure
- `/`: Root directory contains the Maven backend project.
  - `src/main/java`: Backend source code.
  - `src/main/resources`: Configuration files (e.g., `application.properties`).
  - `src/test/java`: Backend tests.
- `/frontend`: Contains the React frontend project.
  - `src/`: Frontend source code.
  - `public/`: Static assets.

## Common Commands

### Backend
- Build: `./mvnw clean install`
- Test: `./mvnw test`
- Run: `java -jar -Dusername={db_user} -Dpassword={db_pass} target/spaceship-0.0.1.jar`

### Frontend (navigate to `/frontend` first)
- Install dependencies: `npm install` (or `npm ci` for clean install)
- Test: `npm test`
- Run: `npm start`

## Configuration Details

### Database
- Default connection: `jdbc:postgresql://localhost:5432/spaceship`
- Configured in `src/main/resources/application.properties`.
- Authentication is required via `${username}` and `${password}` system properties or environment variables.
- Docker environment (see `docker-compose.yaml`):
  - User: `spaceship`
  - Password: `to_1nf1n1ty_And_Bey0nd`

### Frontend Proxy
- The frontend is configured to proxy API requests to `http://localhost:8080/` (defined in `frontend/package.json`).

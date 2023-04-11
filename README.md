# MINUEND SPACESHIP GAME

Browser-based strategy game, the user can act as a space station commander. The aim is to upgrade your station, build
ships and upgrade their parts. Ships can be used to discover locations and collect resources, which are necessary for building
and upgrading.

## Features
 - After creating an account and logging in, the user can play the game
 - Scout ships can be sent on missions to discover new planets
 - Mining ships can be sent on missions to planets, which yield resources, but drain the planets
 - Ships can be upgraded, their stats effect the results of the missions they are sent on
 - New ships can be built, which allows for more missions to be run at the same time
 - The station can be upgraded, increasing the resources and ships it can store
 - Admins can edit the cost and effect of upgrades

## Stack
- Frontend: JavaScript React
- Backend: Java SpringBoot + Spring Security + JPA
- Database: PostgreSQL

## How to run

### With Docker:
- you need Docker and Docker compose installed.
- clone the repository and be sure to be on "development" branch
- run `docker compose up -d` in the repository root
- wait for it to build and start the containers
- once the containers are running, give a couple seconds for the server to start up, and then you can reach the app on localhost:8080
- you can stop the app with `docker compose stop`

### Without Docker
Database:
- You need to have PostgreSQL installed
- Create a database called `spaceship`
- The backend will by default will expect the db to be running on localhost:5432, if you wish to change that, or use a different name for it, you can do so in the src/main/resources/application.properties file

Backend:
- You need JDK installed
- From the root directory of the project, run `./mvnw clean install` to build the backend
- Once it's built, if the db is in place, you can start it with `java -jar -Dusername={your_database_username} -Dpassword={your_database_password} target/spaceship-0.0.1.jar`
- The server will need a few seconds to start, after that, you can reach it on localhost:8080, if you wish to change that, add --server.port={port_you_want} to the end of the start command

Frontend:
- You need Node.js and npm installed
- Navigate to the frontend directory
- Install dependencies with the `npm ci` command
- The frontend will assume you're running the backend on localhost:8080, if you changed that, you need to update the proxy in the bottom of the package.json file
- You can start the frontend with the `npm run` command, which will make the frontend reachable on localhost:3000, if you want to change that, create a .env.local file, and write PORT={port_you_want} into it before starting the frontend

## After running

- Play the game by creating your own user with registering
- Play around with the demo user (username: DemoMan, password: password)
- Try the admin features with the admin user (username: Mr. Admin, password: password)

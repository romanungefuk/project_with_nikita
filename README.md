# project_with_nikita
1) sudo apt install openjdk-11-jdk
2) sudo apt install maven
3) sudo apt install mysql-server
4) configure mysql-server
5) CREATE DATABASE SUBD_nikita CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
6) init db with queries from project_with_nikita/src/main/resources/database/initDB.sql
7) fill tables with queries from project_with_nikita/src/main/resources/database/populateDB.sql
8) git clone https://github.com/romanungefuk/project_with_nikita.git
9) change user and password from database user in project_with_nikita/src/main/resources/application-mysql.yaml
10) cd project_with_nikita
11) mvn clean
12) mvn package
13) cd target
14) java -jar backend-equp-prod.jar
15) if all is ok, then http://localhost:8090/api/v1/auth/check will response "ok"

# Docker
https://hub.docker.com/repository/docker/martold/martoldhub

1. docker network create todo-app - создаешь сеть в докере
2. docker run -d -p 3306:3306 --name mysql\
     --network todo-app --network-alias mysql \
     -v todo-mysql-data:/var/lib/mysql \
     -e MYSQL_ROOT_PASSWORD=supersecret \
     martold/martoldhub:mysql_latest - создаешь контейнер с базой.
3. docker run -d -p 8080:8080 --network todo-app martold/martoldhub:app_latest - создаешь контейнер с приложением.

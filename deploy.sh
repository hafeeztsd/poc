docker network create app-mysql_network
docker container run --name mysqlhost --network app-mysql_network -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=task_management -d mysql:5.7
mvn clean install
docker image build -t app-poc .
docker-compose up 
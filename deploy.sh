git clone https://github.com/hafeeztsd/poc.git
cd poc/
docker network create app-mysql_network
mvn clean install
docker-compose up

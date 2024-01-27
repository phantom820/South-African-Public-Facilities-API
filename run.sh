source .env
source ./build.sh
java -jar target/south.african.data.api-0.0.1-SNAPSHOT.jar --server.port=$PORT server.address=$ADDRESS \
--spring.datasource.username=${DB_USERNAME} --spring.datasource.password=${DB_PASSWORD} \
--spring.datasource.url=${DB_URL}

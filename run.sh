source .env
source ./build.sh
if [ $? -eq 0 ]
then
  gradle bootRun -Dserver.port=$PORT -Dserver.address=$ADDRESS \
  -Dspring.datasource.username=${DB_USERNAME} -Dspring.datasource.password=${DB_PASSWORD} \
  -Dspring.datasource.url=${DB_URL}
else
  exit 1
fi

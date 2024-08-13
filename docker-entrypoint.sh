echo "wait db server"
dockerize -wait tcp://db:3306 -timeout 20s

echo "start spring server"
java -jar /app.jar
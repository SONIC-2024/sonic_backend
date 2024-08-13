FROM openjdk:17-jdk-alpine
ARG JAR_FILE=build/libs/sonic-backend-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.9.0/wait /wait
RUN chmod +x /wait
CMD /wait && java -jar app.jar

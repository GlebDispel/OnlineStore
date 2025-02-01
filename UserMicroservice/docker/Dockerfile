FROM openjdk:22

ARG APP_JAR=*.jar

#откуда копируем и куда
COPY ${APP_JAR} UserMicroservice.jar

ENTRYPOINT ["java", "-jar", "UserMicroservice.jar"]

EXPOSE 8080
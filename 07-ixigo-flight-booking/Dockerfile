FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/*.jar myapp.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "myapp.jar"]
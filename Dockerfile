FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

# Install Maven and build the JAR
RUN apt-get update && apt-get install -y maven \
    && mvn clean package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "target/Unsolved-0.0.1-SNAPSHOT.jar"]
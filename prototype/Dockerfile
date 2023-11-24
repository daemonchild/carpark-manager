#
# Build Container
#
FROM maven:3.6.0-jdk-11-slim as build
WORKDIR /usr/build
COPY . /usr/build/prototype/
RUN mvn -f /usr/build/prototype/pom.xml package

# App Continer
FROM gcr.io/distroless/java
WORKDIR /usr/app
COPY --from=build /usr/build/prototype/target/*.jar /usr/app/carpark-manager.jar
COPY ../database-files/database.csv /usr/app/database-files/
ENTRYPOINT ["java","-jar","/usr/app/carpark-manager.jar"] 
#
# Build Container
#
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /usr/app
ADD . $HOME
RUN --mount=type=cache,target=/root/.m2 ./mvn -f $HOME/pom.xml clean package


FROM gcr.io/distroless/java
WORKDIR /usr/app
COPY prototype/target/prototype-1.0-SNAPSHOT.jar /usr/app/carpark-manager.jar
COPY database-files/database.csv /usr/app/database-files/
ENTRYPOINT ["java","-jar","/usr/app/carpark-manager.jar"] 
FROM gcr.io/distroless/java
WORKDIR /usr/app
COPY prototype/target/prototype-1.0-SNAPSHOT.jar /usr/app/carpark-manager.jar
COPY database-files/database.csv /usr/app/database-files/
ENTRYPOINT ["java","-jar","/usr/app/carpark-manager.jar"] 
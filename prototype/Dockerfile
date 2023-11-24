# 
# File:     Prototype Dockerfile
#
# Course:   DAT4001 
# Date:     Autumn 2023
# Group:    
#           Ross Grant
#           Sam Loftus
#           Tom Rowan
# 


#
# Maven Build Container
#
FROM maven:3.6.0-jdk-11-slim as build
LABEL Author: daemonchild
WORKDIR /usr/build
COPY . /usr/build/prototype/
RUN mvn -f /usr/build/prototype/pom.xml package

#
# App Container
#
FROM gcr.io/distroless/java
LABEL Author: daemonchild
WORKDIR /usr/app
COPY --from=build /usr/build/prototype/target/*.jar /usr/app/carpark-manager.jar
COPY ../database-files/database.csv /usr/app/database-files/
ENTRYPOINT ["java","-jar","/usr/app/carpark-manager.jar"] 
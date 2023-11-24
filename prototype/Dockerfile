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
FROM maven:3.6.0-jdk-11-slim AS build
LABEL Author: daemonchild
WORKDIR /usr/build
COPY . /usr/build/prototype/
RUN mvn -f /usr/build/prototype/pom.xml package

#
# Python to Generate Test Database
#
FROM python:3.12 AS publish
LABEL Author: daemonchild
WORKDIR /app
COPY ./../extra/generate-test-database/app/ /app
RUN mkdir /app/output
RUN pip install -r requirements.txt
RUN python generate-test-data.py 

#
# App Container
#
FROM gcr.io/distroless/java as base
LABEL Author: daemonchild
WORKDIR /usr/app
COPY --from=build /usr/build/prototype/target/*.jar /usr/app/carpark-manager.jar
COPY --from=publish /app/output/database.csv /usr/app/database-files/
#COPY ../database-files/database.csv /usr/app/database-files/
ENTRYPOINT ["java","-jar","/usr/app/carpark-manager.jar"] 


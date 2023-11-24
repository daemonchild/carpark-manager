# carpark-manager

Course Details:
- DAT4001 - Programming Fundamentals
- Autumn 2023
- Group A

Project Team:
- Ross Grant (st20271524)
- Sam Loftus (st20287201)
- Tom Rowan (st20285213) 

Project Tutor:
- Dr Barry Bentley

## Project Brief

"You have been asked to develop a car park management system to track the entry and exit times of vehicles, and allow searches on historic data."

The following is written to provide 'our customer' with several options to run the prototype on their system.

### Prototype
The supplied version of the project (1.0) is a **prototype**. A sample database is included, providing data for 50 visits per day, from 1-29 November 2023.

We have provided 'rough-and-ready' utility software to generate additional sample data as required. (Further information in extra/generate-test-database/README.md).

## Build Notes

There are a number of ways to build this Java project.

### Build Local Docker Image

The preferred way to build is to use Docker. This guarentees that the build will result in a consistent version of the project that is independent of any local libraries or file paths.

The Dockerfile provides a temporary 'build' image and a runnable container image without needing to compile or build the project code within an IDE. The build process is carried out automatically by the container.

We have provided a option to automatically build docker images locally.  This can be carried out using Apache Maven. (If this is not installed in your environment, it can be obtained from https://maven.apache.org/download.cgi?.)

```
c:\your_path\carpark-manager> mvn install -f .\prototype\pom.xml
c:\your_path\carpark-manager> docker images
REPOSITORY                              TAG       IMAGE ID       CREATED             SIZE
carparkmanager/generate-test-database   latest    46a7b87d9c33   34 seconds ago      210MB
carparkmanager/prototype                0.1       46a7b87d9c33   34 seconds ago      210MB
c:\your_path\carpark-manager> docker run -ti --name cpm-prototype carparkmanager/prototype:1.0
[...Carpark Manager app opens here...]
```

(Note: the generate-test-database container is created at the same time for conveinience.)

After the first run, to remove and rebuild the images from source, you can use:

```
c:\your_path\carpark-manager> mvn clean install -f .\prototype\pom.xml
```

If you don't have maven, you will need to compile the sources manually (see below). You can then create the Docker image manually by using:

```
c:\your_path\carpark-manager> cd prototype
c:\your_path\carpark-manager> docker build -t mycarparkmanager:1.0 .

```
To run this image, use:

```
c:\your_path\carpark-manager> docker run -ti --name cpm-prototype  mycarparkmanager:1.0 
```

## Restarting a closed container

The container created above is not deleted when you exit the Carpark Manager application. To reenter the container, and continue, please use:

```

```


## Pull Docker Image from Docker Hub

We have provided an image on the Docker Hub repository should there be any issues with creating a local Docker image. This can be pulled from the repository and run in a single command: 

```
c:\your_path\carpark-manager> docker run -ti --rm daemonchild/carparkmanager-prototype:1.0
```

(Please note that the image *name* is different from the previous commands; the image was created from the latest source code prior to submission.)

### Maven Build

An automated build can be carried out using Apache Maven.
If not installed in your environment, it can be obtained from https://maven.apache.org/download.cgi?. 


```
c:\your_path\carpark-manager> mvn package -f .\prototype\pom.xml
c:\your_path\carpark-manager> java -jar .\prototype\target\prototype-0.1.jar
```

### Manual Build

If all other tools are unavailable, it is possible to build the project as follows:





```

c:\your_path\carpark-manager> cd extra\generate-test-database
c:\your_path\carpark-manager\extra\generate-test-database> docker build -t carparkmanager/generate-test-database:latest .
```

To get output from this container, you will need to share a local folder. This example is using the image to generate a fresh database and *overwrite* the existing sample database.
```
docker run --rm -it -v ${PWD}/prototype/database-files/:/app/output/ carparkmanager/generate-test-database:latest
```
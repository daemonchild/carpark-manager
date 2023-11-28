# generate-test-data.py
You can use this utility to generate a test database.
It is 'rough and ready' and requires an edit to the file to modify parameters.

**We do not intend for this extra work to be marked!**

## Run in Python
```
c:\your_path\carpark-manager> cd prototype\extra\generate-test-database\app\
c:\your_path\carpark-manager\extra\generate-test-database> python .\generate-test-database.py
```
The output is placed in \output\database.csv
You can move this to the application database folder as follows:

```
c:\your_path\carpark-manager\extra\generate-test-database> move .\output\database.csv ..\..\..\database-files\database.csv
```

## Using Docker
Another way to run this utility is to use a Docker container.
This is used in the automated build process.

```
c:\your_path\carpark-manager> cd prototype\extra\generate-test-database
c:\your_path\carpark-manager\extra\generate-test-database> docker build -t carparkmanager/generate-test-database:latest .
```

To get output from this container, you will need to share a local folder. This example is using the image to generate a fresh database and *overwrite* the existing sample database.
```
c:\your_path\carpark-manager\extra\generate-test-database> docker run --rm -it -v ${PWD}/../../database-files/:/app/output/ carparkmanager/generate-test-database:latest
```
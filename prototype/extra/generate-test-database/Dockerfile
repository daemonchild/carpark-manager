#
# Python to Generate Test Database
#
FROM python:3.12 
LABEL Author: daemonchild
WORKDIR /app
COPY app/ /app
RUN mkdir /app/output
RUN pip install -r requirements.txt
ENTRYPOINT [ "python", "generate-test-data.py" ]
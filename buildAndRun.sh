#!/bin/sh
mvn clean package && docker build -t it.usr/Estrazioni .
docker rm -f Estrazioni || true && docker run -d -p 9080:9080 -p 9443:9443 --name Estrazioni it.usr/Estrazioni
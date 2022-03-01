@echo off
call mvn clean package
call docker build -t it.usr/Estrazioni .
call docker rm -f Estrazioni
call docker run -d -p 9080:9080 -p 9443:9443 --name Estrazioni it.usr/Estrazioni
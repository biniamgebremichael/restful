#!/bin/bash
#TODO: manual build - convert to github action yml file for auto-build

# requires:
# 1. java 17 (download from: https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
# 2. gradle
# 3. docker and docker-compose

#build complete jar file
sh gradlew bootJar

#stop and clean old image before upgrade
sudo ./docker-compose  -f docker-compose.yaml  down
sudo docker rmi restful-web
#build image and deploy
sudo ./docker-compose  -f docker-compose.yaml  up -d

# run these two commands if you do not have docker-compose
#sudo docker build . -t friday:latest
#sudo docker run -p 8090:8090 friday:latest

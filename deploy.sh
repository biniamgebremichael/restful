#TODO: manual build - convert to github action yml file for auto-build
#!/bin/bash

# requires:
# 1. java 17 (download from: https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
# 2. gradle
# 3. docker and docker-compose

#build complete jar file
sh gradlew bootJar

sudo ./docker-compose  -f docker-compose.yaml  up -d

# run these two commands if you do not have docker-compose
#sudo docker build . -t friday:latest
#sudo docker run -p 8090:8090 friday:latest

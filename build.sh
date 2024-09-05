#TODO: manual build - convert to github action yml file for auto-build
#!/bin/bash
sh gradlew bootJar
sudo docker build . -t friday:latest
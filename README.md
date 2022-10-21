# Handling the synchronization Jenkins - Docker on Rocky Linux 8.6

## Installation of Docker and Jenkins
### Jenkins
```sh
# in/the/github/repository
chmod 755 docker_for_jenkins/jenkins-install.sh
docker_for_jenkins/jenkins-install.sh
```
### Docker
```sh
# in/the/github/repository
chmod 755 docker_for_jenkins/docker-install.sh
docker_for_jenkins/docker-install.sh
```

## Docker API ?

### Setup of the Docker API
```sh
# in/the/github/repository
chmod 755 docker_for_jenkins/dockerAPI-install.sh
docker_for_jenkins/dockerAPI-install.sh
```
### Check of the API
```sh
curl localhost:4243/version
```
```sh
# Output example
{"Platform":{"Name":"Docker Engine - Community"},"Components":[{"Name":"Engine","Version":"20.10.19","Details":{"ApiVersion":"1.41",...
```

## Setup docker container as agent for Jenkins

### Build image for jenkins agent (rocky linux, ubuntu and alpine)
```sh
# in/the/github/repository
docker build -t [image-name] [ubuntu/rocky/alpine]-img/
```

### Configure Jenkins to add your container as an agent

[Currently working on the next part]

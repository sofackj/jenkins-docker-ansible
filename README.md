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

## Build a docker container from a pipeline
- Build a container and use command in it
```sh
// Filter node with the label docker
node('docker') {
    // Assigne a container to a variable
    def httpd = docker.image('httpd:alpine')
    // Interact inside the container
    httpd.inside {
    // For example, check the OS version of the container
    sh "cat /etc/*release*"
  }
}
```
- Build a container and use command in it
```sh
node('docker') {
  docker.image('httpd:alpine').withRun('-p 8081:80') {c ->
    sh "curl -i http://localhost:8081/"
    IP_DOCKER = sh (
        script: "docker inspect --format='{{.NetworkSettings.Networks.bridge.IPAddress}}' ${c.id}",
        returnStdout: true
    ).trim()
    echo IP_DOCKER
    sh "ping -c 3 ${IP_DOCKER}"
  }
}
```

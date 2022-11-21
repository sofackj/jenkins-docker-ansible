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

## Setup a configuration as code (casc) jenkins container

Thanks to [DigitalOcean](https://www.digitalocean.com/community/tutorials/how-to-automate-jenkins-setup-with-docker-and-jenkins-configuration-as-code) for the general procedure and [jdstamp](https://github.com/jdstamp/ccmb-jenkins) and [ultrabright](https://github.com/ultrabright/docker-jenkins) for further informations and debugs !!

For more details about the casc.yml file, check the website [here](https://verifa.io/blog/getting-started-with-jenkins-config-as-code/index.html)

### V1 - Steps to run the image

- Build the image

```sh
docker build -t my-jenkins \
docker_for_jenkins/dockerfiles/jenkins_casc/
```

- Create a volume

```sh
docker volume create jenkins_volume
```

- Run the container

```sh
docker run \
-d \
--rm \
--restart always \
--name my-jenkins \
-p 8085:8080 \
-p 50000:50000 \
-v jenkins_volume:/var/jenkins_home \
my-jenkins
```

Everything should be ready at the address : ```<IP docker host>:8085```

### V2 - Steps to run the image

- Build the image

```sh
docker build -t my-jenkins \
docker_for_jenkins/dockerfiles/jenkins_casc_v2/
```

- Create a volume

```sh
docker volume create jenkins
```

- Setup your credentials following the template ```docker_for_jenkins/dockerfiles/jenkins_casc_v2/casc_config/.env_template```

```sh
# Create a .env file listed in .gitignore and edit it with your secrets
vim .env
```

- Run the container

```sh
docker run \
-d \
--restart always \
--name my-jenkins \
-p 8085:8080 \
-p 50000:50000 \
--env-file .env \
-v jenkins:/var/jenkins_home \
--mount type=bind,source=$(pwd)/docker_for_jenkins/dockerfiles/jenkins_casc_v2/casc_config/casc.yml,target=/var/jenkins_home/casc.yml \
--mount type=bind,source=$(pwd)/docker_for_jenkins/dockerfiles/jenkins_casc_v2/casc_config/seedjob.groovy,target=/usr/local/seedjob.groovy \
my-jenkins
```

Everything should be ready at the address : ```<IP docker host>:8085```

## Setup docker container as agent for Jenkins

### Build image for jenkins agent (rocky linux, ubuntu and alpine)
```sh
# in/the/github/repository
docker build -t [image-name] [ubuntu/rocky]-img/
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

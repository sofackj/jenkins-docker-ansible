#!/bin/bash

sudo -i <<EOF

# Add permission to user, using docker without sudo
usermod -aG docker devops
usermod -aG docker jenkins

# Create permanent storage for pushed images
mkdir -p /registry/data

EOF

# Start the registry container after pulling the registry image
docker run -d \
  -p 5000:5000 \
  --name registry \
  -v /registry/data:/var/lib/registry \
  --restart always \
  registry:2

# Clone the repo https://github.com/sofackj/software-install.git
git clone -b docker-registry --single-branch https://github.com/sofackj/software-install.git

# Go in the directory
cd software-install/

# Build docker image
for dockerfile in rocky ubuntu
do
    docker build -t $dockerfile-agent docker_for_jenkins/$dockerfile-img/base_docker_agent/
    docker tag $dockerfile-agent localhost:5000/$dockerfile-agent:latest
    docker push localhost:5000/$dockerfile-agent:latest
    docker rmi $dockerfile-agent localhost:5000/$dockerfile-agent:latest
done

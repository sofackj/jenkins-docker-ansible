#!/bin/bash

##-> Installation of Docker on Rocky Linux 8.6
# https://docs.rockylinux.org/gemstones/docker/

sudo -i <<EOF
#Update of the system
dnf update -y

#Installation of widgets
dnf install -y tree wget

#Create the docker repo (Docker not in the defaul repo)
dnf config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo

#Installation of Docker community edition (not engine)
dnf -y install docker-ce docker-ce-cli containerd.io docker-compose-plugin --allowerasing

#Enabling the docker service (Docker service already started)
systemctl --now enable docker

#Give permission to any user for using Docker
#Replace [username] by the chosen user and uncomment the line below
#usermod -aG docker [username]
EOF

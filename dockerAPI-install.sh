#!/bin/bash

##-> Setup for the Docker API (useful for jenkins)
# https://devopscube.com/docker-containers-as-build-slaves-jenkins/
# https://narenchejara.medium.com/how-to-configure-docker-container-as-build-slaves-for-jenkins-d7795f78402d
# https://medium.com/xebia-engineering/using-docker-containers-as-jenkins-build-slaves-a0bb1c9190d

sudo -i <<EOF

#Replace the value of "ExecStart=" in /lib/systemd/system/docker.service by :
#ExecStart=/usr/bin/dockerd -H tcp://0.0.0.0:4243 -H unix:///var/run/docker.sock

#Restart the docker service
systemctl daemon-reload
service docker restart

#Test the resul with
curl http://localhost:4243/version

#It should give as output a JSON type content

EOF

#Slave Dockerfile example
#Be sure to replace the Java version with the version 11
cat <<EOF > Dockerfile
FROM ubuntu:18.04

# Make sure the package repository is up to date.
RUN apt-get update && \
    apt-get install -qy git && \
# Install a basic SSH server
    apt-get install -qy openssh-server && \
    sed -i 's|session    required     pam_loginuid.so|session    optional     pam_loginuid.so|g' /etc/pam.d/sshd && \
    mkdir -p /var/run/sshd && \
# Install JDK 11
    apt-get install -qy openjdk-11-jdk && \
# Cleanup old packages
    apt-get -qy autoremove && \
# Add user jenkins to the image
    adduser --quiet jenkins && \
# Set password for the jenkins user (you may want to alter this).
    echo "jenkins:password" | chpasswd && \
    mkdir /home/jenkins/.m2

# Copy authorized keys
COPY .ssh/authorized_keys /home/jenkins/.ssh/authorized_keys

RUN chown -R jenkins:jenkins /home/jenkins/.m2/ && \
    chown -R jenkins:jenkins /home/jenkins/.ssh/

# Standard SSH port
EXPOSE 22

CMD ["/usr/sbin/sshd", "-D"]
EOF

#Image building
#Don't forget the .ssh/authorized_keys before the build phase
sudo -i <<EOF
docker image build -t docker-slave .
EOF

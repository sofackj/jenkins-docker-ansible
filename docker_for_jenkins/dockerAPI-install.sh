#!/bin/bash

##-> Setup for the Docker API (useful for jenkins)
# https://devopscube.com/docker-containers-as-build-slaves-jenkins/
# https://narenchejara.medium.com/how-to-configure-docker-container-as-build-slaves-for-jenkins-d7795f78402d
# https://medium.com/xebia-engineering/using-docker-containers-as-jenkins-build-slaves-a0bb1c9190d

sudo -i <<EOF

#Replace the value of "ExecStart=" in /lib/systemd/system/docker.service by :
grep ExecStart= /lib/systemd/system/docker.service | xargs -I old_exp sed -i 's|old_exp|ExecStart=/usr/bin/dockerd -H tcp://0.0.0.0:4243 -H unix:///var/run/docker.sock|g' /lib/systemd/system/docker.service

#Restart the docker service
systemctl daemon-reload
service docker restart

#Test the resul with
curl http://localhost:4243/version

#It should give as output a JSON type content

EOF

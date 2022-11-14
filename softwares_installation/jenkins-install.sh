#!/bin/bash

##-> Installation of Jenkins
# Thanks to Atlantic.net
# https://www.atlantic.net/dedicated-server-hosting/how-to-install-jenkins-on-rocky-linux-8/

sudo -i <<EOF
#Update of the system
dnf update -y

# Installation of widgets
dnf install -y tree wget

#Installation of Java (version 11)
#To get the javac command, java-11-openjdk-devel is needed
dnf install -y java-11-openjdk.x86_64 java-11-openjdk-devel.x86_64

#Create a repo (Jenkins is not included in default repository)
wget -O /etc/yum.repos.d/jenkins.repo https://pkg.jenkins.io/redhat-stable/jenkins.repo

#Download and import the Jenkins GPG key
rpm --import https://pkg.jenkins.io/redhat-stable/jenkins.io.key

#Install Jenkins
dnf install -y jenkins --nobest

#Start and enable jenkins service
systemctl start jenkins
systemctl enable jenkins

#Configure Firewalld (for default port 8080)
systemctl start firewalld
firewall-cmd --permanent --zone=public --add-port=8080/tcp
firewall-cmd --reload

#Display the initial admin password
cat /var/lib/jenkins/secrets/initialAdminPassword

EOF

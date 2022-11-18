FROM jenkins/jenkins:lts-jdk11

# Environment variables
# Avoid the pass by the statndard setup
# Setup the path for the jenkins configuration
ENV JAVA_OPTS -Djenkins.install.runSetupWizard=false
ENV CASC_JENKINS_CONFIG /var/jenkins_home/casc.yml

# Install plugins for jenkins
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN jenkins-plugin-cli -f /usr/share/jenkins/ref/plugins.txt
